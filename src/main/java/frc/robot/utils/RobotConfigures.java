package frc.robot.utils;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotConfigures {

    public RobotConfigures(){}
//---------------------CLAW-------------------------
        public static double ClawSpeed = 0.5;
        public static double ClawOpenSpeed = -0.5;

//----------------------ARM-------------------------
        public static double armSpeedUp = 0.2;
        public static double armSpeedDown = -0.2;

 
    
    public static void initDashboard() {
        SmartDashboard.setDefaultNumber(
            "Config/Claw/Velocidade", ClawSpeed
        );
        SmartDashboard.setDefaultNumber(
            "Config/Claw/Velocidade Aberta", ClawOpenSpeed
        );
        SmartDashboard.setDefaultNumber(
            "Config/Arm/Velocidade Cima", armSpeedUp
        );
        SmartDashboard.setDefaultNumber(
            "Config/Arm/Velocidade Baixo", armSpeedDown
        );
    }

      public static void updateFromDashboard() {
        ClawSpeed = clamp(
            SmartDashboard.getNumber(
                "Config/Claw/Velocidade", ClawSpeed
            ),
            -1.0, 1.0
        );
        ClawOpenSpeed = clamp(
            SmartDashboard.getNumber(
                "Config/Claw/Velocidade Aberta", ClawOpenSpeed
            ),
            -1.0, 1.0
        );
        armSpeedUp = clamp(
            SmartDashboard.getNumber(
                "Config/Arm/Velocidade Cima", armSpeedUp
            ),
            -1.0, 1.0
        );
        armSpeedDown = clamp(
            SmartDashboard.getNumber(
                "Config/Arm/Velocidade Baixo", armSpeedDown
            ),
            -1.0, 1.0
        );
    }

    private static double clamp(
        double value, double min, double max
    ) {
        return Math.max(min, Math.min(max, value));
    }
    
    public void PutDashBoard(){
        SmartDashboard.putNumber("Config/Claw/Velocidade", ClawSpeed);
        SmartDashboard.putNumber("Config/Claw/Velocidade Aberta", ClawOpenSpeed);
        SmartDashboard.putNumber("Config/Arm/Velocidade Cima", armSpeedUp);
        SmartDashboard.putNumber("Config/Arm/Velocidade Baixo", armSpeedDown);
    }
}
