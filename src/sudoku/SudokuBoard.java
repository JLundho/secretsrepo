/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import util.MyLinkedHashMap;

/**
 *
 * @author jonas
 */

public class SudokuBoard {
    
    public static final int MAXNUMCOLUMNS = 9;
    public static final int MAXNUMROWS = 9;
    public static final int COLUMNSIZE = 9;
    public static final int ROWSIZE = 9;
    
    public static final int MAX_NUM_SEGMENTS = 3;
    
    public static final int CELLS_IN_SEGMENT = 27;
    public static final int COLUMNS_IN_SEGMENT = 3;
    public static final int ROWS_IN_SEGMENT = 3;
    

    public MyLinkedHashMap<Cell, String> gameBoardMap;
    
    public int currentNumRows = 0;
    
    
    public SudokuBoard(){
        gameBoardMap = new MyLinkedHashMap<>();
    }

    public void addRow(String row) {
        String[] rowNumbers = new String[9];
        
        for(int i = 0; i < row.length();i++){
            rowNumbers[i] = row.substring(i, i+1);
        }
        
        //Lägger till en ny ruta i varje kolumn
        for(int colNum = 0; colNum < MAXNUMCOLUMNS; colNum++){
            Cell cell = new Cell(currentNumRows, colNum, rowNumbers[colNum]);
            gameBoardMap.put(cell, rowNumbers[colNum]);
        }
        currentNumRows++;
    }
    
    
     
    /**
     * 
     * @param rowNumber
     * @param sudokuMap
     * @return 
     * 
     * Returns rows in a zero-based way(e.g. getRow(0. sudokuMap) 
     * returns the first row in the map
     */
    
    public ArrayList<Cell> getRow(int rowNumber){
        ArrayList<Cell> resultRow = new ArrayList<Cell>();
        
        for(int i = 0; i < MAXNUMROWS;i++){
            Entry entry = gameBoardMap.getEntry((rowNumber*ROWSIZE)+i);
            Cell resultCell = (Cell)entry.getKey();
            resultRow.add(resultCell);
        }
        return resultRow;
    }
    
    public ArrayList<Cell> getRowSegment(int segmentNumber, MyLinkedHashMap<Cell,String> segmentMap){
        ArrayList<Cell> resultSegment = new ArrayList<Cell>();
        
        for(int i = 0; i < SudokuBoard.CELLS_IN_SEGMENT;i++){
            Entry entry = segmentMap.getEntry((segmentNumber*ROWSIZE)+i);
            Cell resultCell = (Cell)entry.getKey();
            resultSegment.add(resultCell);
        }
        return resultSegment;
    }
    
    public ArrayList<Cell> getColumnSegment(int segmentNumber, MyLinkedHashMap<Cell,String> segmentMap){
        ArrayList<Cell> resultSegment = new ArrayList<Cell>();
        
        int baseNumber = 9;
        for(int i = 0; i < SudokuBoard.CELLS_IN_SEGMENT;i++){
            
            Entry entry = segmentMap.getEntry(segmentNumber+(i*baseNumber));
            Cell resultCell = (Cell)entry.getKey();
            resultSegment.add(resultCell);
        }
        return resultSegment;
    }
    
    /**
     * 
     * @param colNumber
     * @param sudokuMap
     * @return 
     * 
     * Returns columns in a zero-based way(e.g. getColumn(0. sudokuMap) 
     * returns the first column in the map
     */
    
    public ArrayList<Cell> getColumn(int colNumber){
        ArrayList<Cell> resultColumn = new ArrayList<Cell>();
        
        int baseNumber = 9;
        for(int i = 0; i < MAXNUMCOLUMNS;i++){
            Entry entry = gameBoardMap.getEntry(colNumber+(i*baseNumber));
            Cell resultCell = (Cell)entry.getKey();
            resultColumn.add(resultCell);
        }
        return resultColumn;
    }
    

    public MyLinkedHashMap<Cell,String> rotateCounterClockwise () {
        MyLinkedHashMap<Cell,String> CCWmap = new MyLinkedHashMap<>();
        
        //FIXA TILL SENARE! - Stämmer kolumnen?
        int counter = 0;
        int baseNumber = 8;
        for (Cell cell : gameBoardMap.keySet()) {
            if(counter == 9){
               baseNumber--;
               counter = 0;
            }
            
            int rotatedCellRow = (MAXNUMROWS-1)-cell.getCol();
            int rotatedCellCol = 0;
            String value = gameBoardMap.getValue(baseNumber+(counter*9));
            
            Cell rotatedCell = new Cell(rotatedCellRow, rotatedCellCol, value);
                    
            CCWmap.put(rotatedCell, value);
            counter++;
       }
        return CCWmap;
    }
    
    public MyLinkedHashMap<Cell,String> rotateClockWise () {
        MyLinkedHashMap<Cell,String> CWmap = new MyLinkedHashMap<>();
        
        for(int i = 0; i < MAXNUMCOLUMNS;i++){
            ArrayList<Cell> resultSquare = getColumn(i);
            Collections.reverse(resultSquare);
            
            for (Cell cell : resultSquare) {
                Cell rotatedCell = new Cell(cell.getRow(), cell.getCol(), cell.getValue());
                CWmap.put(rotatedCell, cell.getValue());
            }
        }

        return CWmap;
    }

    public void printBoard(){
        int counter = 1;
        for (Cell cell : gameBoardMap.keySet()) {
            System.out.print(cell.getValue());
            if(counter%9 == 0){
                System.out.println("");
            }
            counter++;
        }
    }
}
