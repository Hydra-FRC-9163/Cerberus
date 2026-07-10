package frc.robot.subsystems.Sensors.ThroughBore;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.utils.Constants;

public class ThroughBoreHardware extends SubsystemBase {

    public final Encoder leftEncoder;
    public final Encoder rightEncoder;

    public ThroughBoreHardware() {
        leftEncoder  = new Encoder(Constants.Encoder.leftEncoderA, Constants.Encoder.leftEncoderB);
        rightEncoder = new Encoder(Constants.Encoder.rightEncoderA, Constants.Encoder.rightEncoderB);

        double countsPerRev = 2048.0; // quadrature counts
        double gearRatio = 1.0;       // encoder direto na roda
        double wheelCircumference = Math.PI * Constants.Drivetrain.wheelDiameterMeters;
        double distancePerPulse = wheelCircumference / (countsPerRev * gearRatio);

        leftEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setReverseDirection(true);
    }
}