package dev.mccue.osc52;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCopy {
    private static final TestCase[] cases = new TestCase[]{
            new TestCase(
                    "hello world",
                    "hello world",
                    Clipboard.SYSTEM,
                    Mode.DEFAULT,
                    0,
                    "\u001b]52;c;aGVsbG8gd29ybGQ=\u0007"
            ),
            new TestCase(
                    "empty string",
                    "",
                    Clipboard.SYSTEM,
                    Mode.DEFAULT,
                    0,
                    "\u001b]52;c;\u0007"
            ),
            new TestCase(
                    "hello world primary",
                    "hello world",
                    Clipboard.PRIMARY,
                    Mode.DEFAULT,
                    0,
                    "\u001b]52;p;aGVsbG8gd29ybGQ=\u0007"
            ),
            new TestCase(
                    "hello world tmux mode",
                    "hello world",
                    Clipboard.SYSTEM,
                    Mode.TMUX,
                    0,
                    "\u001bPtmux;\u001b\u001b]52;c;aGVsbG8gd29ybGQ=\u0007\u001b\\"
            ),
            new TestCase(
                    "hello world screen mode",
                    "hello world",
                    Clipboard.SYSTEM,
                    Mode.SCREEN,
                    0,
                    "\u001bP\u001b]52;c;aGVsbG8gd29ybGQ=\u0007\u001b\\"
            ),
            new TestCase(
                    "hello world screen mode longer than 76 bytes string",
                    "hello world hello world hello world hello world hello world hello world hello world hello world",
                    Clipboard.SYSTEM,
                    Mode.SCREEN,
                    0,
                    "\u001bP\u001b]52;c;aGVsbG8gd29ybGQgaGVsbG8gd29ybGQgaGVsbG8gd29ybGQgaGVsbG8gd29ybGQgaGVsbG8gd29y\u001b\\\u001bPbGQgaGVsbG8gd29ybGQgaGVsbG8gd29ybGQgaGVsbG8gd29ybGQ=\u0007\u001b\\"
            ),
            new TestCase(
                    "hello world with limit 11",
                    "hello world",
                    Clipboard.SYSTEM,
                    Mode.DEFAULT,
                    11,
                    "\u001b]52;c;aGVsbG8gd29ybGQ=\u0007"
            ),
            new TestCase(
                    "hello world with limit 10",
                    "hello world",
                    Clipboard.SYSTEM,
                    Mode.DEFAULT,
                    10,
                    ""
            )
    };

    @Test
    public void testAllCases() {
        for (var c : cases) {
            var s = OSC52.of(c.str)
                    .clipboard(c.clipboard)
                    .mode(c.mode)
                    .limit(c.limit);
            assertEquals(c.expected, s.toString());
        }
    }

    record TestCase(
            String name,
            String str,
            Clipboard clipboard,
            Mode mode,
            int limit,
            String expected
    ) {
    }
}
