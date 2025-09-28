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
        this.coefficients = new double[0];
        this.exponents = new int[0];
    }

    public Polynomial(double[] new_coefficient, int[] new_exponent) {
        this.coefficients = new_coefficient.clone();
        this.exponents = new_exponent.clone();
        sortTermsByExponent();
    }

    public Polynomial(File f){
        try (Scanner scanner = new Scanner(f)) {
            String polynomial_string = scanner.nextLine();
            String modified_polynomial = polynomial_string.replaceAll("-", "\\+-");
            String[] all_terms = modified_polynomial.split("\\+");
            
            // Filter out empty terms
            int valid_terms_count = 0;
            for (String term : all_terms) {
                if (!term.trim().isEmpty()) {
                    valid_terms_count++;
                }
            }
            
            double[] new_coefficient = new double[valid_terms_count];
            int[] exponent_array = new int[valid_terms_count];
            int valid_index = 0;
            
            for (String term : all_terms) {
                String current_term = term.trim();
                if (current_term.isEmpty()) continue;
                if(current_term.contains("x")){
                    String[] coefficient_exponent_parts = current_term.split("x");
                    if (coefficient_exponent_parts.length == 2){
                        // Handle coefficient and exponent
                        String coefficient_string = coefficient_exponent_parts[0];
                        if (coefficient_string.isEmpty() || coefficient_string.equals("+")) {
                            new_coefficient[valid_index] = 1.0;
                        } else if (coefficient_string.equals("-")) {
                            new_coefficient[valid_index] = -1.0;
                        } else {
                            new_coefficient[valid_index] = Double.parseDouble(coefficient_string);
                        }
                        
                        String exponent_string = coefficient_exponent_parts[1];
                        if (exponent_string.isEmpty()) {
                            exponent_array[valid_index] = 1;
                        } else {
                            exponent_array[valid_index] = Integer.parseInt(exponent_string);
                        }
                    }
                    else if (coefficient_exponent_parts.length == 1) {
                        // This is just "x" or coefficient+"x" with exponent 1
                        String coefficient_string = coefficient_exponent_parts[0];
                        if (coefficient_string.isEmpty() || coefficient_string.equals("+")) {
                            new_coefficient[valid_index] = 1.0;
                        } else if (coefficient_string.equals("-")) {
                            new_coefficient[valid_index] = -1.0;
                        } else {
                            new_coefficient[valid_index] = Double.parseDouble(coefficient_string);
                        }
                        exponent_array[valid_index] = 1;
                    }
                    else {
                        // This should be just "x" (coefficient is empty)
                        new_coefficient[valid_index] = 1.0;
                        exponent_array[valid_index] = 1;
                    }
                }
                else{
                    // Constant term
                    new_coefficient[valid_index] = Double.parseDouble(current_term);
                    exponent_array[valid_index] = 0;
                }
                valid_index++;
            }
            this.coefficients = new_coefficient;
            this.exponents = exponent_array;
            sortTermsByExponent();
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found - " + f.getAbsolutePath());
            throw new RuntimeException("Unable to create polynomial from file: " + f.getName(), e);
        }
    }

    // Sort terms by exponent in ascending order using bubble sort
    private void sortTermsByExponent() {
        int n = this.exponents.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (this.exponents[j] > this.exponents[j + 1]) {
                    // Swap exponents
                    int tempExp = this.exponents[j];
                    this.exponents[j] = this.exponents[j + 1];
                    this.exponents[j + 1] = tempExp;
                    
                    // Swap corresponding coefficients
                    double tempCoeff = this.coefficients[j];
                    this.coefficients[j] = this.coefficients[j + 1];
                    this.coefficients[j + 1] = tempCoeff;
                }
            }
        }
    }

    public void saveToFile(String file_name){
        File file = new File(file_name);
        try {
            file.createNewFile();
            String polynomial_string ="";
            
            // Handle empty polynomial
            if (this.coefficients.length == 0) {
                polynomial_string = "0";
            } else {
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
                
                // Handle the last term properly
                int lastIndex = this.coefficients.length - 1;
                if(exponents[lastIndex] == 0){
                    polynomial_string = polynomial_string + coefficients[lastIndex];
                }
                else if (exponents[lastIndex] == 1){
                    polynomial_string = polynomial_string + coefficients[lastIndex] + "x";
                }
                else{
                    polynomial_string = polynomial_string + coefficients[lastIndex] + "x" + exponents[lastIndex];
                }
            }
            
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
        // Find the maximum exponent from both polynomials
        int this_max_exponent = 0;
        for (int exp : this.exponents) {
            if (exp > this_max_exponent) this_max_exponent = exp;
        }
        
        int other_max_exponent = 0;
        for (int exp : polynomial_to_add.exponents) {
            if (exp > other_max_exponent) other_max_exponent = exp;
        }
        
        int result_array_length = Math.max(this_max_exponent, other_max_exponent) + 1;
        double[] coefficient_sum_array = new double[result_array_length];

        // Add coefficients from this polynomial
        for (int i = 0; i < this.exponents.length; ++i){
            coefficient_sum_array[this.exponents[i]] += this.coefficients[i];
        }

        // Add coefficients from the other polynomial
        for (int j = 0; j < polynomial_to_add.exponents.length; ++j){
            coefficient_sum_array[polynomial_to_add.exponents[j]] += polynomial_to_add.coefficients[j];
        }

        // Count non-zero terms
        int non_zero_count = 0;
        for (int p = 0; p < result_array_length; ++p){
            if (coefficient_sum_array[p] != 0){
                non_zero_count += 1;
            }
        }

        // Create result arrays with only non-zero terms
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
        // Find the maximum exponent from both polynomials
        int this_max_exponent = 0;
        for (int exp : this.exponents) {
            if (exp > this_max_exponent) this_max_exponent = exp;
        }
        
        int other_max_exponent = 0;
        for (int exp : polynomial_to_multiply.exponents) {
            if (exp > other_max_exponent) other_max_exponent = exp;
        }
        
        int result_array_length = this_max_exponent + other_max_exponent + 1;
        double[] product_coefficient_array = new double[result_array_length];

        // Multiply each term from this polynomial with each term from the other
        for (int i = 0; i < this.coefficients.length; ++i){
            double current_coefficient = this.coefficients[i];
            for (int j = 0; j < polynomial_to_multiply.coefficients.length; ++j) {
                int result_exponent = this.exponents[i] + polynomial_to_multiply.exponents[j];
                product_coefficient_array[result_exponent] += current_coefficient * polynomial_to_multiply.coefficients[j];
            }
        }

        // Count non-zero terms
        int non_zero_count = 0;
        for (int p = 0; p < result_array_length; ++p){
            if (product_coefficient_array[p] != 0){
                non_zero_count += 1;
            }
        }

        // Create result arrays with only non-zero terms
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