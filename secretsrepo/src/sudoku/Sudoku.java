/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import static sudoku.SudokuBoard.COLUMNSIZE;

/**
 *
 * @author jonas
 */
public class Sudoku {

    /**
     * @param args the command line arguments
     */
    private Scanner sc;
    private int numTestCases;
    public Map<Cell, String> gameBoardMap;

    private SudokuBoard unsolvedBoardInstance;
    private SudokuBoard solutionBoardInstance;

    public Sudoku() {
        sc = new Scanner(System.in);
        numTestCases = sc.nextInt();

        //Verkligen använda? Fullösning för att fånga \n
        sc.nextLine();

        ArrayList<SudokuBoard> solvedInstances = new ArrayList<>();
        ArrayList<SudokuBoard> unsolvedInstances = new ArrayList<>();

        for (int testCaseID = 0; ((testCaseID < numTestCases) && numTestCases < 50); testCaseID++) {
            
            unsolvedBoardInstance = new SudokuBoard();
            solutionBoardInstance = new SudokuBoard();

            while (solutionBoardInstance.currentNumRows < solutionBoardInstance.MAXNUMROWS) {
                solutionBoardInstance.addRow(sc.nextLine());
            }
            while (unsolvedBoardInstance.currentNumRows < unsolvedBoardInstance.MAXNUMROWS) {
                unsolvedBoardInstance.addRow(sc.nextLine());
            }

            solvedInstances.add(solutionBoardInstance);
            unsolvedInstances.add(unsolvedBoardInstance);

            if (testCaseID != numTestCases - 1) {
                sc.nextLine();
            }
        }
        //If any error-checks return true, Yes is returned(Puzzle can be derived)
        ArrayList<String> responses = new ArrayList<>();
        for (int i = 0, n = solvedInstances.size(); i < n; i++) {
            unsolvedBoardInstance = unsolvedInstances.get(i);
            solutionBoardInstance = solvedInstances.get(i);

            System.out.println("NEW BOARD #"+(i+1));
            if (PerformTests()) {
                responses.add("Yes");
            } else {
                responses.add("No");
            }
        }

        for (String response : responses) {
            //System.out.println(response);
        }
    }

    public static void main(String[] args) {
        Sudoku sudoku = new Sudoku();
    }

    private boolean PerformTests() {
        /*Checks if the puzzle has been rotated clockwise or counter clockwise */
        
        
        
         if(compareClockwiseSudoku()){ return true; }
         if(compareCounterClockwiseSudoku()){ return true;}
        
         //Checks within a segment if a column/row has been swapped with another(or if it is in the same place as last week)
        
         if(compareColumnSwapsInSegment()){ return true;}
         if(compareRowSwapsInSegment()){ return true;}
        
         //Checks if two column-segments have been swapped with eachother(or if it is in the same place as last week)
         if(compareColumnSegments()){ return true;}
         if(compareRowSegments()){ return true;}
        
         if(checkPermutations()){ return true;}
         return false;
        
        /*
        
        if (compareClockwiseSudoku()) {
        }
        if (compareCounterClockwiseSudoku()) {
        }

        //Checks within a segment if a column/row has been swapped with another(or if it is in the same place as last week)
        if (compareColumnSwapsInSegment()) {
        }
        if (compareRowSwapsInSegment()) {
        }

        //Checks if two column-segments have been swapped with eachother(or if it is in the same place as last week)
        if (compareColumnSegments()) {
        }
        if (compareRowSegments()) {
        }

        if (checkPermutations()) {
        }
        return false;*/

        /*
         if(compareClockwiseSudoku()){ System.out.println("Board has been rotated clockwise");}
         if(compareCounterClockwiseSudoku()){ System.out.println("Board has been rotated counterclockwise");}
        
         //Checks within a segment if a column/row has been swapped with another(or if it is in the same place as last week)
         if(compareColumnSwapsInSegment()){ System.out.println("Column has been swapped within segment");}
         if(compareRowSwapsInSegment()){ System.out.println("Row has been swapped within segment");}
        
         //Checks if two column-segments have been swapped with eachother(or if it is in the same place as last week)
         if(compareColumnSegments()){ System.out.println("Column segment has been swapped with other segment");}
         if(compareRowSegments()){ System.out.println("Row segment has been swapped with other segment");}
        
         if(checkPermutations()){ System.out.println("String has been permuted");}
         System.out.println("");
         return false;*/
    }

