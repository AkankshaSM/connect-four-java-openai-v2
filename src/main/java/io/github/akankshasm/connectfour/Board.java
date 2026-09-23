package io.github.akankshasm.connectfour;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Mutable board rules, with no terminal dependencies. Coordinates are zero-based. */
public final class Board {
    public static final int ROWS = 6;
    public static final int COLUMNS = 7;
    private final Player[][] cells = new Player[ROWS][COLUMNS];
    private Player winner;
    private int moves;

    /** Row zero is the top; an empty cell is represented by null. */
    public Player cellAt(int row, int column) {
        if (row < 0 || row >= ROWS || column < 0 || column >= COLUMNS) {
            throw new IllegalArgumentException("Cell is outside the board.");
        }
        return cells[row][column];
    }

    public Optional<Player> winner() {
        return Optional.ofNullable(winner);
    }

    public boolean isDraw() {
        return moves == ROWS * COLUMNS && winner == null;
    }

    public boolean isOver() {
        return winner != null || isDraw();
    }

    public List<Integer> legalColumns() {
        List<Integer> result = new ArrayList<>();
        if (!isOver()) {
            for (int column = 0; column < COLUMNS; column++) {
                if (cells[0][column] == null) result.add(column);
            }
        }
        return List.copyOf(result);
    }

    /** Drops a token and returns its row. Invalid moves leave the board unchanged.
     * Turn order is controlled by the caller.
     */
    public int drop(int column, Player player) {
        Objects.requireNonNull(player, "player");
        if (column < 0 || column >= COLUMNS) {
            throw new IllegalArgumentException("Choose a column from 1 to 7.");
        }
        if (isOver()) throw new IllegalStateException("The game is over.");
        if (cells[0][column] != null) {
            throw new IllegalArgumentException("That column is full. Choose another.");
        }
        int row = ROWS - 1;
        while (cells[row][column] != null) row--;
        cells[row][column] = player;
        moves++;
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] direction : directions) {
            int count = 1 + count(row, column, direction[0], direction[1], player)
                    + count(row, column, -direction[0], -direction[1], player);
            if (count >= 4) {
                winner = player;
                break;
            }
        }
        return row;
    }

    private int count(int row, int column, int dr, int dc, Player player) {
        int total = 0;
        row += dr;
        column += dc;
        while (row >= 0 && row < ROWS && column >= 0 && column < COLUMNS
                && cells[row][column] == player) {
            total++;
            row += dr;
            column += dc;
        }
        return total;
    }
}
