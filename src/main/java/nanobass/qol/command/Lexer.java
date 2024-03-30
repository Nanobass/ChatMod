package nanobass.qol.command;

public class Lexer {
	private String source;
	private int position;
	private int mark;

	public Lexer(String source) {
		this.source = source;
		this.position = 0;
		this.mark = -1;
	}

	public char advance() {
		if (position >= source.length()) {
			throw new IndexOutOfBoundsException();
		}
		char chr = source.charAt(position);
		position++;
		return chr;
	}
	
	public boolean isEmpty() {
		return position >= source.length();
	}

	public void mark() {
		mark = position;
	}

	public void reset() {
		if (mark != -1) {
			position = mark;
		}
	}

	public int getPosition() {
		return position;
	}
}
