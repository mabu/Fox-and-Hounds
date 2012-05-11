/**
 * This class controls the play of the game.
 */
package foxandhounds.logic;

public class Play implements Runnable {
    private State state;
    private Fox fox;
    private Hounds hounds;
    private boolean foxTurn = true;
    private int delay = 500; // delay between moves, in milliseconds
    private int foxWins = 0;
    private int houndsWins = 0;
    private int steps = 0;
    private boolean isRunning = false;

    public Play(Fox fox, Hounds hounds) {
        this.fox = fox;
        this.hounds = hounds;
        state = fox.startGame();
    }

    public void run() {
        while (true) {
            synchronized(this) {
                while (!isRunning) {
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                }
            }
            step();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) { }
        }
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized void step() {
        if (foxTurn) {
            state = fox.move(state);
        } else {
            state = hounds.move(state);
        }
        if (state.isFinal()) {
            if (state.foxWon()) {
                ++foxWins;
            } else {
                ++houndsWins;
            }
            // update final Q-values and restart the game
            hounds.move(state);
            fox.move(state);
            state = fox.startGame();
            foxTurn = false;
        }
        foxTurn = !foxTurn;
        ++steps;
    }

    public synchronized void start() {
        isRunning = true;
        notifyAll();
    }

    public void stop() {
        isRunning = false;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getFoxWins() {
        return foxWins;
    }

    public int getHoundsWins() {
        return houndsWins;
    }

    public int getSteps() {
        return steps;
    }
}
