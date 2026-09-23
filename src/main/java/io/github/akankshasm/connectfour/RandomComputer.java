package io.github.akankshasm.connectfour;

import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

/** Chooses uniformly among legal columns, without changing the board. */
public final class RandomComputer {
    private final RandomGenerator random;

    public RandomComputer(RandomGenerator random) {
        this.random = Objects.requireNonNull(random, "random");
    }

    public int chooseColumn(Board board) {
        List<Integer> legal = board.legalColumns();
        if (legal.isEmpty()) throw new IllegalStateException("No legal moves remain.");
        return legal.get(random.nextInt(legal.size()));
    }
}