    //A column-segment is considered a 3x9 segment where segment 0 is column 1-3, segment 1 is column 4-6, etc.
    private boolean ColumnSwapInSegment(int segment) {
        ArrayList<Cell> solvedColumn;
        ArrayList<Cell> unsolvedColumn;
        ArrayList<Cell> columnTwo = unsolvedBoardInstance.getColumn(segment * 3 + 1);
        ArrayList<Cell> columnThree = solutionBoardInstance.getColumn(segment * 3 + 2);

        //Compares column 0 with column 1+2
        for (int i = 1; i < SudokuBoard.ROWS_IN_SEGMENT; i++) {
            solvedColumn = solutionBoardInstance.getColumn(segment * 3);
            unsolvedColumn = unsolvedBoardInstance.getColumn((segment * 3) + i);

            if (compareColumn(unsolvedColumn, solvedColumn)) {
                solvedColumn = solutionBoardInstance.getColumn((segment * 3) + i);
                unsolvedColumn = unsolvedBoardInstance.getColumn((segment * 3));
                if(compareColumn(unsolvedColumn, solvedColumn)){
                    System.out.println("Column " + segment * 3 + " has been swapped with column " + (segment * 3 + i));
                    return true;
                }  
            }
        }
        
        //Compares column 1 with column 2
        if (compareColumn(columnTwo, columnThree)) {
            columnTwo = solutionBoardInstance.getColumn(segment * 3 + 1);
            columnThree = unsolvedBoardInstance.getColumn(segment * 3 + 2);
            if (compareColumn(columnThree, columnTwo)) {
                System.out.println("Column " + (segment * 3 + 1) + " has been swapped with column " + (segment * 3 + 2));
                return true;
            }
        }
        return false;
    }

    //A row-segment is considered a 9x3 segment where segment 0 is row 1-3, segment 1 is row 4-6, etc.
    private boolean RowSwapInSegment(int segment) {
        ArrayList<Cell> solvedRow;
        ArrayList<Cell> unSolvedRow;
        ArrayList<Cell> rowTwo = unsolvedBoardInstance.getRow(segment * 3 + 1);
        ArrayList<Cell> rowThree = solutionBoardInstance.getRow(segment * 3 + 2);

        //Compares column 0 with column 1+2
        for (int i = 1; i < SudokuBoard.ROWS_IN_SEGMENT; i++) {
            solvedRow = solutionBoardInstance.getRow(segment * 3);
            unSolvedRow = unsolvedBoardInstance.getRow((segment * 3) + i);
            
            //Om solved row 0 matchar unsolved row 2.
            if (compareRow(solvedRow, unSolvedRow)) {
                //Matchar solved row 2 även unsolved row 0? - Annars har rad flyttats och inte swappats.
                solvedRow = solutionBoardInstance.getRow((segment * 3) + i);
                unSolvedRow = unsolvedBoardInstance.getRow(segment * 3);
                if(compareRow(solvedRow, unSolvedRow)){
                    System.out.println("Row " + segment * 3 + " has been swapped with row " + (segment * 3 + i));
                    return true;
                }
                
                
            }
        }
        //Compares row 1 with row 2
        if (compareRow(rowThree, rowTwo)) {
            rowTwo = solutionBoardInstance.getRow(segment * 3 + 1);
            rowThree = unsolvedBoardInstance.getRow(segment * 3 + 2);
            if (compareRow(rowTwo, rowThree)) {
                System.out.println("Row " + (segment * 3 + 1) + " has been swapped with row " + (segment * 3 + 2));
                return true;
            }
            
        }
        return false;
    }

