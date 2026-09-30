def lba_parser(input_string):
    # Check that input is not empty
    if not input_string:
        return False, []

    # LBA tape
    tape = list(input_string)

    # Valid symbols
    if any(symbol not in ['a', 'b', 'c'] for symbol in tape):
        return False, ["Invalid symbol"]

    steps = []

    # Check that symbols occur in a^n b^n c^n order
    phase = 'a'

    for symbol in tape:
        if phase == 'a':
            if symbol == 'b':
                phase = 'b'
            elif symbol == 'c':
                phase = 'c'

        elif phase == 'b':
            if symbol == 'a':
                return False, steps
            elif symbol == 'c':
                phase = 'c'

        elif phase == 'c':
            if symbol != 'c':
                return False, steps

    # Make a copy of the tape for simulation
    tape = list(input_string)

    steps.append("Initial Tape: " + ''.join(tape))

    # Match a -> b -> c
    while 'a' in tape:

        # Find first unmarked a
        a_index = tape.index('a')
        tape[a_index] = 'X'

        steps.append(
            f"Marked a: {''.join(tape)}"
        )

        # Find first unmarked b after a
        b_index = -1

        for i in range(a_index + 1, len(tape)):
            if tape[i] == 'b':
                b_index = i
                break

        if b_index == -1:
            return False, steps

        tape[b_index] = 'Y'

        steps.append(
            f"Marked b: {''.join(tape)}"
        )

        # Find first unmarked c after b
        c_index = -1

        for i in range(b_index + 1, len(tape)):
            if tape[i] == 'c':
                c_index = i
                break

        if c_index == -1:
            return False, steps

        tape[c_index] = 'Z'

        steps.append(
            f"Marked c: {''.join(tape)}"
        )

    # Check that no unmarked b or c remains
    if 'b' in tape or 'c' in tape:
        return False, steps

    steps.append("All symbols matched.")
    steps.append("LBA Result: ACCEPT")

    return True, steps


# -------------------------------
# Main Program
# -------------------------------

user_input = input("Enter string: ")

accepted, steps = lba_parser(user_input)

print("\n--- LBA SIMULATION ---")

for step in steps:
    print(step)

if accepted:
    print("\nACCEPTED")
else:
    print("\nREJECTED")