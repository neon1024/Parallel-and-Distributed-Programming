public class MatrixTask implements Runnable {
    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] result;
    private int startIndex;
    private int endIndex;
    private int matrixSize;

    public MatrixTask(int[][] matrixA, int[][] matrixB, int[][] result, int startIndex, int endIndex, int matrixSize) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.matrixSize = matrixSize;
    }

    @Override
    public void run() {
        for (int index = startIndex; index < endIndex; index++) {
            int row = index / matrixSize;
            int col = index % matrixSize;
            result[row][col] = MatrixMultiplication.computeElement(matrixA, matrixB, row, col);
        }
    }
}
