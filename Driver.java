import java.io.*;

public class Driver {
    public static void main(String [] args) {
        try {
            // Test original functionality
            System.out.println("=== Testing Original Functionality ===");
            Polynomial p = new Polynomial();
            System.out.println("Default polynomial: " + p.toString());
            System.out.println("p(3) = " + p.evaluate(3));
            
            double [] c1 = {6,0,0,5};
            Polynomial p1 = new Polynomial(c1);
            System.out.println("p1: " + p1.toString());
            
            double [] c2 = {0,-2,0,0,-9};
            Polynomial p2 = new Polynomial(c2);
            System.out.println("p2: " + p2.toString());
            
            Polynomial s = p1.add(p2);
            System.out.println("s = p1 + p2: " + s.toString());
            System.out.println("s(0.1) = " + s.evaluate(0.1));
            
            if(s.hasRoot(1))
                System.out.println("1 is a root of s");
            else
                System.out.println("1 is not a root of s");
            
            // Test new sparse representation constructor
            System.out.println("\n=== Testing New Sparse Representation ===");
            double[] coeffs = {6, -2, 5};
            int[] exps = {0, 1, 3};
            Polynomial p3 = new Polynomial(coeffs, exps);
            System.out.println("p3 (6 - 2x + 5x^3): " + p3.toString());
            System.out.println("p3(2) = " + p3.evaluate(2));
            
            // Test multiplication
            System.out.println("\n=== Testing Multiplication ===");
            double[] coeffs1 = {1, 1}; // 1 + x
            int[] exps1 = {0, 1};
            Polynomial poly1 = new Polynomial(coeffs1, exps1);
            
            double[] coeffs2 = {1, -1}; // 1 - x
            int[] exps2 = {0, 1};
            Polynomial poly2 = new Polynomial(coeffs2, exps2);
            
            System.out.println("poly1 (1 + x): " + poly1.toString());
            System.out.println("poly2 (1 - x): " + poly2.toString());
            
            Polynomial product = poly1.multiply(poly2);
            System.out.println("poly1 * poly2: " + product.toString());
            System.out.println("Expected: 1 - x^2");
            
            // Test file I/O
            System.out.println("\n=== Testing File I/O ===");
            
            try ( // Create a test polynomial file
                    PrintWriter writer = new PrintWriter(new FileWriter("test_polynomial.txt"))) {
                writer.println("5-3x2+7x8");
            }
            
            // Read polynomial from file
            Polynomial fromFile = new Polynomial(new File("test_polynomial.txt"));
            System.out.println("Polynomial from file (5-3x2+7x8): " + fromFile.toString());
            System.out.println("fromFile(1) = " + fromFile.evaluate(1));
            
            // Save polynomial to file
            fromFile.saveToFile("output_polynomial.txt");
            System.out.println("Polynomial saved to output_polynomial.txt");
            
            try ( // Test more complex polynomial string parsing
                    PrintWriter writer2 = new PrintWriter(new FileWriter("complex_polynomial.txt"))) {
                writer2.println("-2x3+4x+10-x5");
            }
            
            Polynomial complex = new Polynomial(new File("complex_polynomial.txt"));
            System.out.println("Complex polynomial (-2x3+4x+10-x5): " + complex.toString());
            
            // Test edge cases
            System.out.println("\n=== Testing Edge Cases ===");
            
            try ( // Zero polynomial
                    PrintWriter writer3 = new PrintWriter(new FileWriter("zero_polynomial.txt"))) {
                writer3.println("0");
            }
            
            Polynomial zero = new Polynomial(new File("zero_polynomial.txt"));
            System.out.println("Zero polynomial: " + zero.toString());
            
            try ( // Single term
                    PrintWriter writer4 = new PrintWriter(new FileWriter("single_term.txt"))) {
                writer4.println("42x7");
            }
            
            Polynomial single = new Polynomial(new File("single_term.txt"));
            System.out.println("Single term (42x7): " + single.toString());
            
        } catch (IOException e) {
            System.err.println("Error with file operations: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}