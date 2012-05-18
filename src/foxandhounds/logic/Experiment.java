package foxandhounds.logic;

import java.io.PrintStream;
import java.io.FileNotFoundException;

/**
 * Runs an experiment.
 *
 * The experiment is run in two alternating phases: a training phase and an
 * evaluation phase. The training phase goes for a given number of turns.
 * During the evaluation phase, exploration and learning rates are set to 0 and
 * a given number of games are played.
 */
public class Experiment implements Runnable {
    private Fox fox;
    private Hounds hounds;
    private State state;
    private long trainingTurns;
    private long evaluationGames;
    private int cyclesLeft;
    private PrintStream out;
    private boolean isRunning = false;

    /**
     * Initializes an experiment.
     *
     * @param fox a fox to experiment with
     * @param hounds hounds to experiment with
     * @param trainingTurns the number of turns per training phase
     * @param evaluationGames the number of games played to evaluate
     */
    public Experiment(Fox fox, Hounds hounds, long trainingTurns,
                      long evaluationGames) {
        this.fox = fox;
        this.hounds = hounds;
        this.trainingTurns = trainingTurns;
        this.evaluationGames = evaluationGames;
        state = fox.startGame();
    }

    /**
     * Runs some cycles of an experiment.
     * 
     * Each cycle consists of a training phase followed by an evaluation phase.
     * The results are printed as the number of fox and hounds wins separated
     * by a single space, one line per experiment.
     *
     * @param cycles the number of cycles to run the experiment
     * @param out output stream
     * @return the thread which runs the experiment
     */
    public synchronized Thread run(int cycles, PrintStream out) {
        cyclesLeft = cycles;
        this.out = out;
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }

    public synchronized void run() {
        isRunning = true;
        while (cyclesLeft-- > 0) {
            for (int i = 0; i < trainingTurns; ++i) {
                state = step(fox, hounds, state);
            }
            Fox evaluationFox = (Fox)fox.clone();
            Hounds evaluationHounds = (Hounds)hounds.clone();
            evaluationFox.setLearningRate(0);
            evaluationFox.setExplorationRate(0);
            evaluationHounds.setLearningRate(0);
            evaluationHounds.setExplorationRate(0);
            State evaluationState = evaluationFox.startGame();
            long initFoxWins = evaluationFox.getWins();
            long initHoundsWins = evaluationHounds.getWins();
            while (evaluationFox.getWins() + evaluationHounds.getWins()
                   - initFoxWins - initHoundsWins < evaluationGames) {
                evaluationState = step(evaluationFox, evaluationHounds,
                                       evaluationState);
            }
            out.println((evaluationFox.getWins() - initFoxWins) + " "
                        + (evaluationHounds.getWins() - initHoundsWins));
        }
        isRunning = false;
    }

    private State step(Fox fox, Hounds hounds, State state) {
        if (state.foxTurn()) {
            state = fox.move(state);
        } else {
            state = hounds.move(state);
        }
        if (state.isFinal()) {
            // update final Q-values and restart the game
            hounds.move(state);
            fox.move(state);
            state = fox.startGame();
        }
        return state;
    }

    /**
     * In this experiment we train two foxes and two instances of hounds with
     * different learning rates against random opponents. Then we make them play
     * against each other.
     * 
     * @param cycles the number of cycles for the experiments
     */
    private static void experiment1(int cycles) {
        Fox fox1 = new Fox(0.1, 0.5, 0.99);
        Fox fox2 = new Fox(0.1, 0.3, 0.99);
        Fox randomFox1 = new Fox(0, 0, 0);
        Fox randomFox2 = new Fox(0, 0, 0);
        Hounds hounds1 = new Hounds(0.1, 0.5, 0.99);
        Hounds hounds2 = new Hounds(0.1, 0.3, 0.99);
        Hounds randomHounds1 = new Hounds(0, 0, 0);
        Hounds randomHounds2 = new Hounds(0, 0, 0);

        try {
            Experiment e1 = new Experiment(fox1, randomHounds1, 100000, 100);
            Thread t1 = e1.run(cycles,
                               new PrintStream("experiment1/fox1VSrandom"));
            Experiment e2 = new Experiment(fox2, randomHounds2, 100000, 100);
            Thread t2 = e2.run(cycles,
                               new PrintStream("experiment1/fox2VSrandom"));
            Experiment e3 = new Experiment(randomFox1, hounds1, 100000, 100);
            Thread t3 = e3.run(cycles,
                               new PrintStream("experiment1/hounds1VSrandom"));
            Experiment e4 = new Experiment(randomFox2, hounds2, 100000, 100);
            Thread t4 = e4.run(cycles,
                               new PrintStream("experiment1/hounds2VSrandom"));
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            System.out.println("Done training.");
            Experiment e5 = new Experiment(fox1, hounds1, 100000, 100);
            Thread t5 = e5.run(cycles,
                               new PrintStream("experiment1/fox1VShounds1"));
            Experiment e6 = new Experiment(fox2, hounds2, 100000, 100);
            Thread t6 = e6.run(cycles,
                               new PrintStream("experiment1/fox2VShounds2"));
            t5.join();
            t6.join();
            System.out.println("Done first round.");
            Experiment e7 = new Experiment(fox1, hounds2, 100000, 100);
            Thread t7 = e7.run(cycles,
                               new PrintStream("experiment1/fox1VShounds2"));
            Experiment e8 = new Experiment(fox2, hounds1, 100000, 100);
            Thread t8 = e8.run(cycles,
                               new PrintStream("experiment1/fox2VShounds1"));
            t7.join();
            t8.join();
            System.out.println("Done second round.");
        }
        catch (FileNotFoundException ignored) { }
        catch (InterruptedException ignored) { }
    }

    public static void main(String args[]) {
        experiment1(100);
    }
}
