/**
 * Abstract learning system.
 */
package foxandhounds.logic;

abstract public class LearningSystem {
    protected double explorationRate;
    protected double learningRate;
    protected static final int numStates = 32 * 31 * 30 * 29 * 28 / 4 / 3 / 2;

    public LearningSystem(double explorationRate, double learningRate) {
        this.explorationRate = explorationRate;
        this.learningRate = learningRate;
    }

    public void setExplorationRate(double explorationRate) {
        this.explorationRate = explorationRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = explorationRate;
    }

    abstract public State move(State state);
}
