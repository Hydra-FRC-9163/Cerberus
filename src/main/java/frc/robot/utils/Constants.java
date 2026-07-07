package frc.robot.utils;

public final class Constants {

  public static final int PS5_ID = 0;
  public static final int LOGITECH_ID = 1;

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
        

        public static final int joystickid = 0;
    
}
