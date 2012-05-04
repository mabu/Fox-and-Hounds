/**
 * Abstract learning system.
 */
package foxandhounds.logic;

import java.util.Random;

abstract public class LearningSystem {
    protected static final int numStates = 32 * 31 * 30 * 29 * 28 / 4 / 3 / 2;
    protected double explorationRate;
    protected double learningRate;
    protected double discountFactor;
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

    abstract public State move(State state);

    protected int greedyAction(double[] qValues) {
        int action = 0;
        for (int i = 1; i < qValues.length; ++i) {
            if (qValues[i] < qValues[action]) {
                action = i;
            }
        }
        return action;
    }

    protected int qMax(double[] qValues) {
        int action = 0;
        for (int i = 1; i < qValues.length; ++i) {
            if (qValues[i] < qValues[action]) {
                action = i;
            }
        }
        return action;
    }
}
