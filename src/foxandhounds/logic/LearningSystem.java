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

    /**
     * Encodes state as an int in range [0; 32! / 27! / 4!)
     *
     * @param state state to be encoded
     * @return int representing a given state
     */
    private int stateToInt(State state) {
        int intState = 0;
        Coordinates[] hounds = state.getHounds();
        for (int i = 0; i < 4; ++i) {
            intState *= 32 - i - 1;
            intState += hounds[i].getRow() * 4 + hounds[i].getColumn() - i;
        }
        Coordinates fox = state.getFox();
        intState *= 28;
        intState += fox.getRow() * 4 + fox.getColumn();
        return intState;
    }

    abstract public State move(State state);
}
