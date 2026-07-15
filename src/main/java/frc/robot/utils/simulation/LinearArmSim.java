package frc.robot.utils.simulation;

import com.revrobotics.sim.SparkMaxSim;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Score.linear.LinearHardware;

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
      
        double vbus = RobotController.getBatteryVoltage();
        double appliedVoltage = linearMotorSim.getAppliedOutput() * vbus;
        elevatorSim.setInputVoltage(appliedVoltage);
        elevatorSim.update(0.02);

        double drumCircumferenceMeters = 2 * Math.PI * DRUM_RADIUS_METERS;
        double motorRPM = (elevatorSim.getVelocityMetersPerSecond() / drumCircumferenceMeters)
                          * GEARING * 60.0;

        linearMotorSim.iterate(motorRPM, vbus, 0.02);

    }

    public double getHeightMeters(LinearHardware linearHardware) {
        return RobotBase.isSimulation()
            ? elevatorSim.getPositionMeters()
            : linearHardware.LinearMotor.getEncoder().getPosition();
    }

    public double getCurrentDrawAmps() {
        return elevatorSim.getCurrentDrawAmps();
    }
}