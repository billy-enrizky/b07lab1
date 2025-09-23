import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Polynomial {
    double[] coefficients;  // non-zero coefficients
    int[] exponents;       // corresponding exponents

    public Polynomial() {
        this.coefficients = new double[]{0.0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] inputCoefficients) {
        // Convert from old format to new sparse representation
        List<Double> coeffList = new ArrayList<>();
        List<Integer> expList = new ArrayList<>();
        
        for (int i = 0; i < inputCoefficients.length; i++) {
            if (inputCoefficients[i] != 0.0) {
                coeffList.add(inputCoefficients[i]);
                expList.add(i);
            }
        }
        
        if (coeffList.isEmpty()) {
            this.coefficients = new double[]{0.0};
            this.exponents = new int[]{0};
        } else {
            this.coefficients = coeffList.stream().mapToDouble(Double::doubleValue).toArray();
            this.exponents = expList.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    // Constructor with sparse representation
    public Polynomial(double[] coefficients, int[] exponents) {
        if (coefficients.length != exponents.length) {
            throw new IllegalArgumentException("Coefficients and exponents arrays must have the same length");
        }
        
        // Remove zero coefficients and sort by exponent
        Map<Integer, Double> termMap = new TreeMap<>();
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != 0.0) {
                termMap.put(exponents[i], termMap.getOrDefault(exponents[i], 0.0) + coefficients[i]);
            }
        }
        
        // Remove terms that became zero after combining
        termMap.entrySet().removeIf(entry -> entry.getValue() == 0.0);
        
        if (termMap.isEmpty()) {
            this.coefficients = new double[]{0.0};
            this.exponents = new int[]{0};
        } else {
            this.coefficients = termMap.values().stream().mapToDouble(Double::doubleValue).toArray();
            this.exponents = termMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        }
    }

    // Constructor from file
    public Polynomial(File file) throws IOException {
        String polynomialString;
        try (Scanner scanner = new Scanner(file)) {
            polynomialString = scanner.nextLine().trim();
        }
        
        parsePolynomialString(polynomialString);
    }

    private void parsePolynomialString(String polynomialString) {
        // Handle the case of just "0"
        if (polynomialString.equals("0")) {
            this.coefficients = new double[]{0.0};
            this.exponents = new int[]{0};
            return;
        }
        
        Map<Integer, Double> termMap = new TreeMap<>();
        
        // Add a '+' at the beginning if the string doesn't start with '+' or '-'
        if (!polynomialString.startsWith("+") && !polynomialString.startsWith("-")) {
            polynomialString = "+" + polynomialString;
        }
        
        // Pattern to match terms like +5, -3x2, +7x8, etc.
        Pattern pattern = Pattern.compile("([+-])([^+-]*)");
        Matcher matcher = pattern.matcher(polynomialString);
        
        while (matcher.find()) {
            String sign = matcher.group(1);
            String term = matcher.group(2);
            
            double coeff;
            int exp;
            
            if (!term.contains("x")) {
                // Constant term
                coeff = Double.parseDouble(sign + term);
                exp = 0;
            } else {
                String[] parts = term.split("x");
                
                // Coefficient part
                if (parts[0].isEmpty()) {
                    coeff = 1.0;
                } else {
                    coeff = Double.parseDouble(parts[0]);
                }
                
                if (sign.equals("-")) {
                    coeff = -coeff;
                }
                
                // Exponent part
                if (parts.length == 1) {
                    exp = 1; // just "x"
                } else {
                    exp = Integer.parseInt(parts[1]);
                }
            }
            
            termMap.put(exp, termMap.getOrDefault(exp, 0.0) + coeff);
        }
        
        // Remove zero coefficients
        termMap.entrySet().removeIf(entry -> entry.getValue() == 0.0);
        
        if (termMap.isEmpty()) {
            this.coefficients = new double[]{0.0};
            this.exponents = new int[]{0};
        } else {
            this.coefficients = termMap.values().stream().mapToDouble(Double::doubleValue).toArray();
            this.exponents = termMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        }
    }

    public Polynomial add(Polynomial other) {
        Map<Integer, Double> termMap = new TreeMap<>();
        
        // Add terms from this polynomial
        for (int i = 0; i < this.coefficients.length; i++) {
            termMap.put(this.exponents[i], this.coefficients[i]);
        }
        
        // Add terms from other polynomial
        for (int i = 0; i < other.coefficients.length; i++) {
            termMap.put(other.exponents[i], 
                termMap.getOrDefault(other.exponents[i], 0.0) + other.coefficients[i]);
        }
        
        // Remove zero coefficients
        termMap.entrySet().removeIf(entry -> entry.getValue() == 0.0);
        
        if (termMap.isEmpty()) {
            return new Polynomial();
        }
        
        double[] newCoeffs = termMap.values().stream().mapToDouble(Double::doubleValue).toArray();
        int[] newExps = termMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        
        return new Polynomial(newCoeffs, newExps);
    }

    public Polynomial multiply(Polynomial other) {
        Map<Integer, Double> termMap = new TreeMap<>();
        
        // Multiply each term in this polynomial with each term in other polynomial
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                double newCoeff = this.coefficients[i] * other.coefficients[j];
                int newExp = this.exponents[i] + other.exponents[j];
                
                termMap.put(newExp, termMap.getOrDefault(newExp, 0.0) + newCoeff);
            }
        }
        
        // Remove zero coefficients
        termMap.entrySet().removeIf(entry -> entry.getValue() == 0.0);
        
        if (termMap.isEmpty()) {
            return new Polynomial();
        }
        
        double[] newCoeffs = termMap.values().stream().mapToDouble(Double::doubleValue).toArray();
        int[] newExps = termMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        
        return new Polynomial(newCoeffs, newExps);
    }

    public double evaluate(double x) {
        double result = 0.0;

        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }

        return result;
    }

    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-10; // Use epsilon for floating point comparison
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.print(toString());
        }
    }

    @Override
    public String toString() {
        if (coefficients.length == 0 || (coefficients.length == 1 && coefficients[0] == 0.0)) {
            return "0";
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        
        for (int i = 0; i < coefficients.length; i++) {
            double coeff = coefficients[i];
            int exp = exponents[i];
            
            if (coeff == 0.0) continue;
            
            if (!first) {
                if (coeff > 0) {
                    sb.append("+");
                }
            }
            first = false;
            
            switch (exp) {
                case 0 -> sb.append(coeff);
                case 1 -> {
                    switch ((int) Math.signum(coeff)) {
                        case 1 -> {
                            if (coeff == 1.0) {
                                sb.append("x");
                            } else {
                                sb.append(coeff).append("x");
                            }
                        }
                        case -1 -> {
                            if (coeff == -1.0) {
                                sb.append("-x");
                            } else {
                                sb.append(coeff).append("x");
                            }
                        }
                        default -> sb.append(coeff).append("x");
                    }
                }
                default -> {
                    switch ((int) Math.signum(coeff)) {
                        case 1 -> {
                            if (coeff == 1.0) {
                                sb.append("x").append(exp);
                            } else {
                                sb.append(coeff).append("x").append(exp);
                            }
                        }
                        case -1 -> {
                            if (coeff == -1.0) {
                                sb.append("-x").append(exp);
                            } else {
                                sb.append(coeff).append("x").append(exp);
                            }
                        }
                        default -> sb.append(coeff).append("x").append(exp);
                    }
                }
            }
        }
        
        return sb.toString();
    }
}