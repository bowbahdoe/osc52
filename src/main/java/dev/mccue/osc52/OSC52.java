package dev.mccue.osc52;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * OSC52 is a terminal escape sequence that allows copying text to the clipboard.
 *
 * <p>
 *     The sequence consists of the following:
 * </p>
 *
 * <p>
 *     <code>
 *          OSC 52 ; Pc ; Pd BEL
 *     </code>
 * </p>
 *
 * <p>Pc is the clipboard choice:</p>
 *
 * <ol>
 *     <li>c: clipboard</li>
 *     <li>p: primary</li>
 *     <li>q: secondary (not supported)</li>
 *     <li>s: select (not supported)</li>
 *     <li>0-7: cut-buffers (not supported)</li>
 * </ol>
 *
 * <p>
 *     {@code Pd} is the data to copy to the clipboard. This string should be encoded in
 * base64 (RFC-4648).
 * </p>
 *
 * <p>
 *     If {@code Pd} is "?", the terminal replies to the host with the current contents of
 *     the clipboard.
 * </p>
 *
 * <p>
 *     If {@code Pd} is neither a base64 string nor "?", the terminal clears the clipboard.
 * </p>
 *
 * <p>
 *     See <a href="https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands">
 *         https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands</a>
 *     where {@code Ps} = 52 => Manipulate Selection Data.
 * </p>
 *
 * <p>
 *     This class gives a way to construct such a sequence conveniently. It is immutable and safe to use
 *     from multiple threads.
 * </p>
 *
 * <p>
 *     Examples:
 * </p>
 *
 * <pre>{@code
 *     // copy "hello world" to the system clipboard
 *     System.err.print(OSC52.of("hello world"));
 *
 *     // copy "hello world" to the primary Clipboard
 *     System.err.print(OSC52.of("hello world").primary());
 *
 *     // limit the size of the string to copy 10 bytes
 *     System.err.print(OSC52.of("0123456789).limit(10));
 *
 *     // escape the OSC52 sequence for screen using DCS sequences
 *     System.err.print(OSC52.of("hello world").screen());
 *
 *     // escape the OSC52 sequence for Tmux
 *     System.err.print(OSC52.of("hello world").tmux());
 *
 *     // query the system Clipboard
 *     System.err.print(OSC52.ofQuery());
 *
 *     // query the primary clipboard
 *     System.err.println(OSC52.ofQuery().primary());
 *
 *     // clear the system Clipboard
 *     System.err.println(OSC52.ofClear());
 *
 *     // clear the primary Clipboard
 *     System.err.println(OSC52.ofClear().primary());
 * }</pre>
 */
public final class OSC52 {
    private final String str;
    private final int limit;
    private final Operation op;
    private final Mode mode;
    private final Clipboard clipboard;

    private OSC52(
            String str,
            int limit,
            Operation op,
            Mode mode,
            Clipboard clipboard
    ) {
        this.str = Objects.requireNonNull(str);
        this.limit = Math.max(0, limit);
        this.op = Objects.requireNonNull(op);
        this.mode = Objects.requireNonNull(mode);
        this.clipboard = Objects.requireNonNull(clipboard);
    }

    /**
     * Creates a new OSC52 sequence with the given string(s). Strings are
     * joined with a space character.
     * @param strs The strings to use.
     * @return A new OSC52 sequence.
     */
    public static OSC52 of(String... strs) {
        return new OSC52(
                String.join(" " , strs),
                0,
                Operation.SET,
                Mode.DEFAULT,
                Clipboard.SYSTEM
        );
    }


    /**
     * Sets the mode.
     * @param mode The new mode.
     * @return A new instance with the mode set.
     */
    public OSC52 mode(Mode mode) {
        return new OSC52(
                this.str,
                this.limit,
                this.op,
                mode,
                this.clipboard
        );
    }

    /**
     * Sets the mode to {@link Mode#TMUX}.
     *
     * <p>
     *     This is a shortcut for {@code mode(Mode.TMUX)}
     * </p>
     *
     * @return A new instance with the set to {@link Mode#TMUX}.
     */
    public OSC52 tmux() {
        return this.mode(Mode.TMUX);
    }


    /**
     * Sets the mode to {@link Mode#SCREEN}.
     *
     * <p>
     *     This is a shortcut for {@code mode(Mode.SCREEN)}
     * </p>
     *
     * @return A new instance with the set to {@link Mode#SCREEN}.
     */
    public OSC52 screen() {
        return this.mode(Mode.SCREEN);
    }

    /**
     * Sets the clipboard buffer.
     * @param clipboard The clipboard buffer to use.
     * @return A new instance with the clipboard buffer set.
     */
    public OSC52 clipboard(Clipboard clipboard) {
        return new OSC52(
                this.str,
                this.limit,
                this.op,
                this.mode,
                clipboard
        );
    }

    /**
     * Sets the clipboard to {@link Clipboard#PRIMARY}.
     * <p>
     *     This is a shortcut for {@code clipboard(Clipboard.PRIMARY)}.
     * </p>
     *
     * @return A new instance with the operation set to {@link Clipboard#PRIMARY}.
     */
    public OSC52 primary() {
        return this.clipboard(Clipboard.PRIMARY);
    }

    /**
     * Sets the limit.
     * The default limit is 0 (no limit).
     * <p>
     * Strings longer than the limit get ignored. Settting the limit to 0 or a
     * negative value disables the limit. Each terminal defines its own escapse
     * sequence limit.
     * </p>
     * @param limit The new limit.
     * @return A new instance with the limit sent.
     */
    public OSC52 limit(int limit) {
        return new OSC52(
                this.str,
                limit,
                this.op,
                this.mode,
                this.clipboard
        );
    }

    /**
     * Sets the operation.
     *
     * @param op The new operation.
     * @return A new instance with the operation set.
     */
    public OSC52 operation(Operation op) {
        return new OSC52(
                this.str,
                this.limit,
                op,
                this.mode,
                this.clipboard
        );
    }

    /**
     * Sets the operation to {@link Operation#CLEAR}.
     * <p>
     *     This is a shortcut for {@code operation(Operation.CLEAR)}.
     * </p>
     *
     * @return A new instance with the operation set to {@link Operation#CLEAR}.
     */
    public OSC52 clear() {
        return this.operation(Operation.CLEAR);
    }

    /**
     * Sets the operation to {@link Operation#QUERY}.
     * <p>
     *     This is a shortcut for {@code operation(Operation.QUERY)}.
     * </p>
     *
     * @return A new instance with the operation set to {@link Operation#QUERY}.
     */
    public OSC52 query() {
        return this.operation(Operation.QUERY);
    }

    /**
     * Sets the string to use in the sequence.
     * @param strs The string to use in the sequence.
     * @return A new instance with the new string sent.
     */
    public OSC52 withString(String... strs) {
        return new OSC52(
                String.join(" ", strs),
                this.limit,
                this.op,
                this.mode,
                this.clipboard
        );
    }

    /**
     * Makes a new instance with the operation set to {@link Operation#QUERY}.
     *
     * <p>
     *     This is a shortcut for {@code OSC52.of().query()}.
     * </p>
     *
     * @return A new instance with the operation set to {@link Operation#QUERY}.
     */
    public static OSC52 ofQuery() {
        return OSC52.of().query();
    }

    /**
     * Makes a new instance with the operation set to {@link Operation#CLEAR}.
     *
     * <p>
     *     This is a shortcut for {@code OSC52.of().clear()}.
     * </p>
     *
     * @return A new instance with the operation set to {@link Operation#CLEAR}.
     */
    public static OSC52 ofClear() {
        return OSC52.of().clear();
    }

    private String seqStart() {
        return switch (mode) {
            case TMUX -> "\u001BPtmux;\u001B";
            case SCREEN -> "\u001BP";
            case DEFAULT -> "";
        };
    }

    private String seqEnd() {
        return switch (mode) {
            case TMUX, SCREEN -> "\u001B\\";
            case DEFAULT -> "";
        };
    }

    /**
     * @return The OSC52 sequence.
     */
    @Override
    public String toString() {
        var sb = new StringBuilder();

        sb.append(seqStart());
        sb.append(String.format("\u001B]52;%c;", clipboard.value));

        switch (this.op) {
            case SET -> {
                var str = this.str;
                if (this.limit > 0 && str.length() > this.limit) {
                    return "";
                }
                var b64 = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
                if (this.mode == Mode.SCREEN) {
                    // Screen doesn't support OSC52 but will pass the contents of a DCS
                    // sequence to the outer terminal unchanged.
                    //
                    // Here, we split the encoded string into 76 bytes chunks and then
                    // join the chunks with <end-dsc><start-dsc> sequences. Finally,
                    // wrap the whole thing in
                    // <start-dsc><start-osc52><joined-chunks><end-osc52><end-dsc>.
                    // s := strings.SplitN(b64, "", 76)
                    var s = new String[b64.length() / 76 + 1];
                    for (int i = 0; i < b64.length(); i += 76) {
                        var end = i + 76;
                        if (end > b64.length()) {
                            end = b64.length();
                        }

                        s[i / 76] = b64.substring(i, end);
                    }

                    sb.append(String.join("\u001B\\\u001BP", s));
                } else {
                    sb.append(b64);
                }
            }
            case QUERY -> {
                // OSC52 queries the clipboard using "?"
                sb.append("?");
            }
            case CLEAR -> {
                // OSC52 clears the clipboard if the data is neither a base64 string nor "?"
                // we're using "!" as a default
                sb.append("!");
            }
        }

        sb.append("\u0007");
        sb.append(seqEnd());

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OSC52 OSC52
                && OSC52.mode == this.mode
                && OSC52.op == this.op
                && OSC52.clipboard == this.clipboard
                && OSC52.limit == this.limit
                && OSC52.str.equals(this.str);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode, op, clipboard, limit, str);
    }
}
