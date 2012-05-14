/**
 * Fox's learning system.
 */
package foxandhounds.logic;

import java.util.Vector;

public class Fox extends LearningSystem {
    /*
     * Fox may go to up to 4 directions: up left, up right, down left,
     * down right.
     * There is also a special state when the fox has not yet chosen its initial
     * position. Its index is State.NUM_STATES - 1 and the i-th action
     * is to choose i-th column as a starting position.
     */
    public Fox(double explorationRate, double learningRate,
               double discountFactor) {
        super(explorationRate, learningRate, discountFactor);
        qValues = new double[State.NUM_STATES][4];
    }

    /**
     * Returns Q-values of given state.
     * The Q-values are given for fox moves in that order: up left, up right,
     * down left, down right.
     *
     * @param state Q-values of given state will be returned
     * @return array of Q-values, in the form described above
     */
    public synchronized double[] qValues(State state) {
        double[] result = new double[4];
        int stateIndex = state.toInt();
        Vector<State> neighbours = state.foxNeighbours(true);
        for (int resultPos = 0, qValuePos = 0; resultPos < 4; ++resultPos) {
            if (neighbours.elementAt(resultPos) == null) {
                result[resultPos] = 0;
            } else {
                result[resultPos] = qValues[stateIndex][qValuePos++];
            }
        }
        return result;
    }

    /**
     * Initialize the game.
     * The fox chooses initial position.
     *
     * @return initial game state
     */
    public State startGame() {
        return move(new State());
    }

    protected Vector<State> neighbours(State state) {
        return state.foxNeighbours(false);
    }

    /**
     * Calculates a reward given for a given state.
     *
     * @param state a state to evaluate
     * @return a reward value
     */
    protected double reward(State state) {
        if (state.foxWon()) {
            ++wins;
            return 1;
        } else if (state.houndsWon()) {
            ++losses;
            return -1;
        } else {
            return 0;
        }
    }
}
