package foxandhounds.logic;

public class Test {
    public static void main(String[] args) {
        Fox fox = new Fox(0.1, 0.1, 0.95);
        Hounds hounds = new Hounds(0.1, 0.1, 0.95);
        State state = fox.startGame();
        boolean foxTurn = true;
        for (int move = 0; move < 10000; ++move, foxTurn = !foxTurn) {
            printState(state);
            if (foxTurn) {
                System.out.println("Now moves fox.");
                state = fox.move(state);
            } else {
                System.out.println("Now moves hound.");
                state = hounds.move(state);
            }
            if (state.isFinal()) {
                printState(state);
                System.out.println("Game over!");
                if (state.foxWon()) {
                    System.out.println("Fox won!");
                }
                if (state.houndsWon()) {
                    System.out.println("Hounds won!");
                }
                state = fox.startGame();
                foxTurn = false;
            }
        }
    }

    public static void printState(State state) {
        char[][] board = new char[8][8];
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                board[i][j] = ((i + j) % 2 == 0 ? '#' : ' ');
            }
        }
        for (Coordinates hound : state.getHounds()) {
            int row = hound.getRow();
            board[row][hound.getColumn() * 2 + row % 2] = 'H';
        }
        Coordinates foxCoordinates = state.getFox();
        int row = foxCoordinates.getRow();
        board[row][foxCoordinates.getColumn() * 2 + row % 2] = 'F';
        for (int i = 7; i >= 0; --i) {
            System.out.println(board[i]);
        }
    }
}
