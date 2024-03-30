package nanobass.qol.exception;

import nanobass.qol.command.Command.Argument;
import nanobass.qol.command.LexerToken;

import java.util.List;

public class CommandParseException extends Exception {

	private static final long serialVersionUID = 1L;
	private String source;
	private int position;
	private List<LexerToken> tokens;
	private int token;
	private Argument argument;

	public CommandParseException(String source, List<LexerToken> tokens, int token, Argument argument) {
		this.source = source;
		this.position = source.indexOf(" ");
		if (token < tokens.size()) {
			for (int i = 0; i <= token; i++) {
				this.position += tokens.get(i).token.length();
			}
		}
		this.tokens = tokens;
		this.token = token;
		this.argument = argument;
	}

	public String getSource() {
		return source;
	}

	public List<LexerToken> getTokens() {
		return tokens;
	}

	public int getToken() {
		return token;
	}

	public Argument getArgument() {
		return argument;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("%c");
		builder.append("/").append(source).append("\n");

		if (token < tokens.size()) {
			StringBuilder squiggle = new StringBuilder(" ".repeat(source.length()));
			squiggle.insert(position, "~".repeat(tokens.get(token).token.length()));
			builder.append(squiggle).append("\n");
		}

		builder.append("expected ").append(argument);
		return builder.toString();
	}

}
