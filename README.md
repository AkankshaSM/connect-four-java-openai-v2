# Connect Four — Java 21

A terminal Connect Four game for a human (`X`) against a computer (`O`) that chooses uniformly at random from legal columns. No runtime dependencies.

## Requirements

- JDK 21 (with `java` and `javac` on `PATH`)
- Apache Maven 3.9+ (`mvn` on `PATH`)
- Internet access on the first Maven build to download plugins and JUnit

Check your environment:

```sh
java -version
mvn -version
```

Maven must also report Java 21. No Maven wrapper is included.

## Download

```sh
git clone https://github.com/AkankshaSM/connect-four-java-openai-v2.git
cd connect-four-java-openai-v2
```

Run all following commands from the repository root.

## Build

```sh
mvn clean package
```

This compiles the Java 21 sources, runs the JUnit tests, and packages the executable JAR at `target/connect-four.jar`.

## Test

```sh
mvn test
```

Test reports are written to `target/surefire-reports/`.

## Play

After building:

```sh
java -jar target/connect-four.jar
```

- You move first. Enter a column number **1–7**, then press Enter.
- Tokens fall to the lowest empty cell on the **6-row × 7-column** board.
- Invalid text, out-of-range numbers, and full columns are rejected without losing your turn.
- Four matching tokens horizontally, vertically, or in either diagonal direction win.
- A full board with no winner is a draw. A winning final move is a win, not a draw.
- Enter `q` or `quit` (case-insensitive) to quit. End-of-input also exits cleanly.
- The computer is random, not a strategic AI. Start the command again for a new game.

## Design

- `Board`: gravity, cell state, legal columns, win/draw detection; no terminal I/O. Its API uses zero-based coordinates with row 0 at the top. The caller controls turn order, allowing isolated board fixtures in tests.
- `Player`: human/computer token identity.
- `RandomComputer`: injectable random generator, uniform legal-column selection, no I/O or board mutation.
- `ConsoleGame`: one-based input conversion, validation messages, rendering, alternating turns, quitting, and outcome display. Reader/writer injection allows terminal tests without system streams.
- `Main`: wires standard input/output to the game and handles I/O failures.

Maven pins the compiler, test, and JAR plugins. The JAR manifest selects `io.github.akankshasm.connectfour.Main`; no dependency bundling is needed because JUnit is test-only.

## Tests and verification status

JUnit 5 tests cover board dimensions and empty state, gravity, invalid moves, full columns, horizontal and vertical wins, both diagonal directions, post-win move rejection, a complete draw, legal random choices, input validation, quit/EOF handling, and a human/computer turn.

**The creation environment could not execute Java or Maven. These tests were written but were not executed, and build success is not claimed.** Committed files were read back for static verification. Run `mvn clean package` locally to compile, execute tests, and generate the playable JAR.
