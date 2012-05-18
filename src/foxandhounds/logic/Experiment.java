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
public class Experiment {
    private Fox fox;
    private Hounds hounds;
    private State state;
    private int trainingTurns;
    private int evaluationGames;

    /**
     * Initializes an expirement.
     *
     * @param fox a fox to expirement with
     * @param hounds hounds to expirement with
     * @param trainingTurns the number of turns per training phase
     * @param evaluationGames the number of games played to evaluate
     */
    public Experiment(Fox fox, Hounds hounds, int trainingTurns,
                      int evaluationGames) {
        this.fox = fox;
        this.hounds = hounds;
        this.trainingTurns = trainingTurns;
        this.evaluationGames = evaluationGames;
        state = fox.startGame();
    }

    /**
     * Runs some cycles of an expirement.
     * 
     * Each cycle consists of a training phase followed by an evaluation phase.
     * The results are printed as the number of fox and hounds wins separated
     * by a single space, one line per experiment.
     *
     * @param cycles the number of cycles to run the experiment
     * @param out output stream
     */
    public void run(int cycles, PrintStream out) {
        while (cycles-- > 0) {
            for (int i = 0; i < trainingTurns; ++i) {
                state = step(fox, hounds, state);
            }
            Fox evaluationFox = (Fox)fox.clone();
            Hounds evaluationHounds = (Hounds)hounds.clone();
            State evaluationState = evaluationFox.startGame();
            long initFoxWins = evaluationFox.getWins();
            long initHoundsWins = evaluationHounds.getWins();
            evaluationFox.setLearningRate(0);
            evaluationFox.setExplorationRate(0);
            evaluationHounds.setLearningRate(0);
            evaluationHounds.setExplorationRate(0);
            while (evaluationFox.getWins() + evaluationFox.getLosses()
                   - initFoxWins - initHoundsWins < evaluationGames) {
                evaluationState = step(evaluationFox, evaluationHounds,
                                       evaluationState);
            }
            out.println((evaluationFox.getWins() - initFoxWins) + " "
                        + (evaluationHounds.getWins() - initHoundsWins));
        }
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
     */
    private static void expirement1() {
        Fox fox1 = new Fox(0.1, 0.5, 0.99);
        Fox fox2 = new Fox(0.1, 0.3, 0.99);
        Fox randomFox = new Fox(0, 0, 0);
        Hounds hounds1 = new Hounds(0.1, 0.5, 0.99);
        Hounds hounds2 = new Hounds(0.1, 0.3, 0.99);
        Hounds randomHounds = new Hounds(0, 0, 0);

        try {
            Experiment experiment;
            experiment = new Experiment(fox1, randomHounds, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/fox1VSrandom"));
            System.out.println("Done 1");
            experiment = new Experiment(fox2, randomHounds, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/fox2VSrandom"));
            System.out.println("Done 2");
            experiment = new Experiment(randomFox, hounds1, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/hounds1VSrandom"));
            System.out.println("Done 3");
            experiment = new Experiment(randomFox, hounds2, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/hounds2VSrandom"));
            System.out.println("Done 4");
            experiment = new Experiment(fox1, hounds1, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/hounds1VSfox1"));
            System.out.println("Done 5");
            experiment = new Experiment(fox1, hounds2, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/hounds2VSfox1"));
            System.out.println("Done 6");
            experiment = new Experiment(fox2, hounds1, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/hounds1VSfox2"));
            System.out.println("Done 7");
            experiment = new Experiment(fox2, hounds2, 100000, 100);
            experiment.run(100, new PrintStream("expirement1/hounds2VSfox2"));
            System.out.println("Done 8");
        } catch (FileNotFoundException ignored) { }
    }

    public static void main(String args[]) {
        expirement1();
    }
}
