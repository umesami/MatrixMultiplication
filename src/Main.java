import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[][] matrixA = readFromMatrixFile("test1A.txt");
        int[][] matrixB = readFromMatrixFile("test1B.txt");

        int n = matrixA.length;

        int[][] matrixClassic = classicMatrixMultiplication(matrixA, matrixB, n);
        int[][] matrixDNC = divideAndConquer(matrixA, matrixB, n);
        int[][] matrixStrass = strassenAlgo(matrixA, matrixB, n);

        System.out.println("The resulting matrix using Classic Matrix Multiplication is:");
        printMatrix(matrixC);

        System.out.println("The resulting matrix using Divide And Conquer Matrix Multiplication is:");
        printMatrix(matrixC);

        System.out.println("The resulting matrix using Strassen's Matrix Multiplication is:");
        printMatrix(matrixC);
    }

    public static int[][] strassenAlgo(int[][] matrixA, int[][]matrixB, int n){

        return matrixC;
    }
    public static int[][] divideAndConquer(int[][] matrixA, int[][]matrixB, int n){


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

    public static void printMatrix(int[][] matrixC){
        int n = matrixC.length;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.print(matrixC[i][j]);
            }
            System.out.println();
        }
    }

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