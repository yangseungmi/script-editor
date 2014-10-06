package net.imagej.ui.swing.script.interpreter;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_UP;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

import javax.script.ScriptException;
import javax.swing.JTextArea;

/**
 * The prompt for the script interpreter.
 * 
 * @author Johannes Schindelin
 */
public class Prompt extends JTextArea {

	private final InterpreterWindow window;
	private final OutputPane output;

	public Prompt(final InterpreterWindow window, final OutputPane output) {
		super(1, 80);
		this.window = window;
		this.output = output;
		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
				final int code = event.getKeyCode();
				switch (code) {
				case VK_ENTER:
					execute();
					event.consume();
					break;
				case VK_DOWN:
					down();
					break;
				case VK_UP:
					up();
					break;
				}
			}

		});
	}

	protected void up() {
		setText(window.getInterpreter().walkHistory(getText(), false));
	}

	protected void down() {
		setText(window.getInterpreter().walkHistory(getText(), true));
	}

	protected synchronized void execute() {
		try {
			window.getInterpreter().eval(getText());
		}
		catch (ScriptException e) {
			e.printStackTrace(new PrintStream(output.getOutputStream()));
		}
		finally {
			setText("");
		}
	}

}
