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

    public static final int NUM_STATES = 32 * 31 * 30 * 29 * 14 / 4 / 3 / 2 + 1;

    private Coordinates fox = null;
    private Coordinates[] hounds = new Coordinates[4];

    /**
     * Initial game state.
     * Fox not on the board yet.
     */
    public State() {
        for (int i = 0; i < 4; ++i) {
            hounds[i] = new Coordinates(7, i);
        }
    }

    /**
     * Initial game state.
     * @param foxColumn column for initial fox position (between 0 and 3)
     */
    public State(int foxColumn) {
        this();
        fox = new Coordinates(0, foxColumn);
    }

    /**
     * Copy constructor.
     */
    private State(State state) {
        if (state.fox != null) {
            fox = new Coordinates(state.fox);
        }
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
        if (fox == null) {
            for (int i = 0; i < 4; ++i) {
                neighbours.add(new State(i));
            }
            return neighbours;
        }
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
                    } else if (includeNulls) {
                        neighbours.add(null);
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
                    } else if (includeNulls) {
                        neighbours.add(null);
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
    public State moveFox(Coordinates coordinates) {
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
        if (fox == null) {
            return false;
        }
        if (houndsNeighbours(false).size() == 0) {
            return true;
        }
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

    private static int[][] C = new int[33][5]; // number of combinations
    static {
        for (int i = 0; i < 33; ++i) {
            C[i][0] = 1;
        }
        for (int i = 1; i < 33; ++i) {
            for (int j = 1; j < 5; ++j) {
                C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
            }
        }
    }

    /**
     * Encodes state as an int in range [0; 32! / 27! / 4!]
     *
     * @return int representing a given state
     */
    public int toInt() {
        if (fox == null) {
            return NUM_STATES - 1;
        }
        int intState = 0;
        int foxCoordinate = fox.getRow() / 2 * 4 + fox.getColumn();
        for (int i = 0; i < 4; ++i) {
            intState += C[hounds[i].getRow() * 4 + hounds[i].getColumn()][i + 1];
            if (fox.compareTo(hounds[i]) > 0
                && (fox.getRow() - hounds[i].getRow()) % 2 == 0) {
                --foxCoordinate;
            }
        }
        return intState * 14 + foxCoordinate;
    }

    /**
     * Determines if it is a fox's turn based on positions of pieces.
     *
     * @return true, if it is fox's turn, false otherwise
     */
    public boolean foxTurn() {
        if (fox == null) {
            return true;
        }
        int sum = fox.getRow();
        for (Coordinates hound : hounds) {
            sum += hound.getRow();
        }
        return sum % 2 == 0;
    }

    public boolean equals(State state) {
        for (int i = 0; i < 4; ++i) {
            if (!hounds[i].equals(state.hounds[i])) {
                return false;
            }
        }
        return fox.equals(state.fox);
    }
}
