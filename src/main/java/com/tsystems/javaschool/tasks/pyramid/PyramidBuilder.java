package com.tsystems.javaschool.tasks.pyramid;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */



    public int[][] buildPyramid(List<Integer> inputNumbers) {
        if (!isValidData(inputNumbers)) throw new CannotBuildPyramidException();
        int rows = findNumOfRows(inputNumbers.size());
        int columns = findNumOfColumns(inputNumbers.size());
        int [][] pyramid = createMatrix(rows, columns);
        fillMatrix(pyramid, inputNumbers);
        return pyramid;
    }

    private boolean isValidData(List<Integer> inputNumbers){
        if (inputNumbers==null || inputNumbers.size()==0 || inputNumbers.contains(null)) return false;
        long size = (long)inputNumbers.size();
        double res = (-1 + Math.sqrt(1+8*size))/2;
        if (res % 1 != 0) return false;
        return true;
    }

    private int findNumOfRows(int listSize){
        return (int)(-1 + Math.sqrt(1+8*listSize))/2;
    }

    private int findNumOfColumns(int listSize){
        return findNumOfRows(listSize)*2-1;
    }

    private int[][] createMatrix(int rowsNum, int columnsNum){
        if (rowsNum <=0 || columnsNum <= 0) return null;
        int[][] matrix = new int[rowsNum][columnsNum];
        for(int i = 0; i < rowsNum; i++){
            matrix[i] = new int[columnsNum];
            for(int j = 0; j < matrix[i].length; j++){
                matrix[i][j] = 0;
            }
        }
        return matrix;
    }

    private static void fillMatrix(int[][] matrix, List<Integer> inputNumbers){
        if (matrix == null || inputNumbers == null || inputNumbers.size()==0) return;
        // sort!!!!!
        inputNumbers.sort(Comparator.naturalOrder());
        int rows = matrix.length;
        int columns = rows*2-1;
        Iterator<Integer> iterator = inputNumbers.iterator();
        int middleIndex = columns/2;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < i+1; j++){
                matrix[i][middleIndex-i+j*2] = iterator.next();
            }
        }
    }

}
