package frc.robot.utils;

public final class Constants {

  public static final int PS5_ID = 0;
  public static final int LOGITECH_ID = 1;
  public static final int joystickid = 0;

    public static class Drivetrain {
        public static final int frenteleft = 1;
        public static final int trasleft = 2;
        public static final int frenteright = 3;
        public static final int trasright = 4;

        public static final boolean leftinvertido = false;
        public static final boolean rightinvertido = true;

        public static final int eixoX = 0;
        public static final int eixoY = 1;
        public static final int eixoX2 = 4;
        public static final int eixoY2 = 5;

        public static final int R1 = 6;
        public static final int L1 = 5;

        public static final int R2 = 3;
        public static final int L2 = 2;

        public static final int botaoA = 1;  // 100% (Quadrado ou A)
        public static final int botaoB = 2;  // 25% (X ou B)
        public static final int botaoX = 3;  // 50% (Circulo ou X)

        public static final double deadzone = 0.04;

        public static final double wheelDiameterMeters = 0.06; 
    }

    public static class ClimbConstants {

        public static final double CLIMBER_kP = 0.02;
        public static final double CLIMBER_kI = 0.0;
        public static final double CLIMBER_kD = 0.0;

        public static final double CLIMBER_MAX_OUTPUT = 1;
        public static final double CLIMBER_MIN_OUTPUT = -1;
    
        public static final int CLIMBER_LEFT_ID = 14;
        public static final int CLIMBER_RIGHT_ID = 15;
        public static final double CLIMBER_TOLERANCE = 0.7;
   
        public static final String PREF_MIN_KEY = "Climber min position";
        public static final String PREF_MAX_KEY = "Climber max position";
    }

    public static final class Encoder {
        public static final int portaEncoderA = 0;
        public static final int portaEncoderB = 1;

        // CRIADO PARA O SIMULADOR
        // ADAPTAR PARA IGUALAR COM PORTA REAL
        public static final int portaEncoderLeftA = 2;
        public static final int portaEncoderLeftB = 3;
    }

    public static final class LimeLight {
        public static final String limeLightFront = "limelight-front";
        public static final String limeLightLeft = "limelight-left";
  
          public static final double kP_Distance = 0.04;
          public static final double kP_Aim = 0.02;
          public static final double targetArea = 4.0;
          public static final double maxSpeed = 0.25;
          public static final double minSpeed = 0.05;
          public static final double deadbandDistance = 0.3;
          public static final double deadbandAim = 10.0;
          public static final double minTurnCommand = 0.02;
          public static final double initialSpeed = 0.3;
  
          public static final double limeLightHeight = 0.60;
          public static final double tagHeight = 1.22;
          public static final double limeLightAngle = 25.0;
  
          public static final double kP_align = 0.02;
          public static final double maxTurnPower = 0.3;
          public static final double alignDeadband = 10;
    }

    public static class ADLManager {
        public static final double MIN_DECISION_INTERVAL = 0.1; 
        public static final boolean USE_MODULAR_ADL = true;
        public static final boolean RUN_LEGACY_ADL_IN_PARALLEL = false;
  }
}
