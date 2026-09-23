package io.github.akankshasm.connectfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleGameTest {
    private String play(String input) throws IOException {
        StringWriter output = new StringWriter();
        new ConsoleGame(new BufferedReader(new StringReader(input)), new PrintWriter(output),
                new RandomComputer(new Random(42))).run();
        return output.toString();
    }

    @Test
    void quitAndEndOfInputExitCleanly() throws IOException {
        for (String input : new String[]{"q\n", " QUIT \n", ""}) {
            String output = play(input);
            assertTrue(output.contains("Goodbye!"));
            assertFalse(output.contains("Computer chooses"));
        }
    }

    @Test
    void malformedAndOutOfRangeInputDoNotConsumeTurn() throws IOException {
        String output = play("hello\n\n1.5\n99999999999999999999\n0\n8\n-1\nq\n");
        assertTrue(output.contains("Invalid input."));
        assertTrue(output.contains("Choose a column from 1 to 7."));
        assertFalse(output.contains("Computer chooses"));
    }

    @Test
    void legalMoveTriggersExactlyOneComputerTurn() throws IOException {
        String output = play(" 1 \nq\n");
        assertTrue(output.contains(" X"));
        assertTrue(output.contains(" O"));
        assertEquals(1, output.split("Computer chooses column", -1).length - 1);
        assertTrue(output.contains("Goodbye!"));
    }
}
