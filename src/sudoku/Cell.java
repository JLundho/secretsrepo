/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudoku;

/**
 *
 * @author jonas
 */
public class Cell implements Comparable<Cell>{

  private int row;
  private int col;
  private String value;

  public Cell(int row, int col, String value) {
      this.row = row;
      this.col = col;
      this.value = value;
  }

  public String getValue() {
      return value;
  }

  public void setValue(String value) {
      this.value = value;
  }

  public int getRow() {
      return row;
  }

  public void setRow(int row) {
      this.row = row;
  }

  public int getCol() {
      return col;
  }

  public void setCol(int col) {
      this.col = col;
  }
  
  
  // Ha kvar?
  @Override
  public int compareTo(Cell compareCell) {
        if(this.col > compareCell.col) {
            return this.col;
        } else if (this.col < compareCell.col){
            return compareCell.col;
        } else if(this.row > compareCell.row) {
            return this.row;
        } else if (this.row < compareCell.row){
            return compareCell.row;
        } 
        return 0;
  }
  
}


