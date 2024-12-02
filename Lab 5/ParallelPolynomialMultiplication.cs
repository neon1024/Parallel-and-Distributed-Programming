using System;
using System.Threading.Tasks;

namespace Lab_5;

public class ParallelPolynomialMultiplication
{
    public static int[] MultiplyNaiveParallel(int[] poly1, int[] poly2)
    {
        int n = poly1.Length;
        int m = poly2.Length;
        int[] result = new int[n + m - 1];

        Parallel.For(0, n, i =>
        {
            for (int j = 0; j < m; j++)
            {
                lock (result)  // Synchronization for shared resource (result array)
                {
                    result[i + j] += poly1[i] * poly2[j];
                }
            }
        });

        return result;
    }
    
    public static int[] MultiplyKaratsubaParallel(int[] poly1, int[] poly2)
    {
        int n = Math.Max(poly1.Length, poly2.Length);

        // If the length is 1, multiply directly
        if (n == 1) return new int[] { poly1[0] * poly2[0] };

        // Ensure the length is even by padding with zero if necessary
        if (n % 2 != 0)
        {
            n++;
            Array.Resize(ref poly1, n);
            Array.Resize(ref poly2, n);
        }

        int half = n / 2;

        var A = new int[half];
        var B = new int[n - half];
        var C = new int[half];
        var D = new int[n - half];

        // Split the polynomials
        Array.Copy(poly1, 0, A, 0, half);
        Array.Copy(poly1, half, B, 0, n - half);
        Array.Copy(poly2, 0, C, 0, half);
        Array.Copy(poly2, half, D, 0, n - half);

        // Parallel recursive computation of the three products using MultiplyKaratsubaParallel
        var ACTask = Task.Run(() => MultiplyKaratsubaParallel(A, C));
        var BDTask = Task.Run(() => MultiplyKaratsubaParallel(B, D));
        var AB_CDTask = Task.Run(() => MultiplyKaratsubaParallel(Add(A, B), Add(C, D)));

        // Wait for all tasks to complete
        Task.WhenAll(ACTask, BDTask, AB_CDTask).Wait();

        // Allocate space for the result
        var result = new int[2 * n - 1];

        // Add the intermediate results to the result array
        for (int i = 0; i < ACTask.Result.Length; i++) result[i] += ACTask.Result[i];
        for (int i = 0; i < BDTask.Result.Length; i++) result[i + n] += BDTask.Result[i];
        for (int i = 0; i < AB_CDTask.Result.Length; i++) result[i + half] += AB_CDTask.Result[i] - ACTask.Result[i] - BDTask.Result[i];

        return result;
    }

    private static int[] Add(int[] poly1, int[] poly2)
    {
        int length = Math.Max(poly1.Length, poly2.Length);
        int[] result = new int[length];

        for (int i = 0; i < poly1.Length; i++) result[i] += poly1[i];
        for (int i = 0; i < poly2.Length; i++) result[i] += poly2[i];

        return result;
    }
}