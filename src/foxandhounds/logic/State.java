/**
 * State of the board.
 */
package foxandhounds.logic;

public class State {
    /*
     * Position is represented as row (from 0 to 7) and column (from 0 to 3).
     * Fox starts at a chosen column in row 0, hounds start in all columns of
     * row 7.
     */

    private Coordinates fox;
    private Coordinates[] hounds = new Coordinates[4];

    /**
     * Initial game state.
     *
     * @param foxColumn column for initial fox position (between 0 and 3)
     */
    public State(int foxColumn) {
        for (int i = 0; i < 4; ++i) {
            hounds[i] = new Coordinates(7, i);
        }
        fox = new Coordinates(0, foxColumn);
    }

    /**
     * Checks if a game is already over.
     *
     * @return true, if this is a final state, false otherwise
     */
    public boolean isFinal() {
        return foxWon() || houndsWon();
    }

    /**
     * Checks if this state represent fox's victory.
     *
     * @return true if this state represent fox's victory, false otherwise
     */
    private boolean foxWon() {
        for (Coordinates hound : hounds) {
            if (hound.getRow() > fox.getRow()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this state represent hounds' victory.
     *
     * @return true if this state represent hounds' victory, false otherwise
     */
    private boolean houndsWon() {
        if (fox.getRow() > 6) {
            return false;
        }
        if (!isHoundAt(new Coordinates(fox.getRow() - 1, fox.getColumn()))) {
            return false;
        }
        // going up
        if (fox.getRow() % 2 == 1) {
            if (fox.getColumn() < 3
                && !isHoundAt(new Coordinates(fox.getRow() + 1,
                                              fox.getColumn() + 1))) {
                return false;
            }
        } else {
            if (fox.getColumn() > 0
                && !isHoundAt(new Coordinates(fox.getRow() + 1,
                                              fox.getColumn() - 1))) {
                return false;
            }
        }
        if (fox.getRow() == 0) {
            return true;
        }
        // going down
        if (fox.getRow() % 2 == 1) {
            if (fox.getColumn() < 3
                && !isHoundAt(new Coordinates(fox.getRow() - 1,
                                              fox.getColumn() + 1))) {
                return false;
            }
        } else {
            if (fox.getColumn() > 0
                && !isHoundAt(new Coordinates(fox.getRow() - 1,
                                              fox.getColumn() - 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a state after moving a fox to a certain direction.
     *
     * @param direction direction to move
     * @return a state after the move, or null if the move is illegal
     */

    private boolean isHoundAt(Coordinates coordinates) {
        for (Coordinates hound : hounds) {
            if (hound.equals(coordinates)) {
                return true;
            }
        }
        return false;
    }

    public Coordinates getFox() {
        return fox;
    }

    public Coordinates[] getHounds() {
        return hounds;
    }
}
