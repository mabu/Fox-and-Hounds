/**
 * Coordinates of a piece on a board.
 */
package foxandhounds.logic;

public class Coordinates {
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
        return other.row == row && other.column == column;
    }
}
