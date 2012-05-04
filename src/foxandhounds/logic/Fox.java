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
     * position. Its index is LearningSystem.numStates and the i-th action
     * is to choose i-th column as a starting position.
     */
    public Fox(double explorationRate, double learningRate,
               double discountFactor) {
        super(explorationRate, learningRate, discountFactor);
        qValues = new double[LearningSystem.numStates + 1][4];
    }

    /**
     * Returns Q-values of given state.
     * The Q-values are given for fox moves in that order: up left, up right,
     * down left, down right.
     *
     * @param state Q-values of given state will be returned
     * @return array of Q-values, in the form described above
     */
    public double[] qValues(State state) {
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
        Vector<State> neighbours = new Vector<State>();
        for (int i = 0; i < 4; ++i) {
            neighbours.add(new State(i));
        }
        return super.move(LearningSystem.numStates, neighbours);
    }

    /**
     * Learning system execution.
     * Moves from a given state to a new state, updating Q-values.
     *
     * @param state initial state
     * @return a state into which the learning system chooses to go
     */
    public State move(State state) {
        return super.move(state.toInt(), state.foxNeighbours(false));
    }

    /**
     * Calculates a reward given for a given state.
     *
     * @param state a state to evaluate
     * @return a reward value
     */
    protected double reward(State state) {
        if (state.foxWon()) {
            return 1;
        } else if (state.houndsWon()) {
            return -1;
        } else {
            return 0;
        }
    }
}
