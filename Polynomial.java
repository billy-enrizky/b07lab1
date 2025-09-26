import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Polynomial {
    double[] co;
    int[] co_i;

    public Polynomial() {
        this.co = new double[1];
        this.co_i = new int[1];
        this.co[0] = 0.0;
        this.co_i[0] = 0;
    }

    public Polynomial(double[] coe, int[] coe_i) {
        this.co = coe;
        this.co_i = coe_i;
    }

    public Polynomial(File f){
        try (Scanner scanner = new Scanner(f)) {
            String pol = scanner.nextLine();
            parsePolynomialString(pol);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            // Initialize as zero polynomial
            this.co = new double[1];
            this.co_i = new int[1];
            this.co[0] = 0.0;
            this.co_i[0] = 0;
        }
    }
    
    private void parsePolynomialString(String pol) {
        // Handle negative signs by adding + before them
        String modPol = pol.replaceAll("-", "+-");
        // Remove leading + if it exists
        if (modPol.startsWith("+")) {
            modPol = modPol.substring(1);
        }
        String[] terms = modPol.split("\\+");
        
        // Filter out empty terms
        java.util.List<String> nonEmptyTerms = new java.util.ArrayList<>();
        for (String term : terms) {
            if (!term.trim().isEmpty()) {
                nonEmptyTerms.add(term.trim());
            }
        }
        
        int termNum = nonEmptyTerms.size();
        double[] coe = new double[termNum];
        int[] fr = new int[termNum];
        
        for (int i = 0; i < termNum; i++) {
            String ter = nonEmptyTerms.get(i);
            if (ter.contains("x")) {
                String[] cf = ter.split("x");
                switch (cf.length) {
                    case 2 -> {
                        // Case: coefficient + x + exponent
                        String coeffStr = cf[0];
                        coe[i] = switch (coeffStr) {
                            case "", "+" -> 1.0;
                            case "-" -> -1.0;
                            default -> Double.parseDouble(coeffStr);
                        };
                        fr[i] = Integer.parseInt(cf[1]);
                    }
                    case 1 -> {
                        // Case: coefficient + x (exponent = 1)
                        String coeffStr2 = cf[0];
                        coe[i] = switch (coeffStr2) {
                            case "", "+" -> 1.0;
                            case "-" -> -1.0;
                            default -> Double.parseDouble(coeffStr2);
                        };
                        fr[i] = 1;
                    }
                    default -> {
                        // Case: just x (coefficient = 1, exponent = 1)
                        coe[i] = 1.0;
                        fr[i] = 1;
                    }
                }
            } else {
                // Constant term
                coe[i] = Double.parseDouble(ter);
                fr[i] = 0;
            }
        }
        this.co = coe;
        this.co_i = fr;
    }

    public void saveToFile(String fn){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fn))) {
            String pol = buildPolynomialString();
            writer.write(pol);
        } catch(IOException e){
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    
    private String buildPolynomialString() {
        if (co.length == 0) {
            return "0";
        }
        
        StringBuilder pol = new StringBuilder();
        
        for (int i = 0; i < co.length; i++) {
            double coeff = co[i];
            int exp = co_i[i];
            
            // Skip zero coefficients
            if (coeff == 0) {
                continue;
            }
            
            // Add + for positive coefficients (except the first term)
            if (i > 0 && coeff > 0) {
                pol.append("+");
            }
            
            // Handle coefficient
            if (exp == 0) {
                // Constant term
                pol.append(coeff);
            } else if (coeff == 1.0) {
                // Coefficient is 1, don't show it unless it's a constant
                if (exp == 1) {
                    pol.append("x");
                } else {
                    pol.append("x").append(exp);
                }
            } else if (coeff == -1.0) {
                // Coefficient is -1, show only the minus sign
                if (exp == 1) {
                    pol.append("-x");
                } else {
                    pol.append("-x").append(exp);
                }
            } else {
                // General case
                pol.append(coeff);
                if (exp == 1) {
                    pol.append("x");
                } else {
                    pol.append("x").append(exp);
                }
            }
        }
        
        return pol.length() == 0 ? "0" : pol.toString();
    }

    public Polynomial add(Polynomial a) {
        int thisLen = this.co_i[this.co_i.length - 1];
        int aLen = a.co_i[a.co_i.length - 1];
        int newLen = Math.max(thisLen, aLen) + 1;

        double[] add_co = new double[newLen];

        for (int i = 0; i < this.co_i.length; ++i){
            add_co[this.co_i[i]] = this.co[i];
        }

        for (int j = 0; j < a.co_i.length; ++j){
            add_co[a.co_i[j]] = add_co[a.co_i[j]] + a.co[j];
        }

        int no_zero = 0;
        for (int p = 0; p < newLen; ++p){
            if (add_co[p] != 0){
                no_zero += 1;
            }
        }

        double[] true_add_co = new double[no_zero];
        int[] true_add_co_i = new int[no_zero];
        int t = 0;
        for(int k = 0; k < newLen; ++k){
            if(add_co[k] != 0){
                true_add_co[t] = add_co[k];
                true_add_co_i[t] = k;
                t += 1;
            }
        }

        Polynomial newp = new Polynomial(true_add_co, true_add_co_i);
        return newp;
    }

    public double evaluate(double x) {
        int len = this.co.length;
        double result = 0.0d;

        for (int i = 0; i < len; ++i){
            result = result + this.co[i] * (Math.pow(x, this.co_i[i]));
        }

        return result;
    }

    public boolean hasRoot(double x) {
        double e = evaluate(x);
        return e == 0.0d;
    }

    public Polynomial multiply(Polynomial jyf) {
        int thisLen = this.co_i[this.co_i.length - 1];
        int jLen = jyf.co_i[jyf.co_i.length - 1];
        int newLen = thisLen + jLen + 1;
        double[] new_mul_co = new double[newLen];

        for (int i = 0; i < this.co.length; ++i){
            double ic = this.co[i];
            for (int j = 0; j < jyf.co.length; ++j) {
                new_mul_co[this.co_i[i] + jyf.co_i[j]] = new_mul_co[this.co_i[i] + jyf.co_i[j]] + ic * jyf.co[j];
            }
        }

        int no_zero = 0;
        for (int p = 0; p < newLen; ++p){
            if (new_mul_co[p] != 0){
                no_zero += 1;
            }
        }

        double[] true_mul_co = new double[no_zero];
        int[] true_mul_co_i = new int[no_zero];
        int t = 0;
        for(int k = 0; k < newLen; ++k){
            if(new_mul_co[k] != 0){
                true_mul_co[t] = new_mul_co[k];
                true_mul_co_i[t] = k;
                t += 1;
            }
        }
        Polynomial nmp = new Polynomial(true_mul_co, true_mul_co_i);
        return nmp;
    }
}