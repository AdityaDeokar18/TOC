import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class LBASimulator {

    private StringBuilder tape;
    private String state;
    private int head;

    private List<String> executionLog;

    public LBASimulator(String input) {
        tape = new StringBuilder(input);
        state = "q0";
        head = 0;
        executionLog = new ArrayList<>();
    }

    private void logStep() {

        StringBuilder snapshot =
                new StringBuilder(tape.toString());

        snapshot.insert(head, "[");
        snapshot.insert(head + 2, "]");

        executionLog.add(
                "State: " + state +
                " | Head: " + head +
                " | Tape: " + snapshot
        );
    }

    private boolean validateInput() {

        if (tape.length() == 0)
            return false;

        // Alphabet validation
        for (int i = 0; i < tape.length(); i++) {

            char ch = tape.charAt(i);

            if (ch != 'a' && ch != 'b' && ch != 'c')
                return false;
        }

        // Check ordering: a* b* c*
        int phase = 0;

        for (int i = 0; i < tape.length(); i++) {

            char ch = tape.charAt(i);

            if (ch == 'a') {

                if (phase > 0)
                    return false;

            } else if (ch == 'b') {

                if (phase == 2)
                    return false;

                phase = 1;

            } else if (ch == 'c') {

                phase = 2;
            }
        }

        return true;
    }

    public boolean run() {

        if (!validateInput()) {

            state = "q_reject";
            logStep();

            return false;
        }

        state = "q1";
        head = 0;

        logStep();

        while (true) {

            // -------------------------------
            // Find unmarked 'a'
            // -------------------------------

            boolean foundA = false;

            for (int i = 0; i < tape.length(); i++) {

                if (tape.charAt(i) == 'a') {

                    head = i;

                    tape.setCharAt(i, 'X');

                    state = "q2";

                    logStep();

                    foundA = true;

                    break;
                }
            }

            // No 'a' remaining
            if (!foundA) {

                state = "q_accept";

                head = tape.length() - 1;

                logStep();

                return true;
            }

            // -------------------------------
            // Find matching 'b'
            // -------------------------------

            boolean foundB = false;

            for (int i = head + 1; i < tape.length(); i++) {

                if (tape.charAt(i) == 'b') {

                    head = i;

                    tape.setCharAt(i, 'Y');

                    state = "q3";

                    logStep();

                    foundB = true;

                    break;
                }
            }

            if (!foundB) {

                state = "q_reject";

                logStep();

                return false;
            }

            // -------------------------------
            // Find matching 'c'
            // -------------------------------

            boolean foundC = false;

            for (int i = head + 1; i < tape.length(); i++) {

                if (tape.charAt(i) == 'c') {

                    head = i;

                    tape.setCharAt(i, 'Z');

                    state = "q4";

                    logStep();

                    foundC = true;

                    break;
                }
            }

            if (!foundC) {

                state = "q_reject";

                logStep();

                return false;
            }

            // -------------------------------
            // Return to left side
            // -------------------------------

            head = 0;

            state = "q1";

            logStep();
        }
    }

    public void displayResult(boolean accepted) {

        System.out.println("\n========== LBA EXECUTION ==========");

        for (int i = 0; i < executionLog.size(); i++) {

            System.out.println(
                    "Step " + i + ": "
                    + executionLog.get(i)
            );
        }

        System.out.println(
                "\n==================================="
        );

        if (accepted)
            System.out.println("Result: ACCEPT");
        else
            System.out.println("Result: REJECT");

        System.out.println(
                "==================================="
        );
    }
}

public class LBA {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println(
                "========================================"
        );

        System.out.println(
                " Context-Sensitive Grammar Parser"
        );

        System.out.println(
                " Linear Bounded Automata Simulation"
        );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "\nLanguage: L = { a^n b^n c^n | n >= 1 }"
        );

        System.out.print(
                "\nEnter input string: "
        );

        String input = scanner.nextLine();

        LBASimulator lba =
                new LBASimulator(input);

        boolean result = lba.run();

        lba.displayResult(result);

        scanner.close();
    }
}