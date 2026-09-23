package io.github.akankshasm.connectfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.random.RandomGenerator;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter output = new PrintWriter(System.out, true, StandardCharsets.UTF_8);
        try {
            new ConsoleGame(input, output, new RandomComputer(RandomGenerator.getDefault())).run();
        } catch (IOException exception) {
            System.err.println("Unable to read terminal input: " + exception.getMessage());
            System.exit(1);
        }
    }
}
