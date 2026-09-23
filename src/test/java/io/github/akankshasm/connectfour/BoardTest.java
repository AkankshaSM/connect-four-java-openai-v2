package io.github.akankshasm.connectfour;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    void emptyBoardAndGravity() {
        Board board = new Board();
        assertEquals(6, Board.ROWS);
        assertEquals(7, Board.COLUMNS);
        assertEquals(7, board.legalColumns().size());
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS; col++) assertNull(board.cellAt(row, col));
        }
        assertEquals(5, board.drop(2, Player.HUMAN));
        assertEquals(4, board.drop(2, Player.COMPUTER));
        assertEquals(Player.HUMAN, board.cellAt(5, 2));
        assertEquals(Player.COMPUTER, board.cellAt(4, 2));
        assertFalse(board.isOver());
    }

    @Test
    void invalidMovesDoNotChangeBoard() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, () -> board.drop(-1, Player.HUMAN));
        assertThrows(IllegalArgumentException.class, () -> board.drop(7, Player.HUMAN));
        assertThrows(NullPointerException.class, () -> board.drop(0, null));
        assertEquals(7, board.legalColumns().size());
        assertEquals(5, board.drop(0, Player.HUMAN));
        assertThrows(IllegalArgumentException.class, () -> board.cellAt(6, 0));
    }

    @Test
    void fullColumnIsRejected() {
        Board board = new Board();
        for (int i = 0; i < 6; i++) board.drop(0, i % 2 == 0 ? Player.HUMAN : Player.COMPUTER);
        assertFalse(board.legalColumns().contains(0));
        assertThrows(IllegalArgumentException.class, () -> board.drop(0, Player.HUMAN));
        assertEquals(Player.COMPUTER, board.cellAt(0, 0));
        assertFalse(board.isOver());
    }

    @Test
    void horizontalWinAndPostGameRejection() {
        Board board = new Board();
        // The final token joins runs on both sides.
        for (int column : new int[]{0, 1, 3}) board.drop(column, Player.HUMAN);
        assertTrue(board.winner().isEmpty());
        board.drop(2, Player.HUMAN);
        assertEquals(Player.HUMAN, board.winner().orElseThrow());
        assertTrue(board.isOver());
        assertFalse(board.isDraw());
        assertTrue(board.legalColumns().isEmpty());
        assertThrows(IllegalStateException.class, () -> board.drop(4, Player.COMPUTER));
    }

    @Test
    void verticalWin() {
        Board board = new Board();
        for (int i = 0; i < 3; i++) board.drop(6, Player.COMPUTER);
        assertFalse(board.isOver());
        board.drop(6, Player.COMPUTER);
        assertEquals(Player.COMPUTER, board.winner().orElseThrow());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void bothDiagonalDirections(boolean mirrored) {
        Board board = new Board();
        for (int step = 0; step < 4; step++) {
            int col = mirrored ? 6 - step : step;
            for (int support = 0; support < step; support++) board.drop(col, Player.COMPUTER);
            board.drop(col, Player.HUMAN);
            if (step < 3) assertFalse(board.isOver());
        }
        assertEquals(Player.HUMAN, board.winner().orElseThrow());
    }

    @Test
    void fullBoardWithoutFourIsDraw() {
        Board board = new Board();
        // Alternating rows of XXOOXXO and OOXXOOX have no four-token run.
        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 7; col++) {
                assertFalse(board.isOver());
                Player player = (col / 2 + row) % 2 == 0 ? Player.HUMAN : Player.COMPUTER;
                assertEquals(row, board.drop(col, player));
            }
        }
        assertTrue(board.isDraw());
        assertTrue(board.isOver());
        assertTrue(board.winner().isEmpty());
        assertTrue(board.legalColumns().isEmpty());
    }
}
