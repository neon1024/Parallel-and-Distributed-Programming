using System.Diagnostics;

namespace Lab_5;

public static class Program
{
    public static void Main()
    {
        int[] firstPolynomial = new int[] { 1, 2, 3 };
        int[] secondPolynomial = new int[] { 4, 5, 6 };
        int[] resultedPolynomial;
        
        Console.Out.WriteLine("First polynomial: " + PolynomialToString(firstPolynomial));
        Console.Out.WriteLine("Second polynomial: " + PolynomialToString(secondPolynomial));
        Console.Out.WriteLine("");
        
        Stopwatch timer = new Stopwatch();
        
        // Naive sequential multiplication
        timer.Start();
        resultedPolynomial = SequentialPolynomialMultiplication.MultiplyNaive(firstPolynomial, secondPolynomial);
        timer.Stop();
        
        Console.Out.WriteLine($"Naive sequential multiplication took: {timer.Elapsed.Seconds}s, {timer.Elapsed.Milliseconds}ms");
        Console.Out.WriteLine("Result: " + PolynomialToString(resultedPolynomial));
        Console.Out.WriteLine("");
        
        // Naive parallel multiplication
        timer.Restart();
        resultedPolynomial = ParallelPolynomialMultiplication.MultiplyNaiveParallel(firstPolynomial, secondPolynomial);
        timer.Stop();
        
        Console.Out.WriteLine($"Naive parallel multiplication took: {timer.Elapsed.Seconds}s, {timer.Elapsed.Milliseconds}ms");
        Console.Out.WriteLine("Result: " + PolynomialToString(resultedPolynomial));
        Console.Out.WriteLine("");
        
        // Karatsuba sequential multiplication
        timer.Restart();
        resultedPolynomial = SequentialPolynomialMultiplication.MultiplyKaratsuba(firstPolynomial, secondPolynomial);
        timer.Stop();

        Console.Out.WriteLine("Karatsuba sequential multiplication took: " + timer.Elapsed.Seconds + "s, " + timer.Elapsed.Milliseconds + "ms");
        Console.Out.WriteLine("Result: " + PolynomialToString(resultedPolynomial));
        Console.Out.WriteLine("");
        
        // Karatsuba parallel multiplication
        timer.Restart();
        resultedPolynomial = ParallelPolynomialMultiplication.MultiplyKaratsubaParallel(firstPolynomial, secondPolynomial);
        timer.Stop();

        Console.Out.WriteLine("Karatsuba parallel multiplication took: " + timer.Elapsed.Seconds + "s, " + timer.Elapsed.Milliseconds + "ms");
        Console.Out.WriteLine("Result: " + PolynomialToString(resultedPolynomial));
    }
    
    private static String PolynomialToString(int[] polynomial)
    {
        String result = "";
        
        for (int i = 0; i < polynomial.Length; ++i)
        {
            if (i != polynomial.Length - 1)
            {
                result += polynomial[i] + "x^" + (polynomial.Length - i - 1);
            }
            else
            {
                result += polynomial[i];
            }

            if(i != polynomial.Length - 1)
            {
                result += " + ";
            }
            else
            {
                result += " ";
            }
        }

        return result;
    }
}