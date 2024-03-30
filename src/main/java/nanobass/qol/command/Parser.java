package nanobass.qol.command;

import java.util.List;

public class Parser {
	private List<LexerToken> tokens;
	private int position;
	private int mark;

	public Parser(List<LexerToken> tokens) {
		this.tokens = tokens;
		this.position = 0;
		this.mark = -1;
	}

	public LexerToken advance() {
		LexerToken token = tokens.get(position);
		if (token == null) {
			throw new NullPointerException("Token is null");
		}
		position++;
		return token;
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
