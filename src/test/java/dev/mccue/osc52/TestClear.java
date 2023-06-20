package dev.mccue.osc52;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClear {
    private static final TestCase[] cases = new TestCase[] {
           new TestCase(
                    "clear system clipboard",
                Mode.DEFAULT,
                Clipboard.SYSTEM,
    "\u001b]52;c;!\u0007"
            ),
            new TestCase(
            "clear system clipboard tmux mode",
                Mode.TMUX,
        Clipboard.SYSTEM,
        "\u001bPtmux;\u001b\u001b]52;c;!\u0007\u001b\\"
            ),
        new TestCase(
            "clear system clipboard screen mode",
            Mode.SCREEN,
        Clipboard.SYSTEM,
        "\u001bP\u001b]52;c;!\u0007\u001b\\"
        )
    };

    @Test
    public void testAllCases() {
        for (var c : cases) {
            var s = OSC52.ofClear()
                    .clipboard(c.clipboard)
                    .mode(c.mode);
            assertEquals(c.expected, s.toString());
        }
    }

    public record TestCase(
            String name,
            Mode mode,
            Clipboard clipboard,
            String expected
    ) {}
}
