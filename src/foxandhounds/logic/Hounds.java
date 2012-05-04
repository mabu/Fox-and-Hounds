/**
 * Hounds learning system.
 */
package foxandhounds.logic;

import java.util.Vector;

public class Hounds extends LearningSystem {
    /*
     * During a move, hounds have to choose one of 4 hounds and move it to
     * one of up to 2 directions, which gives a maximum of 8 actions from any
     * given state.
     */
    public Hounds(double explorationRate, double learningRate,
                  double discountFactor) {
        super(explorationRate, learningRate, discountFactor);
        qValues = new double[LearningSystem.numStates][8];
    }

    /**
     * Returns Q-values of given state.
     * i-th element has a pair of Q-values for moving i-th hound as given by
     * State.getHounds() to the left and to the right, in that order.
     *
     * @param state Q-values of given state will be returned
     * @return two-dimensional array of Q-values, in the form described above
     */
    public double[][] qValues(State state) {
        double[][] result = new double[4][2];
        int stateIndex = state.toInt();
        Vector<State> neighbours = state.houndsNeighbours(true);
        for (int resultPos = 0, qValuePos = 0; resultPos < 8; ++resultPos) {
            if (neighbours.elementAt(resultPos) == null) {
                result[resultPos / 2][resultPos % 2] = 0;
            } else {
                result[resultPos / 2][resultPos % 2]
                                             = qValues[stateIndex][qValuePos++];
            }
        }
        return result;
    }

    /**
     * Learning system execution.
     * Moves from a given state to a new state, updating Q-values.
     *
     * @param state initial state
     * @return a state into which the learning system chooses to go
     */
    public State move(State state) {
        Vector<State> neighbours = state.houndsNeighbours(false);
        if (neighbours.size() == 0) {
            // FIXME: what to do, if no hound can move?
            return state;
        }
        return super.move(state.toInt(), neighbours);
    }

    /**
     * Calculates a reward given for a given state.
     *
     * @param state a state to evaluate
     * @return a reward value
     */
    protected double reward(State state) {
        if (state.houndsWon()) {
            return 1;
        } else if (state.foxWon()) {
            return -1;
        } else {
            return 0;
        }
    }
}
