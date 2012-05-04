/**
 * Abstract learning system.
 */
package foxandhounds.logic;

import java.util.Random;
import java.util.Vector;

abstract public class LearningSystem {
    protected static final int numStates = 32 * 31 * 30 * 29 * 28 / 4 / 3 / 2;
    protected double explorationRate;
    protected double learningRate;
    protected double discountFactor;
    protected double[][] qValues;
    protected Random random = new Random();

    public LearningSystem(double explorationRate, double learningRate,
                          double discountFactor) {
        this.explorationRate = explorationRate;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
    }

    public void setExplorationRate(double explorationRate) {
        this.explorationRate = explorationRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setDiscountFactor(double discountFactor) {
        this.discountFactor = explorationRate;
    }

    /**
     * Learning system execution.
     * Moves from a given state to a new state, updating Q-values.
     *
     * @param stateIndex the index of the current state in the table of Q-values
     * @param neighbours states to which a move can be made
     * @return a state into which the learning system chooses to go
     */
    protected State move(int stateIndex, Vector<State> neighbours) {
        int action;
        if (random.nextDouble() < explorationRate) {
            action = random.nextInt(neighbours.size());
        } else {
            action = greedyAction(qValues[stateIndex], neighbours.size());
        }
        State nextState = neighbours.elementAt(action);
        int nextStateIndex = nextState.toInt();
        double delta = (discountFactor * qMax(qValues[nextStateIndex])
                        - qValues[stateIndex][action]
                        + reward(nextState)) * learningRate;
        qValues[stateIndex][action] += delta;
        return nextState;
    }

    /**
     * Greedy choice of an action.
     *
     * @param qValues array of Q-values to choose from
     * @param size    only consider Q-values with indices from 0 to size - 1
     * @return index of the largest element of a given size prefix of qValues;
     *         if there are more than one largest value, chooses one of them
     *         randomly
     */
    protected int greedyAction(double[] qValues, int size) {
        double max = qValues[0]; // largest Q-value found so far
        int actions = 1;         // how many actions with largest Q-value
        for (int i = 1; i < size; ++i) {
            if (max < qValues[i]) {
                max = qValues[i];
                actions = 1;
            } else if (max == qValues[i]) {
                ++actions;
            }
        }
        int which = 1 + random.nextInt(actions);
        int action = -1;
        while (which > 0) {
            if (qValues[++action] == max) {
                --which;
            }
        }
        return action;
    }

    /**
     * Returns the index of the largest element of array (of Q-values).
     *
     * @param qValues array (of Q-values) to search for the largest value
     * @return the index of the largest element (Q-value)
     */
    protected int qMax(double[] qValues) {
        int action = 0;
        for (int i = 1; i < qValues.length; ++i) {
            if (qValues[i] < qValues[action]) {
                action = i;
            }
        }
        return action;
    }

    /**
     * Calculate a reward given for a given state.
     *
     * @param state a state to evaluate
     * @return a reward value
     */
    abstract protected double reward(State state);
}
