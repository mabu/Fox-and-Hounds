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
    private int cycles;
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
        this.cycles = cycles;
        this.out = out;
        Thread thread = new Thread(this);
        thread.start();
        return thread;
    }

    public synchronized void run() {
        isRunning = true;
        for (int cycle = 1; cycle <= cycles; ++cycle) {
            for (int i = 0; i < trainingTurns; ++i) {
                state = step(fox, hounds, state);
            }
            Fox evaluationFox = (Fox)fox.clone();
            Hounds evaluationHounds = (Hounds)hounds.clone();
            evaluationFox.setLearningRate(0);
            evaluationHounds.setLearningRate(0);
            State evaluationState = evaluationFox.startGame();
            // evaluate fox
            long initFoxWins = evaluationFox.getWins();
            long initHoundsWins = evaluationHounds.getWins();
            evaluationFox.setExplorationRate(0);
            while (evaluationFox.getWins() + evaluationHounds.getWins()
                   - initFoxWins - initHoundsWins < evaluationGames) {
                evaluationState = step(evaluationFox, evaluationHounds,
                                       evaluationState);
            }
            out.print(cycle);
            out.print(" " + (evaluationFox.getWins() - initFoxWins));
            // evaluate hounds
            initFoxWins = evaluationFox.getWins();
            initHoundsWins = evaluationHounds.getWins();
            evaluationFox.setExplorationRate(fox.getExplorationRate());
            evaluationHounds.setExplorationRate(0);
            while (evaluationFox.getWins() + evaluationHounds.getWins()
                   - initFoxWins - initHoundsWins < evaluationGames) {
                evaluationState = step(evaluationFox, evaluationHounds,
                                       evaluationState);
            }
            out.println(" " + (evaluationHounds.getWins() - initHoundsWins)
                        + " " + fox.getStatesVisited()
                        + " " + hounds.getStatesVisited());
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
     * In this experiment two random learning systems play against each other.
     *
     * @param cycles the number of cycles for the experiments
     */
    private static void experiment1(int cycles) {
        Fox fox = new Fox(0, 0, 0);
        Hounds hounds = new Hounds(0, 0, 0);

        try {
            Experiment e1 = new Experiment(fox, hounds, 100000, 100);
            Thread t1 = e1.run(cycles,
                               new PrintStream("experiment0.txt"));
            t1.join();
        }
        catch (FileNotFoundException ignored) { }
        catch (InterruptedException ignored) { }
    }

    /**
     * Experiment with various hounds against a random fox.
     *
     * @param cycles the number of cycles for the experiments
     */
    private static void experiment2(int cycles) {
        Hounds[] hounds = new Hounds[8];
        Experiment[] experiments = new Experiment[hounds.length];
        Thread[] threads = new Thread[hounds.length];
        hounds[0] = new Hounds(0.1, 0.5, 0.99);
        hounds[1] = new Hounds(0.2, 0.5, 0.99);
        hounds[2] = new Hounds(0.1, 0.5, 0.95);
        hounds[3] = new Hounds(0.2, 0.5, 0.95);
        hounds[4] = new Hounds(0.1, 0.3, 0.99);
        hounds[5] = new Hounds(0.2, 0.3, 0.99);
        hounds[6] = new Hounds(0.1, 0.3, 0.95);
        hounds[7] = new Hounds(0.2, 0.3, 0.95);

        try {
            for (int i = 0; i < hounds.length; ++i) {
                experiments[i] = new Experiment(new Fox(0, 0, 0), hounds[i],
                                                100000, 100);
                threads[i] = experiments[i].run(cycles,
                                           new PrintStream("experiment2/" + i));
            }
            for (int i = 0; i < hounds.length; ++i) {
                threads[i].join();
                hounds[i].save("experiment2/" + i + ".hounds");
            }
        }
        catch (FileNotFoundException ignored) { }
        catch (InterruptedException ignored) { }
    }

    /**
     * Experiment with various foxes against random hounds.
     *
     * @param cycles the number of cycles for the experiments
     */
    private static void experiment3(int cycles) {
        Fox[] foxes = new Fox[8];
        Experiment[] experiments = new Experiment[foxes.length];
        Thread[] threads = new Thread[foxes.length];
        foxes[0] = new Fox(0.1, 0.5, 0.99);
        foxes[1] = new Fox(0.2, 0.5, 0.99);
        foxes[2] = new Fox(0.1, 0.5, 0.95);
        foxes[3] = new Fox(0.2, 0.5, 0.95);
        foxes[4] = new Fox(0.1, 0.3, 0.99);
        foxes[5] = new Fox(0.2, 0.3, 0.99);
        foxes[6] = new Fox(0.1, 0.3, 0.95);
        foxes[7] = new Fox(0.2, 0.3, 0.95);

        try {
            for (int i = 0; i < foxes.length; ++i) {
                experiments[i] = new Experiment(foxes[i], new Hounds(0, 0, 0),
                                                100000, 100);
                threads[i] = experiments[i].run(cycles,
                                           new PrintStream("experiment3/" + i));
            }
            for (int i = 0; i < foxes.length; ++i) {
                threads[i].join();
                foxes[i].save("experiment2/" + i + ".fox");
            }
        }
        catch (FileNotFoundException ignored) { }
        catch (InterruptedException ignored) { }
    }

    /**
     * Expirement with trained foxes and hounds.
     *
     * @param cycles the number of cycles for the experiments
     */
    private static void experiment4(int cycles) {
        Fox[] foxes = new Fox[4];
        Hounds[] hounds = new Hounds[4];
        Experiment[] experiments = new Experiment[foxes.length];
        foxes[0] = (Fox)LearningSystem.load("experiment3/0.fox");
        foxes[1] = (Fox)LearningSystem.load("experiment3/0.fox");
        foxes[2] = (Fox)LearningSystem.load("experiment3/1.fox");
        foxes[3] = (Fox)LearningSystem.load("experiment3/1.fox");
        hounds[0] = (Hounds)LearningSystem.load("experiment2/0.hounds");
        hounds[1] = (Hounds)LearningSystem.load("experiment2/1.hounds");
        hounds[2] = (Hounds)LearningSystem.load("experiment2/0.hounds");
        hounds[3] = (Hounds)LearningSystem.load("experiment2/1.hounds");
        Thread[] threads = new Thread[foxes.length];

        try {
            for (int i = 0; i < foxes.length; ++i) {
                experiments[i] = new Experiment(foxes[i], hounds[i], 100000,
                                                100);
                threads[i] = experiments[i].run(cycles,
                                       new PrintStream("experiment4/exp4-" + i + ".csv"));
            }
            for (int i = 0; i < foxes.length; ++i) {
                threads[i].join();
                foxes[i].save("experiment4/" + i + ".fox");
                hounds[i].save("experiment4/" + i + ".hounds");
            }
        }
        catch (FileNotFoundException ignored) { }
        catch (InterruptedException ignored) { }
    }

    /**
     * Expirement with not trained foxes and hounds.
     *
     * @param cycles the number of cycles for the experiments
     */
    private static void experiment5(int cycles) {
        Fox[] foxes = new Fox[4];
        Hounds[] hounds = new Hounds[4];
        Experiment[] experiments = new Experiment[foxes.length];
        foxes[0] = new Fox(0.1, 0.5, 0.99);
        foxes[1] = new Fox(0.1, 0.5, 0.99);
        foxes[2] = new Fox(0.2, 0.5, 0.99);
        foxes[3] = new Fox(0.2, 0.5, 0.99);
        hounds[0] = new Hounds(0.1, 0.5, 0.99);
        hounds[1] = new Hounds(0.2, 0.5, 0.99);
        hounds[2] = new Hounds(0.1, 0.5, 0.99);
        hounds[3] = new Hounds(0.2, 0.5, 0.99);
        Thread[] threads = new Thread[foxes.length];

        try {
            for (int i = 0; i < foxes.length; ++i) {
                experiments[i] = new Experiment(foxes[i], hounds[i], 100000,
                                                100);
                threads[i] = experiments[i].run(cycles,
                                       new PrintStream("experiment5/exp5-" + i + ".csv"));
            }
            for (int i = 0; i < foxes.length; ++i) {
                threads[i].join();
                foxes[i].save("experiment5/" + i + ".fox");
                hounds[i].save("experiment5/" + i + ".hounds");
            }
        }
        catch (FileNotFoundException ignored) { }
        catch (InterruptedException ignored) { }
    }

    public static void main(String args[]) {
        experiment5(1000);
    }
}
