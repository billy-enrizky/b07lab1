public class Polynomial {
    double[] coefficients;

    public Polynomial() {
        this.coefficients = new double[]{0.0};
    }

    public Polynomial(double[] inputCoefficients) {
        this.coefficients = new double[inputCoefficients.length];
        System.arraycopy(inputCoefficients, 0, this.coefficients, 0, inputCoefficients.length);
    }

    public Polynomial add(Polynomial other) {
        int maxDegree = Math.max(this.coefficients.length, other.coefficients.length);
        double[] newCoefficients = new double[maxDegree];
        
        for (int i = 0; i < maxDegree; i++) {
            double thisCoeff = (i < this.coefficients.length) ? this.coefficients[i] : 0.0;
            double otherCoeff = (i < other.coefficients.length) ? other.coefficients[i] : 0.0;
            newCoefficients[i] = thisCoeff + otherCoeff;
        }
        
        return new Polynomial(newCoefficients);
    }

    public double evaluate(double x) {
        int degree = this.coefficients.length;
        double result = 0.0;

        for (int i = 0; i < degree; i++) {
            result += this.coefficients[i] * Math.pow(x, i);
        }

        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0.0;
    }
}