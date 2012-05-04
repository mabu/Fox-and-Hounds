/**
 * Fox's learning system.
 */
package foxandhounds.logic;

public class Fox extends LearningSystem {
    private double[][] qValues = new double[LearningSystem.numStates][4];

    public State move(State state) {
        return state;
    }

    public Fox(double explorationRate, double learningRate) {
        super(explorationRate, learningRate);
    }
}
