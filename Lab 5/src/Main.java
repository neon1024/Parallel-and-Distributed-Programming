import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Polynomial p = new Polynomial(1000);
        // Polynomial q = new Polynomial(1000);
		Polynomial p = new Polynomial(new ArrayList<Integer>(Arrays.asList(5, 4, 2, 4)));
		Polynomial q = new Polynomial(new ArrayList<Integer>(Arrays.asList(6, 3, 7)));

        System.out.println("Polynomial 1:" + p);
        System.out.println("Polynomial 2:" + q);
        System.out.println();

        // Simple
        System.out.println(multiplication1(p, q).toString() + "\n");
        System.out.println(multiplication2(p, q).toString() + "\n");

        // Karatsuba
        System.out.println(multiplication3(p, q).toString() + "\n");
        System.out.println(multiplication4(p, q).toString() + "\n");
    }

    private static Polynomial multiplication1(Polynomial p, Polynomial q) {
        long startTime = System.currentTimeMillis();
        Polynomial result1 = PolynomialOperation.multiplicationSequentialForm(p, q);
        long endTime = System.currentTimeMillis();

        System.out.println("Simple sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");

        return result1;
    }

    private static Polynomial multiplication2(Polynomial p, Polynomial q) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Polynomial result2 = PolynomialOperation.multiplicationParallelizedForm(p, q, 5);
        long endTime = System.currentTimeMillis();

        System.out.println("Simple parallel multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");

        return result2;
    }

    private static Polynomial multiplication3(Polynomial p, Polynomial q) {
        long startTime = System.currentTimeMillis();
        Polynomial result3 = PolynomialOperation.multiplicationKaratsubaSequentialForm(p, q);
        long endTime = System.currentTimeMillis();

        System.out.println("Karatsuba sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");

        return result3;
    }

    private static Polynomial multiplication4(Polynomial p, Polynomial q) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Polynomial result4 = PolynomialOperation.multiplicationKaratsubaParallelizedForm(p, q, 4);
        long endTime = System.currentTimeMillis();

        System.out.println("Karatsuba parallel multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");

        return result4;
    }
}
