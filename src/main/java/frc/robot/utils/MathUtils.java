package frc.robot.utils;

public class MathUtils {

    private static final double ANALOG_DEADZONE = 0.04;
    private boolean analogicL = true;
    private boolean analogicR = false;
    private double deadzone = Constants.Drivetrain.deadzone;

    private double clamp(double value) {
        return Math.max(-1, Math.min(1, value));
    }

    public double[] calcularMagESeno(double eixoX1, double eixoX2, double eixoY1, double eixoY2) {
        double[] MS = new double[4];

        MS[0] = Math.sqrt(eixoX1 * eixoX1 + eixoY1 * eixoY1);
        MS[1] = Math.sqrt(eixoX2 * eixoX2 + eixoY2 * eixoY2);

        MS[0] = clamp(MS[0]);
        MS[1] = clamp(MS[1]);

        MS[2] = (MS[0] != 0) ? eixoY1 / MS[0] : 0;
        MS[3] = (MS[1] != 0) ? eixoY2 / MS[1] : 0;

        return MS;
    }

    public double calcularL2(double gatEsq, double gatDir, double speed) {
        return (gatEsq > deadzone && gatDir <= deadzone) ? gatEsq * speed : 0;
    }

    public double calcularR2(double gatEsq, double gatDir, double speed) {
        return (gatDir > deadzone && gatEsq <= deadzone) ? gatDir * speed : 0;
    }

    public double[] calcularPOV(int direcaoPOV, double speed) {
        double[] velocidades;
        switch (direcaoPOV) {
            case 0 -> velocidades = new double[] {1, 1};
            case 45 -> velocidades = new double[] {1, 0.75};
            case 90 -> velocidades = new double[] {1, -1};
            case 135 -> velocidades = new double[] {-1, 0.75};
            case 180 -> velocidades = new double[] {-1, -1};
            case 225 -> velocidades = new double[] {-0.75, -1};
            case 270 -> velocidades = new double[] {-1, 1};
            case 315 -> velocidades = new double[] {0.75, 1};
            default -> velocidades = new double[] {0, 0};
        }
        velocidades[0] *= speed;
        velocidades[1] *= speed;
        return velocidades;
    }

    public double[] calcularAnalogicos(double[] mSeno, double velocidade, double x1, double y1, double x2, double y2) {
        double[] velocidades = new double[2];
    
        if (mSeno[0] > ANALOG_DEADZONE) {
            analogicL = true;
            analogicR = false;
        } else if (mSeno[1] > ANALOG_DEADZONE) {
            analogicR = true;
            analogicL = false;
        }
    
        java.util.function.DoubleUnaryOperator dz = v -> (Math.abs(v) < ANALOG_DEADZONE) ? 0 : v;
    
        if (analogicL) {
            double left = dz.applyAsDouble(y1 + x1);
            double right = dz.applyAsDouble(y1 - x1);
            velocidades[0] = clamp(left * velocidade);
            velocidades[1] = clamp(right * velocidade);
        } 
        else if (analogicR) {
            double left = dz.applyAsDouble(-y2 + x2);  
            double right = dz.applyAsDouble(-y2 - x2);  
            velocidades[0] = clamp(left * velocidade);
            velocidades[1] = clamp(right * velocidade);
        }
    
        return velocidades;
    }    
}