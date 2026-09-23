package io.github.akankshasm.connectfour;

import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RandomComputerTest {
    @Test
    void choosesOnlyLegalColumnsWithoutMutatingBoard() {
        Board board = new Board();
        for (int i = 0; i < 6; i++) board.drop(3, i % 2 == 0 ? Player.HUMAN : Player.COMPUTER);
        RandomComputer computer = new RandomComputer(new Random(1234));
        for (int i = 0; i < 500; i++) {
            int col = computer.chooseColumn(board);
            assertTrue(board.legalColumns().contains(col));
            assertNotEquals(3, col);
            assertNull(board.cellAt(5, col));
        }
        assertEquals(6, board.legalColumns().size());
    }

    @Test
    void rejectsFinishedBoard() {
        Board board = new Board();
        for (int i = 0; i < 4; i++) board.drop(i, Player.HUMAN);
        RandomComputer computer = new RandomComputer(new Random(1));
        assertThrows(IllegalStateException.class, () -> computer.chooseColumn(board));
    }
}
