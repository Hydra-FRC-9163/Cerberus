package frc.robot.utils.simulation;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Score.linear.LinearHardware;

/**
 * Physics simulation for the linear (extension) stage of the scoring mechanism.
 * Wraps LinearHardware.LinearMotor with a SparkMaxSim driven by an ElevatorSim,
 * so LinearManager.LinearGoToPosition() drives against real elevator physics
 * (gravity, mass, current draw) instead of just an abstract number in sim.
 *
 * TUNE THESE for your actual mechanism - these are placeholders:
 *  - GEARING: total reduction between the NEO and the drum/pulley
 *  - DRUM_RADIUS_METERS: radius of whatever spool/pulley drives the carriage
 *  - CARRIAGE_MASS_KG: moving mass of the extending stage
 *  - MIN/MAX_HEIGHT_METERS: physical soft limits of travel
 */
public class LinearArmSim extends SubsystemBase {

    private static final double GEARING = 12.0;
    private static final double DRUM_RADIUS_METERS = 0.02;
    private static final double CARRIAGE_MASS_KG = 4.0;
    private static final double MIN_HEIGHT_METERS = 0.0;
    private static final double MAX_HEIGHT_METERS = 0.9;
    private static final double STARTING_HEIGHT_METERS = 0.0;
    private static final boolean SIMULATE_GRAVITY = true;

    private final SparkMaxSim linearMotorSim;
    private final ElevatorSim elevatorSim;

    public LinearArmSim(LinearHardware linearHardware) {
        DCMotor gearbox = DCMotor.getNEO(1);

        linearMotorSim = new SparkMaxSim(linearHardware.LinearMotor, gearbox);

        elevatorSim = new ElevatorSim(
            gearbox,
            GEARING,
            CARRIAGE_MASS_KG,
            DRUM_RADIUS_METERS,
            MIN_HEIGHT_METERS,
            MAX_HEIGHT_METERS,
            SIMULATE_GRAVITY,
            STARTING_HEIGHT_METERS
        );
    }

    @Override
    public void simulationPeriodic() {
        // 1. Read what the real SparkMax is commanding (duty cycle -1..1),
        //    convert to volts, and feed the physics model.
        double vbus = RobotController.getBatteryVoltage();
        double appliedVoltage = linearMotorSim.getAppliedOutput() * vbus;
        elevatorSim.setInputVoltage(appliedVoltage);
        elevatorSim.update(0.02);

        // 2. Convert the elevator's linear velocity back into motor RPM
        //    (through the drum radius and gearing) so the SparkMax's internal
        //    encoder reports values consistent with the real gearbox ratio.
        double drumCircumferenceMeters = 2 * Math.PI * DRUM_RADIUS_METERS;
        double motorRPM = (elevatorSim.getVelocityMetersPerSecond() / drumCircumferenceMeters)
                          * GEARING * 60.0;

        // 3. Push the calculated velocity back into the SparkMax sim. iterate()
        //    updates the sim's position/velocity internally (used for the encoder
        //    AND for closed-loop control, since it re-runs the real firmware's
        //    control loop against this simulated feedback).
        linearMotorSim.iterate(motorRPM, vbus, 0.02);
    }

    /** Height in meters, from real hardware on the robot, or from physics in sim. */
    public double getHeightMeters(LinearHardware linearHardware) {
        return RobotBase.isSimulation()
            ? elevatorSim.getPositionMeters()
            : linearHardware.LinearMotor.getEncoder().getPosition();
    }

    public double getCurrentDrawAmps() {
        return elevatorSim.getCurrentDrawAmps();
    }
}