import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
	public static void main(String[] args) {
		System.out.println("=== Enhanced Driver Test ===\n");
		
		// Create an empty polynomial and print its evaluation at x = 3
		Polynomial emptyPolynomial = new Polynomial();
		System.out.println("Empty polynomial at x=3: " + emptyPolynomial.evaluate(3));  // Expect 0.0

		// Build first polynomial: 6 + 5x^3
		double[] coefficients1 = { 6.0, 5.0 };
		int[] exponents1 = { 0, 3 };
		Polynomial poly1 = new Polynomial(coefficients1, exponents1);
		System.out.println("poly1 represents: 6 + 5x^3");

		// Build second polynomial: -2x - 9x^4
		double[] coefficients2 = { -2.0, -9.0 };
		int[] exponents2 = { 1, 4 };
		Polynomial poly2 = new Polynomial(coefficients2, exponents2);
		System.out.println("poly2 represents: -2x - 9x^4");

		// Add poly1 and poly2 → s(x)
		Polynomial sumPoly = poly1.add(poly2);
		System.out.println("\nAddition test:");
		System.out.println("s(x) = poly1 + poly2 = 6 + 5x^3 - 2x - 9x^4");
		System.out.println("s(0.1) = " + sumPoly.evaluate(0.1));

		// Check if x = 1 is a root of s(x)
		if (sumPoly.hasRoot(1.0)) {
			System.out.println("1 is a root of s");
		} else {
			System.out.println("1 is not a root of s");
		}

		// Multiply poly1 by poly2 → t(x)
		Polynomial productPoly = poly1.multiply(poly2);
		System.out.println("\nMultiplication test:");
		System.out.println("t(x) = poly1 * poly2 = (6 + 5x^3) * (-2x - 9x^4)");
		System.out.println("t(1) = " + productPoly.evaluate(1.0));
		System.out.println("t(2) = " + productPoly.evaluate(2.0));
		System.out.println("t(0) = " + productPoly.evaluate(0.0));

		// Save poly1 to a file "poly1.txt", then read it back into a new Polynomial
		String outputFilename = "poly1.txt";
		poly1.saveToFile(outputFilename);
		System.out.println("\nFile I/O test:");
		System.out.println("Saved poly1 to " + outputFilename);

		File savedFile = new File(outputFilename);
		Polynomial loadedPoly = new Polynomial(savedFile);
		System.out.println("Loaded polynomial from file");

		// Verify the loaded polynomial is correct
		System.out.println("Original poly1(2.5) = " + poly1.evaluate(2.5));
		System.out.println("Loaded poly1(2.5) = " + loadedPoly.evaluate(2.5));

		// Add the loaded polynomial to poly2 → s2(x)
		Polynomial sumWithLoaded = loadedPoly.add(poly2);
		System.out.println("s2(0.1) = " + sumWithLoaded.evaluate(0.1));

		// Check if x = 1 is a root of s2(x)
		if (sumWithLoaded.hasRoot(1.0)) {
			System.out.println("1 is a root of s2");
		} else {
			System.out.println("1 is not a root of s2");
		}
		
		// Additional comprehensive tests
		System.out.println("\n=== Additional Tests ===");
		
		// Test reading from existing test files
		testFileReading();
		
		// Test edge cases
		testEdgeCases();
		
		// Test polynomial string formatting
		testStringFormatting();
	}
	
	private static void testFileReading() {
		System.out.println("\nTest reading from existing files:");
		try {
			File testFile = new File("test_polynomial.txt");
			if (testFile.exists()) {
				Polynomial fromFile = new Polynomial(testFile);
				System.out.println("test_polynomial.txt contains: 5-3x2+7x8");
				System.out.println("Parsed polynomial at x=0: " + fromFile.evaluate(0.0)); // Should be 5
				System.out.println("Parsed polynomial at x=1: " + fromFile.evaluate(1.0)); // Should be 5-3+7=9
			}
			
			File complexFile = new File("complex_polynomial.txt");
			if (complexFile.exists()) {
				Polynomial complexPoly = new Polynomial(complexFile);
				System.out.println("complex_polynomial.txt contains: -2x3+4x+10-x5");
				System.out.println("Parsed polynomial at x=0: " + complexPoly.evaluate(0.0)); // Should be 10
				System.out.println("Parsed polynomial at x=1: " + complexPoly.evaluate(1.0)); // Should be -2+4+10-1=11
			}
		} catch (Exception e) {
			System.out.println("Error reading test files: " + e.getMessage());
		}
	}
	
	private static void testEdgeCases() {
		System.out.println("\nEdge case tests:");
		
		// Test single coefficient polynomial
		double[] singleCoeff = {7.0};
		int[] singleExp = {0};
		Polynomial constant = new Polynomial(singleCoeff, singleExp);
		System.out.println("Constant polynomial 7 at x=100: " + constant.evaluate(100.0));
		
		// Test polynomial with coefficient 1 and -1
		double[] unitCoeffs = {1.0, -1.0, 1.0};
		int[] unitExps = {2, 3, 5};
		Polynomial unitPoly = new Polynomial(unitCoeffs, unitExps);
		System.out.println("Polynomial x^2 - x^3 + x^5 at x=2: " + unitPoly.evaluate(2.0));
		
		// Save and test formatting
		unitPoly.saveToFile("unit_test.txt");
	}
	
	private static void testStringFormatting() {
		System.out.println("\nString formatting tests:");
		try {
			// Create test polynomials with different formats
			FileWriter writer = new FileWriter("format_test1.txt");
			writer.write("x+2x2-3x3");
			writer.close();
			
			Polynomial formatTest1 = new Polynomial(new File("format_test1.txt"));
			formatTest1.saveToFile("format_output1.txt");
			System.out.println("Input: x+2x2-3x3");
			System.out.println("Value at x=1: " + formatTest1.evaluate(1.0)); // Should be 1+2-3=0
			
			writer = new FileWriter("format_test2.txt");
			writer.write("-x+5");
			writer.close();
			
			Polynomial formatTest2 = new Polynomial(new File("format_test2.txt"));
			formatTest2.saveToFile("format_output2.txt");
			System.out.println("Input: -x+5");
			System.out.println("Value at x=3: " + formatTest2.evaluate(3.0)); // Should be -3+5=2
			
		} catch (IOException e) {
			System.out.println("Error in formatting tests: " + e.getMessage());
		}
	}
}