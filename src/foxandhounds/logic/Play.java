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
    private boolean isRunning = false;
    private boolean notTerminated = true;

    public Play(Fox fox, Hounds hounds) {
        this.fox = fox;
        this.hounds = hounds;
        state = fox.startGame();
    }

    public void run() {
        while (notTerminated) {
            synchronized(this) {
                while (!isRunning) {
                    try {
                        wait();
                    } catch (InterruptedException e) { }
                }
            }
            if (notTerminated) {
                step();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) { }
            }
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
            // update final Q-values and restart the game
            hounds.move(state);
            fox.move(state);
            state = fox.startGame();
            foxTurn = false;
        }
        foxTurn = !foxTurn;
    }

    /**
     * Moves a fox to given coordinate and makes hounds' move.
     *
     * @param coordinate coordinate to move the hounds, as 4 * row + column
     */
    public synchronized void step(int coordinate) {
        if (foxTurn) {
            State nextState = state.moveFox(new Coordinates(coordinate / 4,
                                                            coordinate % 4));
            for (State s : state.foxNeighbours(false)) {
                if (s.equals(nextState)) {
                    fox.move(state, nextState);
                    if (nextState.isFinal()) {
                        // update final Q-values and restart the game
                        hounds.move(nextState);
                        fox.move(nextState);
                        state = fox.startGame();
                    } else {
                        state = hounds.move(nextState);
                        if (state.isFinal()) {
                            hounds.move(state);
                            fox.move(state);
                            state = fox.startGame();
                        }
                    }
                    break;
                }
            }
        }
    }

    public synchronized void start() {
        isRunning = true;
        notifyAll();
    }

    public void stop() {
        isRunning = false;
    }

    public synchronized void terminate() {
        notTerminated = false;
        if (!isRunning) {
            start();
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
