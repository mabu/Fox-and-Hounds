/**
 * Hounds learning system.
 */
package foxandhounds.logic;

public class Hounds extends LearningSystem {
    private double[][] qValues = new double[LearningSystem.numStates][8];

    public State move(State state) {
        return state;
    }

    public Hounds(double explorationRate, double learningRate) {
        super(explorationRate, learningRate);
    }
}
