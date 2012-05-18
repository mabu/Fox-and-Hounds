package foxandhounds.logic;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Smart fox's learning system.
 *
 * The better it plays, the less it explores.
 */
public class SmartFox extends Fox implements Serializable, Cloneable {
    private static final int RECENT_RESULTS_LENGTH = 100;
    private LinkedList<Boolean> recentResults = new LinkedList<Boolean>();
    private int recentWins;

    public SmartFox(double explorationRate, double learningRate,
               double discountFactor) {
        super(explorationRate, learningRate, discountFactor);
    }

    /**
     * Calculates a reward given for a given state.
     *
     * @param state a state to evaluate
     * @return a reward value
     */
    protected double reward(State state) {
        if (state.houndsWon()) {
            ++losses;
            recentResults.offer(false);
            updateExplorationRate();
            return -1;
        } else if (state.foxWon()) {
            ++wins;
            ++recentWins;
            recentResults.offer(true);
            updateExplorationRate();
            return 1;
        } else {
            return 0;
        }
    }

    private void updateExplorationRate() {
        if (recentResults.size() > RECENT_RESULTS_LENGTH) {
            if (recentResults.poll()) {
                --recentWins;
            }
        }
        setExplorationRate(0.5 - wins / (double)recentResults.size());
    }

    public Object clone() {
        return super.clone();
    }
}
