/**
 * Coordinates of a piece on a board.
 */
package foxandhounds.logic;

public class Coordinates implements Comparable {
    int row, column;

    Coordinates(int row, int column) {
        // TODO – validate?
        this.row = row;
        this.column = column;
    }

    Coordinates(Coordinates coordinates) {
        this.row = coordinates.row;
        this.column = coordinates.column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean equals(Coordinates other) {
        return other != null && other.row == row && other.column == column;
    }

    public int compareTo(Object o) {
        Coordinates coordinates = (Coordinates) o;
        if (row == coordinates.row) {
            return column - coordinates.column;
        } else {
            return row - coordinates.row;
        }
    }
}
