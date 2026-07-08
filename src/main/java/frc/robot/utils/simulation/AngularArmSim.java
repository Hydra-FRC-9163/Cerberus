package frc.robot.utils.simulation;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Score.claw.ClawHardware;
import frc.robot.subsystems.Score.linear.LinearHardware;

/**
 * Physics simulation for the angular (pivot) stage of the scoring mechanism, using
 * SingleJointedArmSim + SparkMaxSim. Also owns:
 *  - a simple intake "do we have a game piece" state machine (position + intake gated),
 *  - a combined Mechanism2d visualization drawing arm angle + linear extension + intake
 *    together, since both are needed to draw the mechanism accurately.
 *
 * TUNE THESE for your actual mechanism - these are placeholders:
 *  - GEARING, ARM_LENGTH_METERS, MOMENT_OF_INERTIA: from your CAD/gearbox
 *  - MIN/MAX_ANGLE: physical soft limits, in degrees below for readability
 *  - COLLECT_ANGLE / COLLECT_EXTENSION ranges: where the intake can actually pick up a piece
 */
public class AngularArmSim extends SubsystemBase {

    private static final double GEARING = 60.0;
    private static final double ARM_LENGTH_METERS = 0.5;
    private static final double ARM_MASS_KG = 3.0;
    private static final double MOMENT_OF_INERTIA =
        SingleJointedArmSim.estimateMOI(ARM_LENGTH_METERS, ARM_MASS_KG);

    private static final double MIN_ANGLE_DEG = -30.0;
    private static final double MAX_ANGLE_DEG = 100.0;
    private static final double STARTING_ANGLE_DEG = 0.0;
    private static final boolean SIMULATE_GRAVITY = true;

    // Intake can only pick up a piece within this angle/extension window.
    private static final double COLLECT_ANGLE_MIN_DEG = -25.0;
    private static final double COLLECT_ANGLE_MAX_DEG = -5.0;
    private static final double COLLECT_EXTENSION_MAX_METERS = 0.1;
    private static final double INTAKE_RUNNING_THRESHOLD = 0.1; // fraction of full speed

    private final SparkMaxSim angularMotorSim;
    private final SingleJointedArmSim armSim;
    private final ClawHardware clawHardware;
    private final LinearArmSim linearArmSim;
    private final LinearHardware linearHardware;

    private boolean hasGamePiece = false;

    // --- Mechanism2d visualization ---
    private final Mechanism2d mechanism = new Mechanism2d(2.0, 1.5);
    private final MechanismRoot2d mechanismRoot = mechanism.getRoot("ScoreRoot", 1.0, 0.1);
    private final MechanismLigament2d armLigament;
    private final MechanismLigament2d intakeLigament;

    public AngularArmSim(ClawHardware clawHardware, LinearHardware linearHardware, LinearArmSim linearArmSim) {
        this.clawHardware = clawHardware;
        this.linearHardware = linearHardware;
        this.linearArmSim = linearArmSim;

        DCMotor gearbox = DCMotor.getNEO(1);
        angularMotorSim = new SparkMaxSim(linearHardware.AngularMotor, gearbox);

        armSim = new SingleJointedArmSim(
            gearbox,
            GEARING,
            MOMENT_OF_INERTIA,
            ARM_LENGTH_METERS,
            Units.degreesToRadians(MIN_ANGLE_DEG),
            Units.degreesToRadians(MAX_ANGLE_DEG),
            SIMULATE_GRAVITY,
            Units.degreesToRadians(STARTING_ANGLE_DEG)
        );

        armLigament = mechanismRoot.append(
            new MechanismLigament2d("Arm", ARM_LENGTH_METERS, STARTING_ANGLE_DEG, 6, new edu.wpi.first.wpilibj.util.Color8Bit(255, 255, 255))
        );
        intakeLigament = armLigament.append(
            new MechanismLigament2d("Intake", 0.15, 90, 6, new edu.wpi.first.wpilibj.util.Color8Bit(255, 0, 0))
        );

        SmartDashboard.putData("ScoreMechanism", mechanism);
    }

    @Override
    public void simulationPeriodic() {
        double vbus = RobotController.getBatteryVoltage();

        double appliedVoltage = angularMotorSim.getAppliedOutput() * vbus;
        armSim.setInputVoltage(appliedVoltage);
        armSim.update(0.02);

        double motorRPM = Units.radiansPerSecondToRotationsPerMinute(armSim.getVelocityRadPerSec()) * GEARING;
        angularMotorSim.iterate(motorRPM, vbus, 0.02);

        updateIntakeSim();
        updateMechanism();
    }

    /**
     * Simple position-gated game-piece state machine. No physics for the piece itself -
     * just: is the intake spinning to collect, AND is the mechanism positioned where
     * a piece would actually be reachable. Real vision-based piece detection would
     * replace this if you're simulating that too.
     */
    private void updateIntakeSim() {
        double clawSpeed = clawHardware.clawMotor.get();
        double angleDeg = Units.radiansToDegrees(armSim.getAngleRads());
        double extensionMeters = linearArmSim.getHeightMeters(linearHardware);

        boolean inCollectPosition =
            angleDeg >= COLLECT_ANGLE_MIN_DEG && angleDeg <= COLLECT_ANGLE_MAX_DEG
            && extensionMeters <= COLLECT_EXTENSION_MAX_METERS;

        if (!hasGamePiece && clawSpeed > INTAKE_RUNNING_THRESHOLD && inCollectPosition) {
            hasGamePiece = true;
        } else if (hasGamePiece && clawSpeed < -INTAKE_RUNNING_THRESHOLD) {
            hasGamePiece = false; // ejecting/scoring
        }
    }

    private void updateMechanism() {
        armLigament.setAngle(Units.radiansToDegrees(armSim.getAngleRads()));
        armLigament.setLength(ARM_LENGTH_METERS + linearArmSim.getHeightMeters(linearHardware));
        intakeLigament.setColor(hasGamePiece
            ? new edu.wpi.first.wpilibj.util.Color8Bit(0, 255, 0)
            : new edu.wpi.first.wpilibj.util.Color8Bit(255, 0, 0));
    }

    public boolean hasGamePiece() {
        return hasGamePiece;
    }

    public double getAngleDegrees() {
        return RobotBase.isSimulation()
            ? Units.radiansToDegrees(armSim.getAngleRads())
            : linearHardware.AngularMotor.getEncoder().getPosition();
    }

    public double getCurrentDrawAmps() {
        return armSim.getCurrentDrawAmps();
    }
}