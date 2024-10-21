public class Main {
    public static float math(float x, int w) {
        switch (w) {
            case 8:
                float exponent1 = (float) Math.pow(0.5 * (Math.atan((x - 4) / 2 * Math.E + 1) - 1), 2);
                float denominator1 = (float) (Math.asin(Math.pow((x - 4) / 2 * Math.E + 1, 2)) + (float) 1 / 3);
                float nominator1 = (float) Math.cbrt(Math.pow(x, x / 2));
                return (float) Math.pow(nominator1 / denominator1, exponent1);
            case 4:
            case 10:
            case 12:
            case 18:
                float exponent2 = x * x / 2;
                float UnderArtan = (float) ((x - 4) / 2 * Math.E + 1);
                return (float) Math.exp(Math.pow(Math.atan(UnderArtan), exponent2));
            default:
                float exponent3 = (float)Math.cos(Math.atan(1 / Math.exp(Math.abs(x))));
                float UnderSinNominator = (float) Math.pow(x + (float) 1/2, 2);
                float UnderSinDenominator = (float)Math.pow(((x - (float) 1/2) / Math.PI), 3) - 1;
                float UnderSin = (float)Math.pow(UnderSinNominator / UnderSinDenominator, 3);
                float denominator3 = (float) Math.atan(Math.sin(UnderSin));
                return (float) Math.pow(2 / denominator3, exponent3);
        }
    }
    public static void output(float[][] z){
        for (var i = 0; i < 8; i++) {
            for (var j = 0; j < 10; j++){
                System.out.printf("%10.2f ", z[i][j]);
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        int[] w = new int [8];

        for (var i = 0; i < 8; i++) {
            w[i] = i * 2 + 4;
        }

        float[] x = new float [10];
        for (var i = 0; i < 10; i++) {
            x[i] = (float) (Math.random() * 20) - 14;
        }

        float[][] z = new float [8][10];
        for (var i = 0; i < 8; i++) {
            for (var j = 0; j < 10; j++){
                z[i][j] = math(x[j], w[i]);
            }
        }
        output(z);
    }
}