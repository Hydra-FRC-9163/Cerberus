package frc.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.utils.Constants;

public class ThroughBoreSubsystem extends SubsystemBase {

    public final Encoder leftEncoder;
    public final Encoder rightEncoder;

    public ThroughBoreSubsystem() {
        leftEncoder  = new Encoder(Constants.Encoder.portaEncoderLeftA, Constants.Encoder.portaEncoderLeftB);
        rightEncoder = new Encoder(Constants.Encoder.portaEncoderA, Constants.Encoder.portaEncoderB);

        double countsPerRev = 2048.0; // quadrature counts
        double gearRatio = 1.0;       // encoder direto na roda
        double wheelCircumference = Math.PI * Constants.Drivetrain.wheelDiameterMeters;
        double distancePerPulse = wheelCircumference / (countsPerRev * gearRatio);

        leftEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setReverseDirection(true);
    }

    public double getDistanceMeters() { 
        return rightEncoder.getDistance();
    }

    public double getSpeedMetersPerSecond() {
        return rightEncoder.getRate();
    }

    public double getLeftMetersPerSecond() {
        return leftEncoder.getRate();
    }


    public void reset() {
        leftEncoder.reset();
        rightEncoder.reset();
    }
}