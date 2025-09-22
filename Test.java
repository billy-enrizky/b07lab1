public class Test {
    public static void main(String [] args) {
        // Test 1: Zero polynomial
        Polynomial p = new Polynomial();
        System.out.println("Test 1 - Zero polynomial:");
        System.out.println("p.evaluate(3) = " + p.evaluate(3));
        System.out.println("p.evaluate(0) = " + p.evaluate(0));
        System.out.println("p.hasRoot(0) = " + p.hasRoot(0));
        System.out.println("p.hasRoot(5) = " + p.hasRoot(5));
        System.out.println();

        // Test 2: Constant polynomial
        double[] c_const = {7};
        Polynomial p_const = new Polynomial(c_const);
        System.out.println("Test 2 - Constant polynomial (7):");
        System.out.println("p_const.evaluate(0) = " + p_const.evaluate(0));
        System.out.println("p_const.evaluate(10) = " + p_const.evaluate(10));
        System.out.println("p_const.hasRoot(7) = " + p_const.hasRoot(7));
        System.out.println("p_const.hasRoot(0) = " + p_const.hasRoot(0));
        System.out.println();

        // Test 3: Linear polynomial
        double[] c_linear = {3, 2}; // 3 + 2x
        Polynomial p_linear = new Polynomial(c_linear);
        System.out.println("Test 3 - Linear polynomial (3 + 2x):");
        System.out.println("p_linear.evaluate(0) = " + p_linear.evaluate(0));
        System.out.println("p_linear.evaluate(1) = " + p_linear.evaluate(1));
        System.out.println("p_linear.evaluate(-1.5) = " + p_linear.evaluate(-1.5));
        System.out.println("p_linear.hasRoot(-1.5) = " + p_linear.hasRoot(-1.5));
        System.out.println("p_linear.hasRoot(0) = " + p_linear.hasRoot(0));
        System.out.println();

        // Test 4: Quadratic polynomial
        double[] c_quad = {1, 0, 1}; // 1 + x^2
        Polynomial p_quad = new Polynomial(c_quad);
        System.out.println("Test 4 - Quadratic polynomial (1 + x^2):");
        System.out.println("p_quad.evaluate(0) = " + p_quad.evaluate(0));
        System.out.println("p_quad.evaluate(1) = " + p_quad.evaluate(1));
        System.out.println("p_quad.evaluate(-1) = " + p_quad.evaluate(-1));
        System.out.println("p_quad.hasRoot(0) = " + p_quad.hasRoot(0));
        System.out.println("p_quad.hasRoot(1) = " + p_quad.hasRoot(1));
        System.out.println();

        // Test 5: Original polynomials and addition
        double [] c1 = {6,0,0,5}; // 6 + 5x^3
        Polynomial p1 = new Polynomial(c1);
        double [] c2 = {0,-2,0,0,-9}; // -2x - 9x^4
        Polynomial p2 = new Polynomial(c2);
        Polynomial s = p1.add(p2);
        System.out.println("Test 5 - Addition of polynomials:");
        System.out.println("p1 (6 + 5x^3) at x=0: " + p1.evaluate(0));
        System.out.println("p2 (-2x - 9x^4) at x=0: " + p2.evaluate(0));
        System.out.println("s = p1 + p2 at x=0: " + s.evaluate(0));
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        System.out.println("s(1) = " + s.evaluate(1));
        System.out.println("s(2) = " + s.evaluate(2));
        if(s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");
        System.out.println();

        // Test 6: Addition of same degree polynomials
        double[] c3 = {1, 2, 3}; // 1 + 2x + 3x^2
        double[] c4 = {4, 5, 6}; // 4 + 5x + 6x^2
        Polynomial p3 = new Polynomial(c3);
        Polynomial p4 = new Polynomial(c4);
        Polynomial s2 = p3.add(p4);
        System.out.println("Test 6 - Addition of same degree polynomials:");
        System.out.println("p3 + p4 at x=1: " + s2.evaluate(1));
        System.out.println("Expected: (1+2+3) + (4+5+6) = " + (1+2+3 + 4+5+6));
        System.out.println();

        // Test 7: Addition with zero polynomial
        Polynomial s3 = p1.add(p);
        System.out.println("Test 7 - Addition with zero polynomial:");
        System.out.println("p1 + zero at x=2: " + s3.evaluate(2));
        System.out.println("p1 at x=2: " + p1.evaluate(2));
        System.out.println("Equal? " + (s3.evaluate(2) == p1.evaluate(2)));
        System.out.println();

        // Test 8: hasRoot with known roots
        double[] c_root = {0, 1}; // x (root at x=0)
        Polynomial p_root = new Polynomial(c_root);
        System.out.println("Test 8 - hasRoot with known roots:");
        System.out.println("p_root (x) hasRoot(0) = " + p_root.hasRoot(0));
        System.out.println("p_root (x) hasRoot(1) = " + p_root.hasRoot(1));
        System.out.println("p_root (x) hasRoot(0.0) = " + p_root.hasRoot(0.0));
    }
}