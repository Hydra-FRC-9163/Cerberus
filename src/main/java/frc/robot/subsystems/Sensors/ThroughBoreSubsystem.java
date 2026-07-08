package frc.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.utils.Constants;

public class ThroughBoreSubsystem extends SubsystemBase {
    public final Encoder leftEncoder;
    public final Encoder rightEncoder;

    public ThroughBoreSubsystem() {
        leftEncoder = new Encoder(Constants.Encoder.leftEncoderA, Constants.Encoder.leftEncoderB);

        rightEncoder = new Encoder(Constants.Encoder.rightEncoderA, Constants.Encoder.rightEncoderB);



        double wheelDiameterMeters = 0.06; // 6" em metros
        double countsPerRev = 2048.0;        // quadrature counts ou 2048
        double gearRatio = 1.0;              // encoder direto na roda
        double wheelCircumference = Math.PI * wheelDiameterMeters;

        double distancePerPulse = wheelCircumference / (countsPerRev * gearRatio);

        rightEncoder.setDistancePerPulse(distancePerPulse);
        leftEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setReverseDirection(true);
        leftEncoder.setReverseDirection(false);
    }

    public double getRightPerPulse() {
        return rightEncoder.getDistancePerPulse();
    }

    public double getLeftPerPulse() {
        return leftEncoder.getDistancePerPulse();
    }

    public double getRightMeters() {
        return rightEncoder.getDistance();
    }

    public double getLeftMeters() {
        return leftEncoder.getDistance();
    }

    public double getSpeedMetersPerSecond() {
        return rightEncoder.getRate();
    }

    public void reset() {
        rightEncoder.reset();
    }
}