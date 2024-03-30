package nanobass.qol.command;

import nanobass.qol.exception.CommandLexException;

import java.util.ArrayList;
import java.util.List;

public class CommandLexer {

	public static List<LexerToken> getTokens(Command command, String source) throws CommandLexException {
		List<LexerToken> tokens = new ArrayList<>();
		String[] toks = source.split(" ");
		for (int i = 1; i < toks.length; i++) {
			tokens.add(new LexerToken(null, toks[i]));
		}
		return tokens;
	}
}