    public boolean compareRow(ArrayList<Cell> solutionBoardRow, ArrayList<Cell> unSolvedRow) {
        int numHits = 0;
        int numZeros = 0;

        for (int i = 0; i < solutionBoardRow.size(); i++) {
            String solutionValue = solutionBoardRow.get(i).getValue();
            String unsolvedValue = unSolvedRow.get(i).getValue();

            if (unsolvedValue.equals("0")) {
                numZeros++;
            } else if (solutionValue.equals(unsolvedValue)) {
                numHits++;
            }
        }

        if (numHits + numZeros == COLUMNSIZE && numZeros < COLUMNSIZE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean compareColumn(ArrayList<Cell> unSolvedColumn, ArrayList<Cell> solvedColumn) {
        int numHits = 0;
        int numZeros = 0;

        for (int i = 0; i < solvedColumn.size(); i++) {
            String solutionValue = solvedColumn.get(i).getValue();
            String unsolvedValue = unSolvedColumn.get(i).getValue();

            if (unsolvedValue.equals("0")) {
                numZeros++;
            } else if (solutionValue.equals(unsolvedValue)) {
                numHits++;
            }
            //If every number is the same except the zeros, the column has been swapped.
        }
        if (numHits + numZeros == COLUMNSIZE && numZeros < COLUMNSIZE) {
            return true;
        } else {
            return false;
        }

    }

    private boolean compareColumnSegment(int solvedSegmentID, int unsolvedSegmentID) {
        ArrayList<Cell> solvedSegment = new ArrayList<>();
        ArrayList<Cell> unsolvedSegment = new ArrayList<>();

        //Adds the solved segment to be controlled
        for (int i = 1; i <= SudokuBoard.COLUMNS_IN_SEGMENT; i++) {
            for (Cell cell : solutionBoardInstance.getColumn((SudokuBoard.COLUMNS_IN_SEGMENT * solvedSegmentID))) {
                solvedSegment.add(cell);
            }

            for (Cell cell : unsolvedBoardInstance.getColumn((SudokuBoard.COLUMNS_IN_SEGMENT * unsolvedSegmentID))) {
                unsolvedSegment.add(cell);
            }
        }

        int numHits = 0;
        int numZeros = 0;

        String unsolvedValue;
        String solvedValue;

        //Compare every value in the unsolved segment with the value in the same position of the solved segment.
        for (int cellPosition = 0; cellPosition < SudokuBoard.CELLS_IN_SEGMENT; cellPosition++) {
            unsolvedValue = unsolvedSegment.get(cellPosition).getValue();
            solvedValue = solvedSegment.get(cellPosition).getValue();

            if (unsolvedValue.equals("0")) {
                numZeros++;
            } else if (solvedValue.equals(unsolvedValue)) {
                numHits++;
            }
        }

        //Controls if the segment is the same as last week or swapped with another
        if (numHits + numZeros == SudokuBoard.CELLS_IN_SEGMENT && numZeros < SudokuBoard.CELLS_IN_SEGMENT) {
            return true; 
        }
        return false;
    }

    private boolean compareRowSegment(int solvedSegmentID, int unsolvedSegmentID) {
        ArrayList<Cell> solvedSegment = new ArrayList<>();
        ArrayList<Cell> unsolvedSegment = new ArrayList<>();

        //Adds the solved segment to be controlled
        for (int i = 0; i < SudokuBoard.ROWS_IN_SEGMENT; i++) {
            for (Cell cell : solutionBoardInstance.getRow((SudokuBoard.ROWS_IN_SEGMENT * solvedSegmentID) + i)) {
                solvedSegment.add(cell);
            }

            for (Cell cell : unsolvedBoardInstance.getRow((SudokuBoard.ROWS_IN_SEGMENT * unsolvedSegmentID) + i)) {
                unsolvedSegment.add(cell);
            }
        }

        int numHits = 0;
        int numZeros = 0;

        String unsolvedValue;
        String solvedValue;

        //Compare every value in the unsolved segment with the value in the same position of the solved segment.
        for (int cellPosition = 0; cellPosition < SudokuBoard.CELLS_IN_SEGMENT; cellPosition++) {
            unsolvedValue = unsolvedSegment.get(cellPosition).getValue();
            solvedValue = solvedSegment.get(cellPosition).getValue();

            if (unsolvedValue.equals("0")) {
                numZeros++;
            } else if (solvedValue.equals(unsolvedValue)) {
                numHits++;
            }
        }

        //Controls if the segment is the same as last week or swapped with another
        if (numHits + numZeros == SudokuBoard.CELLS_IN_SEGMENT && numZeros < SudokuBoard.CELLS_IN_SEGMENT) {
            return true; 

        }
        return false;
    }

    private boolean compareClockwiseSudoku() {
        SudokuBoard CCRotatedBoard = new SudokuBoard();
        CCRotatedBoard.gameBoardMap = unsolvedBoardInstance.rotateClockWise();

        int numHits = 0;
        int numZeros = 0;

        for (int rowNumber = 0; rowNumber < SudokuBoard.MAXNUMROWS; rowNumber++) {
            ArrayList<Cell> originalRow = solutionBoardInstance.getRow(rowNumber);
            ArrayList<Cell> rotatedColumn = CCRotatedBoard.getRow((SudokuBoard.MAXNUMROWS - 1) - rowNumber);
            Collections.reverse(rotatedColumn);

            for (int i = 0; i < SudokuBoard.MAXNUMCOLUMNS; i++) {
                String originalValue = originalRow.get(i).getValue();
                String rotatedValue = rotatedColumn.get(i).getValue();

                if (rotatedValue.equals("0")) {
                    numZeros++;
                } else if (originalValue.equals(rotatedValue)) {
                    numHits++;
                }
            }

        }
        if (numHits + numZeros == CCRotatedBoard.gameBoardMap.size()) {
            System.out.println("Board has been rotated clockwise!");
            return true;
        }
        return false;
    }

    private boolean compareCounterClockwiseSudoku() {
        SudokuBoard CCWRotatedBoard = new SudokuBoard();
        CCWRotatedBoard.gameBoardMap = unsolvedBoardInstance.rotateCounterClockwise();

        int numHits = 0;
        int numZeros = 0;

        for (int rowNumber = 0; rowNumber < SudokuBoard.MAXNUMROWS; rowNumber++) {
            ArrayList<Cell> originalRow = solutionBoardInstance.getRow(rowNumber);
            ArrayList<Cell> rotatedColumn = CCWRotatedBoard.getRow((SudokuBoard.MAXNUMROWS - 1) - rowNumber);
            Collections.reverse(rotatedColumn);

            for (int i = 0; i < SudokuBoard.MAXNUMCOLUMNS; i++) {
                String originalValue = originalRow.get(i).getValue();
                String rotatedValue = rotatedColumn.get(i).getValue();

                if (rotatedValue.equals("0")) {
                    numZeros++;
                } else if (originalValue.equals(rotatedValue)) {
                    numHits++;
                }
            }

        }
        if (numHits + numZeros == CCWRotatedBoard.gameBoardMap.size()) {
            System.out.println("Board has been rotated counterclockwise!");
            return true;
        }
        return false;
    }

    //Iterates over every column in a segment and checks if any of them have been swapped
    private boolean compareColumnSwapsInSegment() {
        for (int i = 0; i < 3; i++) {
            if (ColumnSwapInSegment(i)) {
                return true;
            }
        }
        return false;
    }

    //Iterates over every row in a segment and checks if any of them have been swapped
    private boolean compareRowSwapsInSegment() {
        for (int i = 0; i < 3; i++) {
            if (RowSwapInSegment(i)) {
                return true;
            }
        }
        return false;
    }

    //Iterates over every column segment and checks if it has been swapped with another segment
    private boolean compareColumnSegments() {
       
         if ((compareColumnSegment(0, 1) && (compareColumnSegment(1, 0)))  
         || (compareColumnSegment(0, 2) && (compareColumnSegment(2, 0)))
         || (compareColumnSegment(2, 0) && (compareColumnSegment(2, 1)))
         || (compareColumnSegment(1, 2) && (compareColumnSegment(2, 1)))) {
             System.out.println("Swapped column");
            return true;
        }
        return false;

    }

    //Iterates over every row segment and checks if it has been swapped with another segment
    private boolean compareRowSegments() {
        if ((compareRowSegment(0, 1) && (compareRowSegment(1, 0)))  
         || (compareRowSegment(0, 2) && (compareRowSegment(2, 0)))
         || (compareRowSegment(2, 0) && (compareRowSegment(2, 1)))
         || (compareRowSegment(1, 2) && (compareRowSegment(2, 1)))) {
            System.out.println("Swapped row");
            return true;
        }
        return false;
    }

    private boolean checkPermutations() {

        ArrayList<Cell> unsolvedCells = new ArrayList<>();
        ArrayList<Cell> solvedCells = new ArrayList<>();

        /* Skapar ArrayLists med samtliga squares från det lösta och olösta brädet */
        for (int i = 0; i < 81; i++) {
            Map.Entry unsolvedEntry = unsolvedBoardInstance.gameBoardMap.getEntry(i);
            Map.Entry solvedEntry = solutionBoardInstance.gameBoardMap.getEntry(i);

            Cell unsolvedCell = (Cell) unsolvedEntry.getKey();
            Cell solvedCell = (Cell) solvedEntry.getKey();

            unsolvedCells.add(unsolvedCell);
            solvedCells.add(solvedCell);
        }

        // Itererar unsolvedSquares efter positioner där squareValue 1-9 befinner sig. */
        int maxHits = 0;
        for (int squareValue = 1; squareValue < 10; squareValue++) {
            //Skapar en lista över vilka positioner i det olösta brädet där squareValue befinner sig
            ArrayList<Integer> unsolvedSquarePositions = new ArrayList<>();

            for (int i = 0, n = unsolvedCells.size(); i < n; i++) {
                int unsolvedValue = Integer.parseInt(unsolvedCells.get(i).getValue());

                if (squareValue == unsolvedValue) {
                    unsolvedSquarePositions.add(i);
                }
            }

            //Itererar över positionerna i det lösta brädet där t.ex. 1 fanns i det olösta
            ArrayList<String> solvedValues = new ArrayList<>();

            if (unsolvedSquarePositions.size() > 0) {
                for (Integer unsolvedSquarePos : unsolvedSquarePositions) {
                    String solvedValue = solvedCells.get(unsolvedSquarePos).getValue();
                    
                    if (!solvedValue.equals(String.valueOf(squareValue))) {
                        solvedValues.add(solvedValue);
                    }

                }
            }

            int numHits = 0;
            int size = solvedValues.size();

            /*Om värdena på t.ex. position 1, 17 och 50 är 1 i det olösta brädet och
             värdena på position 1, 17 och 50 är 3 i det lösta brädet har med stor sannolikhet
             en funktion applicerats som adderar 2 på samtliga 1or.*/
            
            if (size > 0) {
                String solvedValue = solvedValues.get(0);
                for (int i = 1; i <= size - 1; i++) {
                    String nextSolvedValue = solvedValues.get(i);
                    if (solvedValue.equals(nextSolvedValue)) {
                        numHits++;
                    } 
                }
                if (numHits == size - 1 && numHits > 0 && size > 0) {
                    maxHits++;
                }

                    
            }

        }
        if (maxHits == 9) {
            System.out.println("String has been permuted");
            return true;
        } else {
            return false;
        }
    }
}
