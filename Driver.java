import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) {
        System.out.println("=== COMPREHENSIVE POLYNOMIAL TESTING ===\n");
        
        int tests_passed = 0;
        int total_tests = 0;
        
        // Test 1: Empty polynomial
        total_tests++;
        try {
            Polynomial empty_poly = new Polynomial();
            double result = empty_poly.evaluate(3.0);
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 1 PASSED: Empty polynomial evaluates to 0");
                tests_passed++;
            } else {
                System.out.println("✗ Test 1 FAILED: Expected 0, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 1 FAILED: Exception - " + e.getMessage());
        }

        // Test 2: Constructor with arrays
        total_tests++;
        try {
            double[] coeffs = {6.0, 5.0};
            int[] exps = {0, 3};
            Polynomial poly_1 = new Polynomial(coeffs, exps);
            double result = poly_1.evaluate(2.0); // 6 + 5*8 = 46
            if (Math.abs(result - 46.0) < 1e-10) {
                System.out.println("✓ Test 2 PASSED: Array constructor works correctly");
                tests_passed++;
            } else {
                System.out.println("✗ Test 2 FAILED: Expected 46, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 2 FAILED: Exception - " + e.getMessage());
        }

        // Test file parsing edge cases
        int file_tests = testFileParsing();
        total_tests += 8;
        tests_passed += file_tests;

        // Test addition edge cases
        int add_tests = testAddition();
        total_tests += 5;
        tests_passed += add_tests;

        // Test multiplication edge cases
        int mul_tests = testMultiplication();
        total_tests += 5;
        tests_passed += mul_tests;

        // Test root finding
        int root_tests = testRootFinding();
        total_tests += 5;
        tests_passed += root_tests;

        // Test save/load file operations
        int file_op_tests = testFileOperations();
        total_tests += 5;
        tests_passed += file_op_tests;

        // Test edge cases with zero coefficients
        int zero_tests = testZeroCoefficients();
        total_tests += 5;
        tests_passed += zero_tests;

        // Test large numbers and precision
        int precision_tests = testPrecision();
        total_tests += 5;
        tests_passed += precision_tests;

        // Additional extreme edge case tests
        int extreme_tests = testExtremeEdgeCases();
        total_tests += 10;
        tests_passed += extreme_tests;

        System.out.println("\n=== FINAL RESULTS ===");
        System.out.println("Tests passed: " + tests_passed + "/" + total_tests);
        System.out.println("Success rate: " + String.format("%.1f", 100.0 * tests_passed / total_tests) + "%");
        
        if (tests_passed == total_tests) {
            System.out.println("🎉 ALL TESTS PASSED! The implementation is robust.");
        } else {
            System.out.println("⚠️  Some tests failed. Please review the implementation.");
        }
    }

    private static int testFileParsing() {
        System.out.println("\n=== FILE PARSING TESTS ===");
        int passed = 0;
        
        // Test 3: Basic polynomial from file
        try {
            FileWriter writer = new FileWriter("test1.txt");
            writer.write("5-3x2+7x8");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test1.txt"));
            double result = poly.evaluate(0.0); // Should be 5
            if (Math.abs(result - 5.0) < 1e-10) {
                System.out.println("✓ Test 3 PASSED: Basic polynomial parsing");
                passed++;
            } else {
                System.out.println("✗ Test 3 FAILED: Expected 5, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 3 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 4: Polynomial with just x
        try {
            FileWriter writer = new FileWriter("test2.txt");
            writer.write("x");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test2.txt"));
            double result = poly.evaluate(5.0); // Should be 5
            if (Math.abs(result - 5.0) < 1e-10) {
                System.out.println("✓ Test 4 PASSED: Single x parsing");
                passed++;
            } else {
                System.out.println("✗ Test 4 FAILED: Expected 5, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 4 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 5: Polynomial with -x
        try {
            FileWriter writer = new FileWriter("test3.txt");
            writer.write("-x");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test3.txt"));
            double result = poly.evaluate(3.0); // Should be -3
            if (Math.abs(result - (-3.0)) < 1e-10) {
                System.out.println("✓ Test 5 PASSED: Negative x parsing");
                passed++;
            } else {
                System.out.println("✗ Test 5 FAILED: Expected -3, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 5 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 6: Constant polynomial
        try {
            FileWriter writer = new FileWriter("test4.txt");
            writer.write("42");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test4.txt"));
            double result = poly.evaluate(100.0); // Should be 42
            if (Math.abs(result - 42.0) < 1e-10) {
                System.out.println("✓ Test 6 PASSED: Constant polynomial parsing");
                passed++;
            } else {
                System.out.println("✗ Test 6 FAILED: Expected 42, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 6 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 7: Polynomial with decimal coefficients
        try {
            FileWriter writer = new FileWriter("test5.txt");
            writer.write("1.5x2-2.3x+4.7");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test5.txt"));
            double result = poly.evaluate(1.0); // 1.5 - 2.3 + 4.7 = 3.9
            if (Math.abs(result - 3.9) < 1e-10) {
                System.out.println("✓ Test 7 PASSED: Decimal coefficients parsing");
                passed++;
            } else {
                System.out.println("✗ Test 7 FAILED: Expected 3.9, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 7 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 8: Polynomial starting with positive term (no sign)
        try {
            FileWriter writer = new FileWriter("test6.txt");
            writer.write("3x3+2x-1");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test6.txt"));
            double result = poly.evaluate(1.0); // 3 + 2 - 1 = 4
            if (Math.abs(result - 4.0) < 1e-10) {
                System.out.println("✓ Test 8 PASSED: Positive leading term parsing");
                passed++;
            } else {
                System.out.println("✗ Test 8 FAILED: Expected 4, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 8 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 9: Polynomial with large exponents
        try {
            FileWriter writer = new FileWriter("test7.txt");
            writer.write("x10-x5+1");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test7.txt"));
            double result = poly.evaluate(1.0); // 1 - 1 + 1 = 1
            if (Math.abs(result - 1.0) < 1e-10) {
                System.out.println("✓ Test 9 PASSED: Large exponents parsing");
                passed++;
            } else {
                System.out.println("✗ Test 9 FAILED: Expected 1, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 9 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 10: Zero polynomial
        try {
            FileWriter writer = new FileWriter("test8.txt");
            writer.write("0");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("test8.txt"));
            double result = poly.evaluate(5.0); // Should be 0
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 10 PASSED: Zero polynomial parsing");
                passed++;
            } else {
                System.out.println("✗ Test 10 FAILED: Expected 0, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 10 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
    
    private static int testAddition() {
        System.out.println("\n=== ADDITION TESTS ===");
        int passed = 0;
        
        // Test 11: Basic addition
        try {
            double[] c_1 = {6.0, 5.0}, c_2 = {-2.0, -9.0};
            int[] e_1 = {0, 3}, e_2 = {1, 4};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial sum = p_1.add(p_2);
            double result = sum.evaluate(1.0); // 6 + 5 - 2 - 9 = 0
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 11 PASSED: Basic addition");
                passed++;
            } else {
                System.out.println("✗ Test 11 FAILED: Expected 0, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 11 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 12: Addition with overlapping terms
        try {
            double[] c_1 = {3.0, 2.0}, c_2 = {1.0, -1.0};
            int[] e_1 = {1, 2}, e_2 = {1, 2};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial sum = p_1.add(p_2);
            double result = sum.evaluate(2.0); // 4*2 + 1*4 = 12
            if (Math.abs(result - 12.0) < 1e-10) {
                System.out.println("✓ Test 12 PASSED: Addition with overlapping terms");
                passed++;
            } else {
                System.out.println("✗ Test 12 FAILED: Expected 12, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 12 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 13: Adding to empty polynomial
        try {
            Polynomial empty = new Polynomial();
            double[] c = {5.0};
            int[] e = {2};
            Polynomial p = new Polynomial(c, e);
            Polynomial sum = empty.add(p);
            double result = sum.evaluate(3.0); // 5*9 = 45
            if (Math.abs(result - 45.0) < 1e-10) {
                System.out.println("✓ Test 13 PASSED: Adding to empty polynomial");
                passed++;
            } else {
                System.out.println("✗ Test 13 FAILED: Expected 45, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 13 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 14: Addition resulting in zero terms
        try {
            double[] c_1 = {5.0}, c_2 = {-5.0};
            int[] e_1 = {2}, e_2 = {2};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial sum = p_1.add(p_2);
            double result = sum.evaluate(10.0); // Should be 0
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 14 PASSED: Addition resulting in zero");
                passed++;
            } else {
                System.out.println("✗ Test 14 FAILED: Expected 0, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 14 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 15: Addition with different degree polynomials
        try {
            double[] c_1 = {1.0}, c_2 = {1.0};
            int[] e_1 = {0}, e_2 = {10};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial sum = p_1.add(p_2);
            double result = sum.evaluate(2.0); // 1 + 1024 = 1025
            if (Math.abs(result - 1025.0) < 1e-10) {
                System.out.println("✓ Test 15 PASSED: Adding different degree polynomials");
                passed++;
            } else {
                System.out.println("✗ Test 15 FAILED: Expected 1025, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 15 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
    
    private static int testMultiplication() {
        System.out.println("\n=== MULTIPLICATION TESTS ===");
        int passed = 0;
        
        // Test 16: Basic multiplication
        try {
            double[] c_1 = {2.0}, c_2 = {3.0};
            int[] e_1 = {1}, e_2 = {1};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial product = p_1.multiply(p_2);
            double result = product.evaluate(2.0); // 6*4 = 24
            if (Math.abs(result - 24.0) < 1e-10) {
                System.out.println("✓ Test 16 PASSED: Basic multiplication");
                passed++;
            } else {
                System.out.println("✗ Test 16 FAILED: Expected 24, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 16 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 17: Multiplication by constant
        try {
            double[] c_1 = {5.0}, c_2 = {1.0, 2.0};
            int[] e_1 = {0}, e_2 = {1, 3};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial product = p_1.multiply(p_2);
            double result = product.evaluate(2.0); // 5*(2 + 16) = 90
            if (Math.abs(result - 90.0) < 1e-10) {
                System.out.println("✓ Test 17 PASSED: Multiplication by constant");
                passed++;
            } else {
                System.out.println("✗ Test 17 FAILED: Expected 90, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 17 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 18: Multiplication with zero
        try {
            double[] c_1 = {0.0}, c_2 = {5.0};
            int[] e_1 = {0}, e_2 = {3};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial product = p_1.multiply(p_2);
            double result = product.evaluate(10.0); // Should be 0
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 18 PASSED: Multiplication with zero");
                passed++;
            } else {
                System.out.println("✗ Test 18 FAILED: Expected 0, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 18 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 19: Complex multiplication
        try {
            double[] c_1 = {1.0, 1.0}, c_2 = {1.0, -1.0};
            int[] e_1 = {0, 1}, e_2 = {0, 1};
            Polynomial p_1 = new Polynomial(c_1, e_1); // 1 + x
            Polynomial p_2 = new Polynomial(c_2, e_2); // 1 - x
            Polynomial product = p_1.multiply(p_2); // 1 - x^2
            double result = product.evaluate(3.0); // 1 - 9 = -8
            if (Math.abs(result - (-8.0)) < 1e-10) {
                System.out.println("✓ Test 19 PASSED: Complex multiplication (1+x)(1-x)");
                passed++;
            } else {
                System.out.println("✗ Test 19 FAILED: Expected -8, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 19 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 20: Multiplication resulting in higher degree
        try {
            double[] c_1 = {1.0}, c_2 = {1.0};
            int[] e_1 = {5}, e_2 = {7};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial product = p_1.multiply(p_2);
            double result = product.evaluate(2.0); // 2^12 = 4096
            if (Math.abs(result - 4096.0) < 1e-10) {
                System.out.println("✓ Test 20 PASSED: High degree multiplication");
                passed++;
            } else {
                System.out.println("✗ Test 20 FAILED: Expected 4096, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 20 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
    
    private static int testRootFinding() {
        System.out.println("\n=== ROOT FINDING TESTS ===");
        int passed = 0;
        
        // Test 21: Simple root
        try {
            double[] c = {0.0, 1.0};
            int[] e = {0, 1};
            Polynomial p = new Polynomial(c, e); // x
            if (p.hasRoot(0.0)) {
                System.out.println("✓ Test 21 PASSED: Simple root detection");
                passed++;
            } else {
                System.out.println("✗ Test 21 FAILED: Should detect x=0 as root of x");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 21 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 22: No root
        try {
            double[] c = {1.0};
            int[] e = {0};
            Polynomial p = new Polynomial(c, e); // 1
            if (!p.hasRoot(5.0)) {
                System.out.println("✓ Test 22 PASSED: Non-root detection");
                passed++;
            } else {
                System.out.println("✗ Test 22 FAILED: Should not detect root in constant polynomial");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 22 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 23: Quadratic root
        try {
            double[] c = {-4.0, 0.0, 1.0};
            int[] e = {0, 1, 2};
            Polynomial p = new Polynomial(c, e); // x^2 - 4
            if (p.hasRoot(2.0) && p.hasRoot(-2.0)) {
                System.out.println("✓ Test 23 PASSED: Quadratic roots detection");
                passed++;
            } else {
                System.out.println("✗ Test 23 FAILED: Should detect ±2 as roots of x²-4");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 23 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 24: Multiple roots
        try {
            double[] c = {0.0, 0.0, 1.0};
            int[] e = {0, 1, 2};
            Polynomial p = new Polynomial(c, e); // x^2
            if (p.hasRoot(0.0)) {
                System.out.println("✓ Test 24 PASSED: Multiple root detection");
                passed++;
            } else {
                System.out.println("✗ Test 24 FAILED: Should detect x=0 as root of x²");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 24 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 25: Precision in root finding
        try {
            double[] c = {-1e-15, 1.0};
            int[] e = {0, 1};
            Polynomial p = new Polynomial(c, e); // x - 1e-15
            if (p.hasRoot(1e-15)) {
                System.out.println("✓ Test 25 PASSED: Precision root detection");
                passed++;
            } else {
                System.out.println("✗ Test 25 FAILED: Should detect very small root");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 25 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
    
    private static int testFileOperations() {
        System.out.println("\n=== FILE OPERATIONS TESTS ===");
        int passed = 0;
        
        // Test 26: Save and load basic polynomial
        try {
            double[] c = {1.0, 2.0, 3.0};
            int[] e = {0, 1, 2};
            Polynomial original = new Polynomial(c, e);
            original.saveToFile("save_test1.txt");
            
            Polynomial loaded = new Polynomial(new File("save_test1.txt"));
            double orig_result = original.evaluate(2.0);
            double load_result = loaded.evaluate(2.0);
            
            if (Math.abs(orig_result - load_result) < 1e-10) {
                System.out.println("✓ Test 26 PASSED: Save and load basic polynomial");
                passed++;
            } else {
                System.out.println("✗ Test 26 FAILED: Loaded polynomial differs from original");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 26 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 27: Save and load with negative coefficients
        try {
            double[] c = {-5.0, 3.0, -1.0};
            int[] e = {0, 2, 4};
            Polynomial original = new Polynomial(c, e);
            original.saveToFile("save_test2.txt");
            
            Polynomial loaded = new Polynomial(new File("save_test2.txt"));
            double orig_result = original.evaluate(1.0);
            double load_result = loaded.evaluate(1.0);
            
            if (Math.abs(orig_result - load_result) < 1e-10) {
                System.out.println("✓ Test 27 PASSED: Save and load with negatives");
                passed++;
            } else {
                System.out.println("✗ Test 27 FAILED: Loaded polynomial with negatives differs");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 27 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 28: Save constant polynomial
        try {
            double[] c = {42.0};
            int[] e = {0};
            Polynomial original = new Polynomial(c, e);
            original.saveToFile("save_test3.txt");
            
            Polynomial loaded = new Polynomial(new File("save_test3.txt"));
            double result = loaded.evaluate(100.0);
            
            if (Math.abs(result - 42.0) < 1e-10) {
                System.out.println("✓ Test 28 PASSED: Save and load constant");
                passed++;
            } else {
                System.out.println("✗ Test 28 FAILED: Constant polynomial save/load failed");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 28 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 29: Save single variable polynomial
        try {
            double[] c = {1.0};
            int[] e = {1};
            Polynomial original = new Polynomial(c, e);
            original.saveToFile("save_test4.txt");
            
            Polynomial loaded = new Polynomial(new File("save_test4.txt"));
            double result = loaded.evaluate(5.0);
            
            if (Math.abs(result - 5.0) < 1e-10) {
                System.out.println("✓ Test 29 PASSED: Save and load single variable");
                passed++;
            } else {
                System.out.println("✗ Test 29 FAILED: Single variable save/load failed");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 29 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 30: Round trip multiple times
        try {
            double[] c = {1.5, -2.7, 3.14};
            int[] e = {1, 3, 5};
            Polynomial p_1 = new Polynomial(c, e);
            
            p_1.saveToFile("round1.txt");
            Polynomial p_2 = new Polynomial(new File("round1.txt"));
            p_2.saveToFile("round2.txt");
            Polynomial p_3 = new Polynomial(new File("round2.txt"));
            
            double orig_result = p_1.evaluate(2.0);
            double final_result = p_3.evaluate(2.0);
            
            if (Math.abs(orig_result - final_result) < 1e-5) {
                System.out.println("✓ Test 30 PASSED: Multiple round trips");
                passed++;
            } else {
                System.out.println("✗ Test 30 FAILED: Multiple round trips failed");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 30 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
    
    private static int testZeroCoefficients() {
        System.out.println("\n=== ZERO COEFFICIENT TESTS ===");
        int passed = 0;
        
        // Test 31: Adding polynomials that cancel out
        try {
            double[] c_1 = {1.0, 2.0}, c_2 = {-1.0, -2.0};
            int[] e_1 = {1, 2}, e_2 = {1, 2};
            Polynomial p_1 = new Polynomial(c_1, e_1);
            Polynomial p_2 = new Polynomial(c_2, e_2);
            Polynomial sum = p_1.add(p_2);
            double result = sum.evaluate(5.0);
            
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 31 PASSED: Complete cancellation in addition");
                passed++;
            } else {
                System.out.println("✗ Test 31 FAILED: Expected 0 from cancellation, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 31 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 32: Zero coefficient in constructor
        try {
            double[] c = {0.0, 5.0, 0.0, 3.0};
            int[] e = {0, 1, 2, 3};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(2.0); // 0 + 10 + 0 + 24 = 34
            
            if (Math.abs(result - 34.0) < 1e-10) {
                System.out.println("✓ Test 32 PASSED: Zero coefficients in constructor");
                passed++;
            } else {
                System.out.println("✗ Test 32 FAILED: Expected 34, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 32 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 33: Multiplication with zero coefficient
        try {
            double[] c1 = {0.0, 1.0}, c2 = {2.0, 3.0};
            int[] e1 = {0, 1}, e2 = {0, 1};
            Polynomial p1 = new Polynomial(c1, e1); // x
            Polynomial p2 = new Polynomial(c2, e2); // 2 + 3x
            Polynomial product = p1.multiply(p2); // 2x + 3x^2
            double result = product.evaluate(2.0); // 4 + 12 = 16
            
            if (Math.abs(result - 16.0) < 1e-10) {
                System.out.println("✓ Test 33 PASSED: Multiplication with zero coefficient");
                passed++;
            } else {
                System.out.println("✗ Test 33 FAILED: Expected 16, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 33 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 34: All zero polynomial
        try {
            double[] c = {0.0};
            int[] e = {0};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(1000.0);
            
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 34 PASSED: All zero polynomial");
                passed++;
            } else {
                System.out.println("✗ Test 34 FAILED: Expected 0, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 34 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 35: Save zero polynomial
        try {
            double[] c = {0.0};
            int[] e = {0};
            Polynomial zero = new Polynomial(c, e);
            zero.saveToFile("zero_test.txt");
            
            Polynomial loaded = new Polynomial(new File("zero_test.txt"));
            double result = loaded.evaluate(42.0);
            
            if (Math.abs(result - 0.0) < 1e-10) {
                System.out.println("✓ Test 35 PASSED: Save and load zero polynomial");
                passed++;
            } else {
                System.out.println("✗ Test 35 FAILED: Zero polynomial save/load failed");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 35 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
    
    private static int testPrecision() {
        System.out.println("\n=== PRECISION TESTS ===");
        int passed = 0;
        
        // Test 36: Large coefficients
        try {
            double[] c = {1e10, -1e10};
            int[] e = {0, 1};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(1.0); // 1e10 - 1e10 = 0
            
            if (Math.abs(result - 0.0) < 1e-5) {
                System.out.println("✓ Test 36 PASSED: Large coefficients");
                passed++;
            } else {
                System.out.println("✗ Test 36 FAILED: Large coefficient precision issue");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 36 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 37: Small coefficients
        try {
            double[] c = {1e-10, 2e-10};
            int[] e = {0, 1};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(1.0); // 1e-10 + 2e-10 = 3e-10
            
            if (Math.abs(result - 3e-10) < 1e-15) {
                System.out.println("✓ Test 37 PASSED: Small coefficients");
                passed++;
            } else {
                System.out.println("✗ Test 37 FAILED: Small coefficient precision issue");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 37 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 38: Large exponents
        try {
            double[] c = {1.0};
            int[] e = {20};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(2.0); // 2^20 = 1048576
            
            if (Math.abs(result - 1048576.0) < 1e-10) {
                System.out.println("✓ Test 38 PASSED: Large exponents");
                passed++;
            } else {
                System.out.println("✗ Test 38 FAILED: Large exponent calculation error");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 38 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 39: Fractional evaluation
        try {
            double[] c = {1.0, -1.0};
            int[] e = {0, 2};
            Polynomial p = new Polynomial(c, e); // 1 - x^2
            double result = p.evaluate(0.5); // 1 - 0.25 = 0.75
            
            if (Math.abs(result - 0.75) < 1e-15) {
                System.out.println("✓ Test 39 PASSED: Fractional evaluation");
                passed++;
            } else {
                System.out.println("✗ Test 39 FAILED: Fractional evaluation error");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 39 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 40: Decimal precision in file operations
        try {
            double[] c = {3.14159, -2.71828};
            int[] e = {0, 1};
            Polynomial original = new Polynomial(c, e);
            original.saveToFile("precision_test.txt");
            
            Polynomial loaded = new Polynomial(new File("precision_test.txt"));
            double orig_result = original.evaluate(1.0);
            double load_result = loaded.evaluate(1.0);
            
            if (Math.abs(orig_result - load_result) < 1e-4) {
                System.out.println("✓ Test 40 PASSED: Decimal precision in file ops");
                passed++;
            } else {
                System.out.println("✗ Test 40 FAILED: Precision lost in file operations");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 40 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }

    private static int testExtremeEdgeCases() {
        System.out.println("\n=== EXTREME EDGE CASE TESTS ===");
        int passed = 0;
        
        // Test 41: Polynomial with coefficient 1 (implicit)
        try {
            FileWriter writer = new FileWriter("edge1.txt");
            writer.write("x3+x2+x+1");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("edge1.txt"));
            double result = poly.evaluate(1.0); // 1 + 1 + 1 + 1 = 4
            if (Math.abs(result - 4.0) < 1e-10) {
                System.out.println("✓ Test 41 PASSED: Implicit coefficient 1 parsing");
                passed++;
            } else {
                System.out.println("✗ Test 41 FAILED: Expected 4, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 41 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 42: Polynomial with negative coefficients mixed
        try {
            FileWriter writer = new FileWriter("edge2.txt");
            writer.write("-x3-x2-x-1");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("edge2.txt"));
            double result = poly.evaluate(1.0); // -1 - 1 - 1 - 1 = -4
            if (Math.abs(result - (-4.0)) < 1e-10) {
                System.out.println("✓ Test 42 PASSED: Negative implicit coefficients");
                passed++;
            } else {
                System.out.println("✗ Test 42 FAILED: Expected -4, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 42 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 43: Very large polynomial degree
        try {
            double[] c = {1.0, 1.0};
            int[] e = {0, 100};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(1.0); // 1 + 1 = 2
            if (Math.abs(result - 2.0) < 1e-10) {
                System.out.println("✓ Test 43 PASSED: Very large degree polynomial");
                passed++;
            } else {
                System.out.println("✗ Test 43 FAILED: Expected 2, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 43 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 44: Polynomial with many terms
        try {
            double[] c = new double[10];
            int[] e = new int[10];
            for (int i = 0; i < 10; i++) {
                c[i] = i + 1;
                e[i] = i;
            }
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(0.0); // Should be first coefficient = 1
            if (Math.abs(result - 1.0) < 1e-10) {
                System.out.println("✓ Test 44 PASSED: Many terms polynomial");
                passed++;
            } else {
                System.out.println("✗ Test 44 FAILED: Expected 1, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 44 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 45: Polynomial with zero exponent only
        try {
            FileWriter writer = new FileWriter("edge3.txt");
            writer.write("5");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("edge3.txt"));
            poly.saveToFile("edge3_saved.txt");
            
            Polynomial loaded = new Polynomial(new File("edge3_saved.txt"));
            double result = loaded.evaluate(999.0); // Should always be 5
            if (Math.abs(result - 5.0) < 1e-10) {
                System.out.println("✓ Test 45 PASSED: Zero exponent save/load");
                passed++;
            } else {
                System.out.println("✗ Test 45 FAILED: Expected 5, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 45 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 46: Addition that creates gaps in exponents
        try {
            double[] c_1 = {1.0, 1.0}, c_2 = {1.0, -1.0};
            int[] e_1 = {0, 2}, e_2 = {1, 2};
            Polynomial p_1 = new Polynomial(c_1, e_1); // 1 + x^2
            Polynomial p_2 = new Polynomial(c_2, e_2); // x - x^2
            Polynomial sum = p_1.add(p_2); // 1 + x + x^2 - x^2 = 1 + x
            double result = sum.evaluate(3.0); // 1 + 3 = 4
            if (Math.abs(result - 4.0) < 1e-10) {
                System.out.println("✓ Test 46 PASSED: Addition with exponent gaps");
                passed++;
            } else {
                System.out.println("✗ Test 46 FAILED: Expected 4, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 46 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 47: Multiplication creating many terms
        try {
            double[] c_1 = {1.0, 1.0, 1.0}, c_2 = {1.0, 1.0};
            int[] e_1 = {0, 1, 2}, e_2 = {0, 1};
            Polynomial p_1 = new Polynomial(c_1, e_1); // 1 + x + x^2
            Polynomial p_2 = new Polynomial(c_2, e_2); // 1 + x
            Polynomial product = p_1.multiply(p_2); // (1+x+x^2)(1+x) = 1+x+x^2+x+x^2+x^3 = 1+2x+2x^2+x^3
            double result = product.evaluate(1.0); // 1 + 2 + 2 + 1 = 6
            if (Math.abs(result - 6.0) < 1e-10) {
                System.out.println("✓ Test 47 PASSED: Complex multiplication");
                passed++;
            } else {
                System.out.println("✗ Test 47 FAILED: Expected 6, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 47 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 48: File parsing with mixed positive/negative terms
        try {
            FileWriter writer = new FileWriter("edge4.txt");
            writer.write("3x5-2x4+x3-4x2+5x-6");
            writer.close();
            
            Polynomial poly = new Polynomial(new File("edge4.txt"));
            double result = poly.evaluate(0.0); // Should be -6
            if (Math.abs(result - (-6.0)) < 1e-10) {
                System.out.println("✓ Test 48 PASSED: Complex mixed terms parsing");
                passed++;
            } else {
                System.out.println("✗ Test 48 FAILED: Expected -6, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 48 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 49: Zero coefficient handling in arrays
        try {
            double[] c = {5.0, 0.0, 0.0, 3.0, 0.0};
            int[] e = {0, 1, 2, 3, 4};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(2.0); // 5 + 0 + 0 + 24 + 0 = 29
            if (Math.abs(result - 29.0) < 1e-10) {
                System.out.println("✓ Test 49 PASSED: Mixed zero coefficients");
                passed++;
            } else {
                System.out.println("✗ Test 49 FAILED: Expected 29, got " + result);
            }
        } catch (Exception e) {
            System.out.println("✗ Test 49 FAILED: Exception - " + e.getMessage());
        }
        
        // Test 50: Stress test with very small and very large values
        try {
            double[] c = {1e-100, 1e100};
            int[] e = {0, 1};
            Polynomial p = new Polynomial(c, e);
            double result = p.evaluate(1e-50); // 1e-100 + 1e100 * 1e-50 = 1e-100 + 1e50
            // The 1e50 term dominates
            if (result > 1e49) {
                System.out.println("✓ Test 50 PASSED: Extreme value stress test");
                passed++;
            } else {
                System.out.println("✗ Test 50 FAILED: Extreme values not handled correctly");
            }
        } catch (Exception e) {
            System.out.println("✗ Test 50 FAILED: Exception - " + e.getMessage());
        }
        
        return passed;
    }
}