import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Polynomial {
    double[] coefficients;
    int[] exponents;

    public Polynomial() {
        this.coefficients = new double[]{0.0};
        this.exponents = new int[]{0};
    }

    public Polynomial(double[] new_coefficient, int[] new_exponent) {
        this.coefficients = new_coefficient;
        this.exponents = new_exponent;
    }

    public Polynomial(File f){
        try (Scanner scanner = new Scanner(f)) {
            String polynomial_string = scanner.nextLine();
            String modified_polynomial = polynomial_string.replaceAll("-", "\\+-");
            String[] terms = modified_polynomial.split("\\+");
            int terms_count = terms.length;
            double[] new_coefficient = new double[terms_count];
            int[] exponent_array = new int[terms_count];
            for (int i = 0; i < terms_count; ++i) {
                String current_term = terms[i];
                if(current_term.contains("x")){
                    String[] coefficient_exponent_parts = current_term.split("x");
                    if (coefficient_exponent_parts.length == 2){
                        // Handle coefficient and exponent
                        String coefficient_string = coefficient_exponent_parts[0];
                        if (coefficient_string.isEmpty() || coefficient_string.equals("+")) {
                            new_coefficient[i] = 1.0;
                        } else if (coefficient_string.equals("-")) {
                            new_coefficient[i] = -1.0;
                        } else {
                            new_coefficient[i] = Double.parseDouble(coefficient_string);
                        }
                        
                        String exponent_string = coefficient_exponent_parts[1];
                        if (exponent_string.isEmpty()) {
                            exponent_array[i] = 1;
                        } else {
                            exponent_array[i] = Integer.parseInt(exponent_string);
                        }
                    }
                    else if (coefficient_exponent_parts.length == 1) {
                        // This is just "x" or coefficient+"x" with exponent 1
                        String coefficient_string = coefficient_exponent_parts[0];
                        if (coefficient_string.isEmpty() || coefficient_string.equals("+")) {
                            new_coefficient[i] = 1.0;
                        } else if (coefficient_string.equals("-")) {
                            new_coefficient[i] = -1.0;
                        } else {
                            new_coefficient[i] = Double.parseDouble(coefficient_string);
                        }
                        exponent_array[i] = 1;
                    }
                    else {
                        // This should be just "x" (coefficient is empty)
                        new_coefficient[i] = 1.0;
                        exponent_array[i] = 1;
                    }
                }
                else{
                    // Constant term
                    if (!current_term.isEmpty()) {
                        new_coefficient[i] = Double.parseDouble(current_term);
                        exponent_array[i] = 0;
                    } else {
                        new_coefficient[i] = 0.0;
                        exponent_array[i] = 0;
                    }
                }
            }
            this.coefficients = new_coefficient;
            this.exponents = exponent_array;
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + f.getAbsolutePath());
            throw new RuntimeException("Unable to create polynomial from file: " + f.getName(), e);
        }
    }

    public void saveToFile(String file_name){
        File file = new File(file_name);
        try {
            file.createNewFile();
            String polynomial_string ="";
            for(int i = 0; i < this.coefficients.length - 1; ++i){
                if(exponents[i] == 0){
                    polynomial_string = polynomial_string + coefficients[i] + "+";
                }
                else if (exponents[i] == 1){
                    polynomial_string = polynomial_string + coefficients[i] + "x" + "+";
                }
                else{
                    polynomial_string = polynomial_string + coefficients[i] + "x" + exponents[i] + "+";
                }
            }
            polynomial_string = polynomial_string + coefficients[this.coefficients.length - 1] + "x" + exponents[this.exponents.length - 1];
            polynomial_string = polynomial_string.replaceAll("\\+-", "-");
            
            try (FileWriter file_writer = new FileWriter(file);
                 BufferedWriter buffered_writer = new BufferedWriter(file_writer)) {
                buffered_writer.write(polynomial_string);
            }
        } catch(IOException e){
            System.err.println("Error: Unable to save polynomial to file: " + file_name);
            System.err.println("Reason: " + e.getMessage());
            throw new RuntimeException("Failed to save polynomial to file", e);
        }
    }

    public Polynomial add(Polynomial polynomial_to_add) {
        int this_max_exponent = this.exponents[this.exponents.length - 1];
        int other_max_exponent = polynomial_to_add.exponents[polynomial_to_add.exponents.length - 1];
        int result_array_length = Math.max(this_max_exponent, other_max_exponent) + 1;

        double[] coefficient_sum_array = new double[result_array_length];

        for (int i = 0; i < this.exponents.length; ++i){
            coefficient_sum_array[this.exponents[i]] = this.coefficients[i];
        }

        for (int j = 0; j < polynomial_to_add.exponents.length; ++j){
            coefficient_sum_array[polynomial_to_add.exponents[j]] = coefficient_sum_array[polynomial_to_add.exponents[j]] + polynomial_to_add.coefficients[j];
        }

        int non_zero_count = 0;
        for (int p = 0; p < result_array_length; ++p){
            if (coefficient_sum_array[p] != 0){
                non_zero_count += 1;
            }
        }

        double[] final_coefficients = new double[non_zero_count];
        int[] final_exponents = new int[non_zero_count];
        int index = 0;
        for(int k = 0; k < result_array_length; ++k){
            if(coefficient_sum_array[k] != 0){
                final_coefficients[index] = coefficient_sum_array[k];
                final_exponents[index] = k;
                index += 1;
            }
        }

        Polynomial result_polynomial = new Polynomial(final_coefficients, final_exponents);
        return result_polynomial;
    }

    public double evaluate(double x_value) {
        int coefficient_count = this.coefficients.length;
        double polynomial_result = 0.0d;

        for (int i = 0; i < coefficient_count; ++i){
            polynomial_result = polynomial_result + this.coefficients[i] * (Math.pow(x_value, this.exponents[i]));
        }

        return polynomial_result;
    }

    public boolean hasRoot(double x_value) {
        double evaluation_result = evaluate(x_value);
        return evaluation_result == 0.0d;
    }

    public Polynomial multiply(Polynomial polynomial_to_multiply) {
        int this_max_exponent = this.exponents[this.exponents.length - 1];
        int other_max_exponent = polynomial_to_multiply.exponents[polynomial_to_multiply.exponents.length - 1];
        int result_array_length = this_max_exponent + other_max_exponent + 1;
        double[] product_coefficient_array = new double[result_array_length];

        for (int i = 0; i < this.coefficients.length; ++i){
            double current_coefficient = this.coefficients[i];
            for (int j = 0; j < polynomial_to_multiply.coefficients.length; ++j) {
                product_coefficient_array[this.exponents[i] + polynomial_to_multiply.exponents[j]] = product_coefficient_array[this.exponents[i] + polynomial_to_multiply.exponents[j]] + current_coefficient * polynomial_to_multiply.coefficients[j];
            }
        }

        int non_zero_count = 0;
        for (int p = 0; p < result_array_length; ++p){
            if (product_coefficient_array[p] != 0){
                non_zero_count += 1;
            }
        }

        double[] final_coefficients = new double[non_zero_count];
        int[] final_exponents = new int[non_zero_count];
        int index = 0;
        for(int k = 0; k < result_array_length; ++k){
            if(product_coefficient_array[k] != 0){
                final_coefficients[index] = product_coefficient_array[k];
                final_exponents[index] = k;
                index += 1;
            }
        }
        Polynomial result_polynomial = new Polynomial(final_coefficients, final_exponents);
        return result_polynomial;
    }
}