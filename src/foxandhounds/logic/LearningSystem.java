/**
 * Abstract learning system.
 */
package foxandhounds.logic;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

abstract public class LearningSystem implements Serializable{
    protected double explorationRate;
    protected double learningRate;
    protected double discountFactor;
    protected double[][] qValues;
    protected int previousStateIndex = -1;
    protected int lastAction;
    protected long losses = 0;
    protected long wins = 0;
    protected Random random = new Random();

    private boolean[] stateVisited = new boolean[State.NUM_STATES];
    private int statesVisited = 0;
    private long turns = 0;

    public LearningSystem(double explorationRate, double learningRate,
                          double discountFactor) {
        this.explorationRate = explorationRate;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
    }

    public double getExplorationRate() {
        return explorationRate;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }

    public void setExplorationRate(double explorationRate) {
        this.explorationRate = explorationRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setDiscountFactor(double discountFactor) {
        this.discountFactor = discountFactor;
    }
    

    /**
     * Learning system execution.
     * Updates Q-values of previous move.
     * If the given state is not final, moves to a new state, otherwise only
     * updates the Q-value of previous move and gets ready for a new game.
     *
     * @param state current state before move
     * @return a state into which the learning system chooses to go, or the same
     *         state if it was final
     */
    public synchronized State move(State state) {
        ++turns;
        Vector<State> neighbours = neighbours(state);
        int stateIndex = state.toInt();
        if (!stateVisited[stateIndex]) {
            stateVisited[stateIndex] = true;
            ++statesVisited;
        }
        if (previousStateIndex >= 0) {
            double delta = (discountFactor * max(qValues[stateIndex],
                                                 neighbours.size())
                            - qValues[previousStateIndex][lastAction]
                            + reward(state)) * learningRate;
            qValues[previousStateIndex][lastAction] += delta;
        }
        if (state.isFinal()) {
            previousStateIndex = -1;
            return state;
        }
        previousStateIndex = stateIndex;
        if (random.nextDouble() < explorationRate) {
            lastAction = random.nextInt(neighbours.size());
        } else {
            lastAction = greedyAction(qValues[stateIndex], neighbours.size());
        }
        return neighbours.elementAt(lastAction);
    }

    /**
     * Returns states reachable from a given state.
     *
     * @param state current state
     * @return states reachable from a given state
     */
    abstract protected Vector<State> neighbours(State state);

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
     * Returns the largest element of a given size prefix of an array.
     *
     * @param array array in which to search for the largest value
     * @param size how many array elements to consider
     * @return the largest value
     */
    private double max(double[] array, int size) {
        double largest = array[0];
        for (int i = 0; i < size; ++i) {
            if (largest < array[i]) {
                largest = array[i];
            }
        }
        return largest;
    }

    /**
     * Get the number of states which this system has visited.
     *
     * @return how many of State.NUM_STATES states were visited by this system.
     */
    public int getStatesVisited() {
        return statesVisited;
    }

    /**
     * Get the number of times the learning system has won.
     *
     * @return the number of times the learning system has won
     */
    public long getWins() {
        return wins;
    }

    /**
     * Get the number of times the learning system has lost.
     *
     * @return the number of times the learning system has lost
     */
    public long getLosses() {
        return losses;
    }

    /**
     * Get the number of turns the learning system has made.
     *
     * @return the number of turns the learning system has made
     */
    public long getTurns() {
        return turns;
    }

    /**
     * Calculate a reward given for a given state.
     *
     * @param state a state to evaluate
     * @return a reward value
     */
    abstract protected double reward(State state);
    
}
