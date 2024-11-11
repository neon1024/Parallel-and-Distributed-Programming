public class MatrixMultiplication {
    // Function to compute a single element of the resulting matrix
    public static int computeElement(int[][] matrixA, int[][] matrixB, int row, int col) {
        int sum = 0;
        int n = matrixA[0].length;  // Assuming matrixA's columns = matrixB's rows
        for (int i = 0; i < n; i++) {
            sum += matrixA[row][i] * matrixB[i][col];
        }
        return sum;
    }
}
