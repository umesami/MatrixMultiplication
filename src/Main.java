import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[][] matrixA = readFromMatrixFile("./testCases/test10A.txt");
        int[][] matrixB = readFromMatrixFile("./testCases/test10B.txt");

        int n = matrixA.length;

        int[][] matrixClassic = classicMatrixMultiplication(matrixA, matrixB, n);
        int[][] matrixDNC = divideAndConquer(matrixA, matrixB, n);
        int[][] matrixStrass = strassenAlgo(matrixA, matrixB, n);

        System.out.println("We are multiplying the following matrices:");
        System.out.println("Matrix A:");
        printMatrix(matrixA);

        System.out.println("Matrix B:");
        printMatrix(matrixB);


//        System.out.println("The resulting matrix using Classic Matrix Multiplication is:");
//        printMatrix(matrixClassic);

//        System.out.println("The resulting matrix using Divide And Conquer Matrix Multiplication is:");
//        printMatrix(matrixDNC);

        System.out.println("The resulting matrix using Strassen's Matrix Multiplication is:");
        printMatrix(matrixStrass);
    }

    public static int[][] strassenAlgo(int[][] matrixA, int[][]matrixB, int n){
        //base case
        if (n == 1) {
            return new int[][]{{matrixA[0][0] * matrixB[0][0]}};
        }

        int newN = n / 2;

        //Partition the matrices
        int[][] matrixA11 = new int[newN][newN];
        int[][] matrixA12 = new int[newN][newN];
        int[][] matrixA21 = new int[newN][newN];
        int[][] matrixA22 = new int[newN][newN];

        int[][] matrixB11 = new int[newN][newN];
        int[][] matrixB12 = new int[newN][newN];
        int[][] matrixB21 = new int[newN][newN];
        int[][] matrixB22 = new int[newN][newN];

        //Fill the new submatrices
        for (int i = 0; i < newN; i++) {
            for (int j = 0; j < newN; j++) {
                matrixA11[i][j] = matrixA[i][j];
                matrixA12[i][j] = matrixA[i][j + newN];
                matrixA21[i][j] = matrixA[i + newN][j];
                matrixA22[i][j] = matrixA[i + newN][j + newN];

                matrixB11[i][j] = matrixB[i][j];
                matrixB12[i][j] = matrixB[i][j + newN];
                matrixB21[i][j] = matrixB[i + newN][j];
                matrixB22[i][j] = matrixB[i + newN][j + newN];
            }
        }

        //Strassen's algorithm
        int[][] M1 = strassenAlgo(addMatrices(matrixA11, matrixA22), addMatrices(matrixB11, matrixB22), newN);
        int[][] M2 = strassenAlgo(addMatrices(matrixA21, matrixA22), matrixB11, newN);
        int[][] M3 = strassenAlgo(matrixA11, subtractMatrices(matrixB12, matrixB22), newN);
        int[][] M4 = strassenAlgo(matrixA22, subtractMatrices(matrixB21, matrixB11), newN);
        int[][] M5 = strassenAlgo(addMatrices(matrixA11, matrixA12), matrixB22, newN);
        int[][] M6 = strassenAlgo(subtractMatrices(matrixA21, matrixA11), addMatrices(matrixB11, matrixB12), newN);
        int[][] M7 = strassenAlgo(subtractMatrices(matrixA12, matrixA22), addMatrices(matrixB21, matrixB22), newN);

        //Compute the resulting quadrants
        int[][] matrixC11 = addMatrices(subtractMatrices(addMatrices(M1, M4), M5), M7);
        int[][] matrixC12 = addMatrices(M3, M5);
        int[][] matrixC21 = addMatrices(M2, M4);
        int[][] matrixC22 = addMatrices(subtractMatrices(addMatrices(M1, M3), M2), M6);

        //Combine into resulting matrix, C
        int[][] matrixC = combineQuadrants(matrixC11, matrixC12, matrixC21, matrixC22);
        return matrixC;
    }
    public static int[][] divideAndConquer(int[][] matrixA, int[][]matrixB, int n){
        if(n == 1){
            int[][] product = {{matrixA[0][0] * matrixB[0][0]}};
            return product;
        }

        int newN = n/2;

        //Split the matrices into 4 equal sized quadrants
        int[][]matrixA11 = new int[newN][newN];
        int[][]matrixA12 = new int[newN][newN];
        int[][]matrixA21 = new int[newN][newN];
        int[][]matrixA22 = new int[newN][newN];

        int[][]matrixB11 = new int[newN][newN];
        int[][]matrixB12 = new int[newN][newN];
        int[][]matrixB21 = new int[newN][newN];
        int[][]matrixB22 = new int[newN][newN];

        //fill in the new submatrices
        for (int i = 0; i < newN; i++) {
            for (int j = 0; j < newN; j++) {
                matrixA11[i][j] = matrixA[i][j];
                matrixA12[i][j] = matrixA[i][j + newN];
                matrixA21[i][j] = matrixA[i + newN][j];
                matrixA22[i][j] = matrixA[i + newN][j + newN];

                matrixB11[i][j] = matrixB[i][j];
                matrixB12[i][j] = matrixB[i][j + newN];
                matrixB21[i][j] = matrixB[i + newN][j];
                matrixB22[i][j] = matrixB[i + newN][j + newN];
            }
        }

        //recursively multiplying the submatrices into submatrices for resulting matrix C
        int[][] matrixC11 = addMatrices(divideAndConquer(matrixA11, matrixB11, newN), divideAndConquer(matrixA12, matrixB21, newN));
        int[][] matrixC12 = addMatrices(divideAndConquer(matrixA11, matrixB12, newN), divideAndConquer(matrixA12, matrixB22, newN));
        int[][] matrixC21 = addMatrices(divideAndConquer(matrixA21, matrixB11, newN), divideAndConquer(matrixA22, matrixB21, newN));
        int[][] matrixC22 = addMatrices(divideAndConquer(matrixA21, matrixB12, newN), divideAndConquer(matrixA22, matrixB22, newN));

        int[][] matrixC = combineQuadrants(matrixC11, matrixC12, matrixC21, matrixC22);
        return matrixC;
    }

    public static int[][] classicMatrixMultiplication(int[][] matrixA, int[][] matrixB, int n){
        // Resulting matrix, C
        int[][] matrixC = new int[n][n];

        // Perform classic matrix multiplication algorithm
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // C[i][j] is the dot product of row i of A and column j of B
                matrixC[i][j] = 0;
                for (int k = 0; k < n; k++) {
                    matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return matrixC;
    }

    //Helper functions for our algorithms

    //Helper function to combine matrices
    public static int[][] combineQuadrants(int[][]matrixC11, int[][]matrixC12, int[][]matrixC21,int[][] matrixC22){
        //new resulting matrix should be 2n x 2n, since we are combining the n x n matrices together
        int n = matrixC11.length * 2;
        int[][] matrixC = new int[n][n];
        int newSize = n / 2;

        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                matrixC[i][j] = matrixC11[i][j];
                matrixC[i][j + newSize] = matrixC12[i][j];
                matrixC[i + newSize][j] = matrixC21[i][j];
                matrixC[i + newSize][j + newSize] = matrixC22[i][j];
            }
        }
        return matrixC;
    }

    //Helper function to subtract matrices from each other
    public static int[][] subtractMatrices(int[][] matrixA, int[][] matrixB) {
        int n = matrixA.length;
        int[][] matrixC = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrixC[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }
        return matrixC;
    }

    //Helper function to add matrices together
    public static int[][] addMatrices(int[][] matrixA, int[][] matrixB){
        int n = matrixA.length;
        int[][] matrixC = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matrixC[i][j] = matrixA[i][j] + matrixB[i][j];
        return matrixC;
    }


    //Helper function to print the matrix
    public static void printMatrix(int[][] matrixC){
        int n = matrixC.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(matrixC[i][j] + " ");
            }
            System.out.println();
        }
    }

    //Helper function to read the matrix from an input file
    public static int[][] readFromMatrixFile(String fileName){
        /*input files look like this:
        2
        1 2
        3 4
         */
        try{
            Scanner scan = new Scanner(new File(fileName));

            //input files have n written at the top, indicating the size of the n x n matrix
            int n = Integer.parseInt(scan.nextLine().trim());

            //create n x n matrix
            int[][] matrix = new int[n][n];

            for(int i = 0; i < n; i++){
                //removes whitespaces before and after input
                String line = scan.nextLine().trim();

                //adds integers into an array, split decided where there is whitespace between numbers
                //for example, 43 6 -> ["43", "6"]
                String[] values = line.split("\\s+");

                //store the values of each line into the appropriate index of the matrix
                for(int j = 0; j < n; j++){
                    matrix[i][j] = Integer.parseInt(values[j]);
                }
            }

            scan.close();

            //return the resulting matrix
            return matrix;
        }
        catch(FileNotFoundException e){
            System.out.println("File not found: " + fileName);
            return null;
        }
        catch(NumberFormatException e){
            System.out.println("Incorrect format in txt file: " + fileName);
            return null;
        }
    }
}