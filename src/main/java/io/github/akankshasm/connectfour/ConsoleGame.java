package io.github.akankshasm.connectfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/** Terminal adapter: parses input, manages turns, and renders board state. */
public final class ConsoleGame {
    private final BufferedReader input;
    private final PrintWriter output;
    private final RandomComputer computer;

    public ConsoleGame(BufferedReader input, PrintWriter output, RandomComputer computer) {
        this.input = Objects.requireNonNull(input);
        this.output = Objects.requireNonNull(output);
        this.computer = Objects.requireNonNull(computer);
    }

    public void run() throws IOException {
        Board board = new Board();
        output.println("Connect Four: you are X, computer is O. You move first.");
        output.println("Enter 1-7, or q / quit to exit.");
        render(board);
        while (!board.isOver()) {
            output.print("Your column (1-7, q to quit): ");
            output.flush();
            String line = input.readLine();
            if (line == null || line.strip().equalsIgnoreCase("q")
                    || line.strip().equalsIgnoreCase("quit")) {
                output.println("Goodbye!");
                output.flush();
                return;
            }
            int column;
            try {
                column = Integer.parseInt(line.strip());
            } catch (NumberFormatException exception) {
                output.println("Invalid input. Enter a number from 1 to 7, or q.");
                continue;
            }
            if (column < 1 || column > Board.COLUMNS) {
                output.println("Choose a column from 1 to 7.");
                continue;
            }
            try {
                board.drop(column - 1, Player.HUMAN);
            } catch (IllegalArgumentException exception) {
                output.println(exception.getMessage());
                continue;
            }
            render(board);
            if (board.isOver()) break;
            int choice = computer.chooseColumn(board);
            board.drop(choice, Player.COMPUTER);
            output.println("Computer chooses column " + (choice + 1) + ".");
            render(board);
        }
        if (board.isDraw()) {
            output.println("It's a draw!");
        } else if (board.winner().orElseThrow() == Player.HUMAN) {
            output.println("You win!");
        } else {
            output.println("Computer wins!");
        }
        output.flush();
    }

    private void render(Board board) {
        output.println();
        for (int row = 0; row < Board.ROWS; row++) {
            output.print("|");
            for (int column = 0; column < Board.COLUMNS; column++) {
                Player player = board.cellAt(row, column);
                char token = player == null ? '.' : player == Player.HUMAN ? 'X' : 'O';
                output.print(" " + token);
            }
            output.println(" |");
        }
        output.println("  1 2 3 4 5 6 7");
        output.println();
    }
}
