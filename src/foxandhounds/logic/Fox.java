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
    private double[][] qValues = new double[LearningSystem.numStates + 1][4];

    public Fox(double explorationRate, double learningRate,
               double discountFactor) {
        super(explorationRate, learningRate, discountFactor);
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

    public State move(State state) {
        int stateIndex;
        Vector<State> neighbours;
        if (state.isFinal()) {
            // restart the game – choose starting column
            stateIndex = LearningSystem.numStates;
            neighbours = new Vector<State>();
            for (int i = 0; i < 4; ++i) {
                neighbours.add(new State(i));
            }
        } else {
            stateIndex = state.toInt();
            neighbours = state.foxNeighbours(false);
        }

        int action;
        if (random.nextDouble() < explorationRate) {
            action = random.nextInt() % neighbours.size();
        } else {
            action = greedyAction(qValues[stateIndex]);
        }
        int nextStateIndex = neighbours.elementAt(action).toInt();
        double delta = (discountFactor * qMax(qValues[nextStateIndex])
                        - qValues[stateIndex][action]) * learningRate;
        qValues[LearningSystem.numStates][action] += delta;
        return neighbours.elementAt(action);
    }
}
