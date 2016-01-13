# secretsudoku
Run the program by importing it into an IDE of your choice, then simply create a jar and run it.

Smaller program that takes in a number of sudoku-boards (first line-input).
Afterwards the user fills the terminal with 18 lines (9 lines first lines representing a solved board, next 9 lines representing an unsolved and finally a space.

The program will find out if:
* A puzzle has been rotated puzzle clockwise or counterclockwise.
* If two columns have been swapped within a 3×93×9 column segment.
* If two rows have been swapped within a 9×39×3 row segment.
* If entire row/column segments have been swapped
* If a permutation has occurred where x is replaced by replaced by f(x) in every cell.

Sample input:

1

963174258

178325649

254689731

821437596

496852317

735961824

589713462

317246985

642598173

060104050

200000001

008305600

800407006

006000300

700901004

500000002

040508070

007206900

Sample output: 

Yes

