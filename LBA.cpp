#include <iostream>
#include <string>
#include <vector>
using namespace std;

class LBASimulator {
private:
    string tape;
    string state;
    int head;
    vector<string> executionLog;

    void logStep() {
        string snapshot = tape;
        snapshot.insert(head, "[");
        snapshot.insert(head + 2, "]");
        
        executionLog.push_back(
            "State: " + state +
            " | Head: " + to_string(head) +
            " | Tape: " + snapshot
        );
    }

public:
    LBASimulator(string input) {
        tape = input;
        state = "q0";
        head = 0;
    }

    bool validateInput() {
        if (tape.empty())
            return false;

        for (char ch : tape) {
            if (ch != 'a' && ch != 'b' && ch != 'c')
                return false;
        }

        // Check order: a* b* c*
        int phase = 0;

        for (char ch : tape) {
            if (ch == 'a') {
                if (phase > 0)
                    return false;
            }
            else if (ch == 'b') {
                if (phase == 2)
                    return false;
                phase = 1;
            }
            else if (ch == 'c') {
                phase = 2;
            }
        }

        return true;
    }

    bool run() {

        if (!validateInput()) {
            state = "q_reject";
            logStep();
            return false;
        }

        state = "q1";
        head = 0;
        logStep();

        while (true) {

            // Find unmarked 'a'
            bool foundA = false;

            for (int i = 0; i < tape.length(); i++) {
                if (tape[i] == 'a') {
                    head = i;
                    tape[i] = 'X';
                    state = "q2";
                    logStep();
                    foundA = true;
                    break;
                }
            }

            // No unmarked a remains
            if (!foundA) {
                state = "q_accept";
                head = tape.length() - 1;
                logStep();
                return true;
            }

            // Find matching b
            bool foundB = false;

            for (int i = head + 1; i < tape.length(); i++) {
                if (tape[i] == 'b') {
                    head = i;
                    tape[i] = 'Y';
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

            // Find matching c
            bool foundC = false;

            for (int i = head + 1; i < tape.length(); i++) {
                if (tape[i] == 'c') {
                    head = i;
                    tape[i] = 'Z';
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

            // Return to left side
            head = 0;
            state = "q1";
            logStep();
        }
    }

    void displayResult(bool accepted) {

        cout << "\n========== LBA EXECUTION ==========\n";

        for (int i = 0; i < executionLog.size(); i++) {
            cout << "Step " << i << ": "
                 << executionLog[i] << endl;
        }

        cout << "\n===================================\n";

        if (accepted)
            cout << "Result: ACCEPT\n";
        else
            cout << "Result: REJECT\n";

        cout << "===================================\n";
    }
};

int main() {

    string input;

    cout << "========================================\n";
    cout << " Context-Sensitive Grammar Parser\n";
    cout << " Linear Bounded Automata Simulation\n";
    cout << "========================================\n";

    cout << "\nLanguage: L = { a^n b^n c^n | n >= 1 }\n";

    cout << "\nEnter input string: ";
    cin >> input;

    LBASimulator lba(input);

    bool result = lba.run();

    lba.displayResult(result);

    return 0;
}