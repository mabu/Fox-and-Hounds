/**
 * State of the board.
 */
package foxandhounds.logic;

import java.util.Vector;
import java.util.Arrays;

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
     * Copy constructor.
     */
    private State(State state) {
        fox = new Coordinates(state.fox);
        for (int i = 0; i < 4; ++i) {
            hounds[i] = new Coordinates(state.hounds[i]);
        }
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
     * Get neighbour states for fox's moves.
     *
     * @param includeNulls if true, adds null elements for illegal actions, to
     *                     always return 4 elements
     * @return a vector of possible states after fox's move, in order: up left,
     *         up righ, down left, down right
     */
    public Vector<State> foxNeighbours(boolean includeNulls) {
        Vector<State> neighbours = new Vector<State>();
        Coordinates moved;
        // going up
        moved = new Coordinates(fox.getRow() + 1, fox.getColumn());
        State sameColumnState = (isHoundAt(moved) ? null : moveFox(moved));
        if (fox.getRow() % 2 == 0) {
            moved = new Coordinates(fox.getRow() + 1, fox.getColumn() - 1);
            if (fox.getColumn() > 0 && !isHoundAt(moved)) {
                neighbours.add(moveFox(moved));
            } else if (includeNulls) {
                neighbours.add(null);
            }
            if (sameColumnState != null || includeNulls) {
                neighbours.add(sameColumnState);
            }
        } else {
            if (sameColumnState != null || includeNulls) {
                neighbours.add(sameColumnState);
            }
            moved = new Coordinates(fox.getRow() + 1, fox.getColumn() + 1);
            if (fox.getColumn() < 3 && !isHoundAt(moved)) {
                neighbours.add(moveFox(moved));
            } else if (includeNulls) {
                neighbours.add(null);
            }
        }
        // going down
        moved = new Coordinates(fox.getRow() - 1, fox.getColumn());
        sameColumnState = (isHoundAt(moved) ? null : moveFox(moved));
        if (fox.getRow() > 0) {
            if (fox.getRow() % 2 == 0) {
                moved = new Coordinates(fox.getRow() - 1, fox.getColumn() - 1);
                if (fox.getColumn() > 0 && !isHoundAt(moved)) {
                    neighbours.add(moveFox(moved));
                } else if (includeNulls) {
                    neighbours.add(null);
                }
                if (sameColumnState != null || includeNulls) {
                    neighbours.add(sameColumnState);
                }
            } else {
                if (sameColumnState != null || includeNulls) {
                    neighbours.add(sameColumnState);
                }
                moved = new Coordinates(fox.getRow() - 1, fox.getColumn() + 1);
                if (fox.getColumn() < 3 && !isHoundAt(moved)) {
                    neighbours.add(moveFox(moved));
                }
            }
        }
        return neighbours;
    }

    /**
     * Get neighbour states for hounds' moves.
     *
     * @param includeNulls if true, adds null elements for illegal actions, to
     *                     always return 8 elements
     * @return a vector of possible states after hound's move.
     */
    public Vector<State> houndsNeighbours(boolean includeNulls) {
        Vector<State> neighbours = new Vector<State>();
        for (int i = 0; i < 4; ++i) {
            Coordinates moved;
            if (hounds[i].getRow() > 0) {
                moved = new Coordinates(hounds[i].getRow() - 1,
                                        hounds[i].getColumn());
                if (!isHoundAt(moved) && !moved.equals(fox)) {
                    neighbours.add(moveHound(i, moved));
                } else if (includeNulls) {
                    neighbours.add(null);
                }
                if (hounds[i].getRow() % 2 == 1) {
                    if (hounds[i].getColumn() < 3) {
                        moved = new Coordinates(hounds[i].getRow() - 1,
                                                hounds[i].getColumn() + 1);
                        if (!isHoundAt(moved) && !moved.equals(fox)) {
                            neighbours.add(moveHound(i, moved));
                        } else if (includeNulls) {
                            neighbours.add(null);
                        }
                    }
                } else {
                    if (hounds[i].getColumn() > 0) {
                        moved = new Coordinates(hounds[i].getRow() - 1,
                                                hounds[i].getColumn() - 1);
                        if (!isHoundAt(moved) && !moved.equals(fox)) {
                            neighbours.add(moveHound(i, moved));
                        } else if (includeNulls) {
                            neighbours.add(null);
                        }
                    }
                }
            } else if (includeNulls) {
                neighbours.add(null);
                neighbours.add(null);
            }
        }
        return neighbours;
    }

    /**
     * Creates a new state with fox moved to given coordinates.
     *
     * @param coordinates where to move fox
     * @return a new state with fox moved to given coordinates
     */
    private State moveFox(Coordinates coordinates) {
        State state = new State(this);
        state.fox = coordinates;
        return state;
    }

    /**
     * Creates a new state with a hound moved to given coordinates.
     *
     * @param houndIndex which hound to move ([0, 3])
     * @param coordinates where to move the hound
     * @return a new state with fox moved to given coordinates
     */
    private State moveHound(int houndIndex, Coordinates coordinates) {
        State state = new State(this);
        state.hounds[houndIndex] = coordinates;
        Arrays.sort(state.hounds);
        return state;
    }

    /**
     * Checks if this state represent fox's victory.
     *
     * @return true if this state represent fox's victory, false otherwise
     */
    public boolean foxWon() {
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
    public boolean houndsWon() {
        return foxNeighbours(false).size() == 0;
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

    /**
     * Encodes state as an int in range [0; 32! / 27! / 4!)
     *
     * @return int representing a given state
     */
    public int toInt() {
        int intState = 0;
        for (int i = 0; i < 4; ++i) {
            intState *= 32 - i - 1;
            intState += hounds[i].getRow() * 4 + hounds[i].getColumn() - i;
        }
        intState *= 28;
        intState += fox.getRow() * 4 + fox.getColumn();
        return intState;
    }
}
