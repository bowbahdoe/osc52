package dev.mccue.osc52;

/**
 * The mode to use for the OSC52 sequence.
 */
public enum Mode {
    /**
     * The default OSC52 sequence mode.
     */
    DEFAULT,
    /**
     * Escapes the OSC52 sequence for screen using DCS sequences.
     */
    SCREEN,
    /**
     * TmuxMode escapes the OSC52 sequence for tmux. Not needed if tmux
     * clipboard is set to {@code set-clipboard on}.
     */
    TMUX;
}
