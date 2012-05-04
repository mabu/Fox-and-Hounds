/**
 * Hounds learning system.
 */
package foxandhounds.logic;

public class Hounds extends LearningSystem {
    private double[][] qValues = new double[LearningSystem.numStates][4][2];

    public State move(State state) {
        return state;
    }

    public Hounds(double explorationRate, double learningRate,
                  double discountFactor) {
        super(explorationRate, learningRate, discountFactor);
    }

    public double[][] qValues(State state) {
        return qValues[stateToInt(state)];
    }
}
