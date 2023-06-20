package dev.mccue.osc52;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestQuery {
    private static final TestCase[] cases = new TestCase[]{
            new TestCase(
                    "query system clipboard",
                    Mode.DEFAULT,
                    Clipboard.SYSTEM, "\u001b]52;c;?\u0007"),
            new TestCase(
                    "query primary clipboard",
                    Mode.DEFAULT,
                    Clipboard.PRIMARY,
                    "\u001b]52;p;?\u0007"
            ),
            new TestCase(
                    "query system clipboard tmux mode",
                    Mode.TMUX,
                    Clipboard.SYSTEM,
                    "\u001bPtmux;\u001b\u001b]52;c;?\u0007\u001b\\"
            ),
            new TestCase(
                    "query system clipboard screen mode",
                    Mode.SCREEN,
                    Clipboard.SYSTEM,
                    "\u001bP\u001b]52;c;?\u0007\u001b\\"
            ),
            new TestCase(
                    "query primary clipboard tmux mode",
                    Mode.TMUX,
                    Clipboard.PRIMARY,
                    "\u001bPtmux;\u001b\u001b]52;p;?\u0007\u001b\\"
            ),
            new TestCase(
                    "query primary clipboard screen mode",
                    Mode.SCREEN,
                    Clipboard.PRIMARY,
                    "\u001bP\u001b]52;p;?\u0007\u001b\\"
            ),
    };

    @Test
    public void testAllCases() {
        for (var c : cases) {
            var s = OSC52.ofQuery()
                    .clipboard(c.clipboard)
                    .mode(c.mode);
            assertEquals(c.expected, s.toString());
        }
    }

    record TestCase(
            String name,
            Mode mode,
            Clipboard clipboard,
            String expected
    ) {
    }
}
