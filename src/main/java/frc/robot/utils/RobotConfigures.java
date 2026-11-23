package frc.robot.utils;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotConfigures {
    private static final NetworkTable clawTable =
        NetworkTableInstance.getDefault().getTable("Config/Claw");

    private static final NetworkTable armTable =
        NetworkTableInstance.getDefault().getTable("Config/Arm");

    private static final NetworkTableEntry clawSpeedEntry =
        clawTable.getEntry("Velocidade");

    private static final NetworkTableEntry clawOpenSpeedEntry =
        clawTable.getEntry("Velocidade Aberta");

    private static final NetworkTableEntry armUpSpeedEntry =
        armTable.getEntry("Velocidade Cima");

    private static final NetworkTableEntry armDownSpeedEntry =
        armTable.getEntry("Velocidade Baixo");

    public static double clawSpeed = 0.5;
    public static double clawOpenSpeed = -0.5;
    public static double armUpSpeed = 0.3;
    public static double armDownSpeed = -0.2;

    public static void updateFromNetworkTables() {
        clawSpeed = clawSpeedEntry.getDouble(0.5);
        clawOpenSpeed = clawOpenSpeedEntry.getDouble(-0.5);
        armUpSpeed = armUpSpeedEntry.getDouble(0.3);
        armDownSpeed = armDownSpeedEntry.getDouble(-0.2);
    }

        public void PutDashBoard(){
        SmartDashboard.putNumber("Config/Claw/Velocidade", clawSpeed);
        SmartDashboard.putNumber("Config/Claw/Velocidade Aberta", clawOpenSpeed);
        SmartDashboard.putNumber("Config/Arm/Velocidade Cima", armUpSpeed);
        SmartDashboard.putNumber("Config/Arm/Velocidade Baixo", armDownSpeed);
    }

    
    
    public static void initDashboard() {
        SmartDashboard.setDefaultNumber(
            "Config/Claw/Velocidade", clawSpeed
        );
        SmartDashboard.setDefaultNumber(
            "Config/Claw/Velocidade Aberta", clawOpenSpeed
        );
        SmartDashboard.setDefaultNumber(
            "Config/Arm/Velocidade Cima", armUpSpeed
        );
        SmartDashboard.setDefaultNumber(
            "Config/Arm/Velocidade Baixo", armDownSpeed
        );
    }
}