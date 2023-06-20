package dev.mccue.osc52;

/**
 * The clipboard to target with the set, query, or clear operation.
 */
public enum Clipboard {
    /**
     * The system clipboard buffer.
     */
    SYSTEM('c'),
    /**
     * The primary clipboard buffer (X11).
     */
    PRIMARY('p');

    /**
     * The character that needs to be inserted into the OSC52 escape sequence
     * in order to target the selected clipboard.
     */
    public final char value;

    Clipboard(char value) {
        this.value = value;
    }
}
