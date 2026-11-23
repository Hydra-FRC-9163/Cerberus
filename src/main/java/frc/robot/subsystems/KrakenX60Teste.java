// package frc.robot.subsystems;

// import com.ctre.phoenix6.configs.Slot0Configs;
// import com.ctre.phoenix6.configs.TalonFXConfiguration;
// import com.ctre.phoenix6.controls.MotionMagicVoltage;
// import com.ctre.phoenix6.controls.PositionVoltage;
// import com.ctre.phoenix6.controls.VelocityVoltage;
// import com.ctre.phoenix6.hardware.TalonFX;

// import edu.wpi.first.units.measure.Velocity;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// public class KrakenX60Teste extends SubsystemBase {

//   private final TalonFX krakenX60 = new TalonFX(5);

//   private final PositionVoltage PvKrakenx60 = new PositionVoltage(0).withSlot(1);

//   private final MotionMagicVoltage MmKrakenx60 = new MotionMagicVoltage(0).withSlot(1);

//   private final VelocityVoltage VvKrakenx60 = new VelocityVoltage(0).withSlot(0);


//   public KrakenX60Teste() {
//    var config = new TalonFXConfiguration();

//   //Configs Velocity
//            // Configs de PID //
//    config.Slot0.kP = 0.3;
//    config.Slot0.kI = 0.0;
//    config.Slot0.kD = 0.0;
//            // Configs de Feedforward //
//    config.Slot0.kV = 0.0;
//    config.Slot0.kA = 0.0;
//    config.Slot0.kS = 0.0;

//    //Configs Position 
//            // Configs de PID //
//    config.Slot1.kP = 0.6;
//    config.Slot1.kI = 0.0;
//    config.Slot1.kD = 0.3;
//             // Configs de Feedforward //
//    config.Slot1.kV = 0.0;
//    config.Slot1.kA = 0.0;
//    config.Slot1.kS = 0.0;

//            // Configs de Motion Magic //
//    config.MotionMagic.MotionMagicAcceleration = 20;
//    config.MotionMagic.MotionMagicCruiseVelocity = 10;
//    config.MotionMagic.MotionMagicJerk = 100;
//            // Configs de corrente //
//    config.CurrentLimits.StatorCurrentLimit = 120;
//    config.CurrentLimits.StatorCurrentLimitEnable = true;

//    krakenX60.getConfigurator().apply(config);
//    var status = krakenX60.getConfigurator().apply(config);
//    System.out.println("Config Funcionando?" + status);

//    krakenX60.setPosition(0);

//   }

//   public void setSpeeds(double Speed) {
//     krakenX60.set(Speed);
//   }

//   public double getPosition() {
//     return krakenX60.getPosition().getValueAsDouble();
//   }

//   public double getVelocity() {
//     return krakenX60.getVelocity().getValueAsDouble();
//   }

//   public double getCurrent() {
//     return krakenX60.getSupplyCurrent().getValueAsDouble();
//   }

//   public void setRpm(double rpm) {
//     double rps = rpm / 60.0; // Converter RPM para RPS :D
//     krakenX60.setControl(VvKrakenx60.withVelocity(rps));
//      System.out.println("setRpm chamado: " + rpm + " RPM");
//   }

//   public void setMotionMagic(double position) {
//     krakenX60.setControl(MmKrakenx60.withPosition(position));
//     System.out.println("setMotionMagic chamado: " + position);
//   }

//   public void setPosition(double position) {
//     krakenX60.setControl(PvKrakenx60.withPosition(position));
//     System.out.println("setPosition chamado: " + position);
//   }

//   @Override
//   public void periodic() {
//    SmartDashboard.putNumber("Velocity", getVelocity());
//    SmartDashboard.putNumber("Position", getPosition());
//    SmartDashboard.putNumber("Current", getCurrent());
//   }
// }
