/**
 * This class controls the play of the game.
 */
package foxandhounds.logic;

public class Play {
    private State state;
    private Fox fox;
    private Hounds hounds;
    private boolean foxTurn = true;

    public Play(Fox fox, Hounds hounds) {
        this.fox = fox;
        this.hounds = hounds;
        state = fox.startGame();
    }

    public State getState() {
        return state;
    }

    public void step() {
        if (foxTurn) {
            state = fox.move(state);
        } else {
            state = hounds.move(state);
        }
        if (state.isFinal()) {
            // update final Q-values and restart the game
            hounds.move(state);
            fox.move(state);
            state = fox.startGame();
            foxTurn = false;
        }
        foxTurn = !foxTurn;
    }
}
