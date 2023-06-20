
# osc52

<p>
    <a href="https://github.com/aymanbagabas/go-osc52/releases"><img src="https://img.shields.io/github/release/aymanbagabas/go-osc52.svg" alt="Latest Release"></a>
    <a href="https://pkg.go.dev/github.com/aymanbagabas/go-osc52/v2?tab=doc"><img src="https://godoc.org/github.com/golang/gddo?status.svg" alt="GoDoc"></a>
</p>

A Java library to work with the [ANSI OSC52](https://invisible-island.net/xterm/ctlseqs/ctlseqs.html#h3-Operating-System-Commands) terminal sequence.

Requires Java 17+

## Dependency Information

### Maven

```xml
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>osc52</artifactId>
    <version>0.1.0</version>
</dependency>
```

### Gradle

```
dependencies {
    implementation("dev.mccue:osc52:0.1.0")
}
```

## Usage

You can use this small library to construct an ANSI OSC52 sequence suitable for
your terminal.


### Example

```java
import dev.mccue.osc52.OSC52;

public class Main {
    public static void main(String[] args) {
        var s = "Hello World!";
        
        // Copy `s` to system clipboard
        System.err.print(OSC52.of(s));
        
        // Copy `s` to primary clipboard (X11)
        System.err.print(OSC52.of(s).primary());
        
        // Query the clipboard
        System.err.print(OSC52.ofQuery());
        
        // Clear system clipboard
        System.err.print(OSC52.ofClear());
    }
}
```

## Tmux

Make sure you have `set-clipboard on` in your config, otherwise, tmux won't
allow your application to access the clipboard [^1].

Using the tmux option, `Mode.TMUX` or `OSC52.of(...).tmux()`, wraps the
OSC52 sequence in a special tmux DCS sequence and pass it to the outer
terminal. This requires `allow-passthrough on` in your config.
`allow-passthrough` is no longer enabled by default
[since tmux 3.3a](https://github.com/tmux/tmux/issues/3218#issuecomment-1153089282) [^2].

[^1]: See [tmux clipboard](https://github.com/tmux/tmux/wiki/Clipboard)
[^2]: [What is allow-passthrough](https://github.com/tmux/tmux/wiki/FAQ#what-is-the-passthrough-escape-sequence-and-how-do-i-use-it)

## Credits

* [go-osc52](https://github.com/aymanbagabas/go-osc52/tree/master) From which almost all code was cribbed. Thank you, @aymanbagabas!
* [vim-oscyank](https://github.com/ojroques/vim-oscyank) which inspired go-osc52.