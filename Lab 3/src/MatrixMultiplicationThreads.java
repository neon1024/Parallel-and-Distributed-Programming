import java.util.Scanner;

public class MatrixMultiplicationThreads {

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Matrix size: ");

        int matrixSize = scanner.nextInt();

        int[][] matrixA = new int[matrixSize][matrixSize];
        int[][] matrixB = new int[matrixSize][matrixSize];
        int[][] result = new int[matrixSize][matrixSize];

        // Initialize the matrices with sample data
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                matrixA[i][j] = 1;
                matrixB[i][j] = 1;
            }
        }

        System.out.print("Number of threads: ");

        int numberOfThreads = scanner.nextInt();

        scanner.close();

        Thread[] threads = new Thread[numberOfThreads];
        int totalElements = matrixSize * matrixSize;
        int elementsPerThread = totalElements / numberOfThreads;
        int remainderElements = totalElements % numberOfThreads;

        int startIndex = 0;

        // Start timer
        long startTime = System.nanoTime();

        // Create and execute the threads
        for (int i = 0; i < numberOfThreads; i++) {
            int endIndex = startIndex + elementsPerThread;
            if (i == numberOfThreads - 1) {
                endIndex += remainderElements;  // Handle remainder in the last thread
            }

            threads[i] = new Thread(new MatrixTask(matrixA, matrixB, result, startIndex, endIndex, matrixSize));
            threads[i].start();
            startIndex = endIndex;
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // End timer
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  // / 1_000_000;  // Convert to milliseconds

        // Print the time taken
        System.out.println("Threads approach took: " + duration + " ns");

        // Print the result
        printMatrix(result);
    }

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }
}
