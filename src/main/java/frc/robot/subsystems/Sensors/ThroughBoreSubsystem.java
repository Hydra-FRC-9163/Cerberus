package frc.robot.subsystems.Sensors;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.utils.Constants;

public class ThroughBoreSubsystem extends SubsystemBase {
    private final Encoder rightEncoder;

    public ThroughBoreSubsystem() {
        rightEncoder = new Encoder(Constants.Encoder.portaEncoderA, Constants.Encoder.portaEncoderB);

        double wheelDiameterMeters = 0.1524; // 6" em metros
        double countsPerRev = 2048.0;        // quadrature counts ou 2048
        double gearRatio = 1.0;              // encoder direto na roda
        double wheelCircumference = Math.PI * wheelDiameterMeters;

        double distancePerPulse = wheelCircumference / (countsPerRev * gearRatio);

        rightEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setReverseDirection(true); // inverter se precisar
    }

    public double getDistanceMeters() {
        return rightEncoder.getDistance();
    }

    public double getSpeedMetersPerSecond() {
        return rightEncoder.getRate();
    }

    public void reset() {
        rightEncoder.reset();
    }
}