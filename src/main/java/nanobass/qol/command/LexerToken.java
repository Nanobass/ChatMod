package nanobass.qol.command;

import nanobass.qol.command.Command.ArgumentType;

public class LexerToken {
	public final ArgumentType type;
	public final String token;

	public LexerToken(ArgumentType type, String token) {
		this.type = type;
		this.token = token;
	}

}
