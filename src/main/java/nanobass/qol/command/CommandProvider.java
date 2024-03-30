package nanobass.qol.command;

import nanobass.qol.ChatProvider;
import nanobass.qol.NanobassQolMod;
import nanobass.qol.command.Command.Parameter;
import nanobass.qol.exception.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public abstract class CommandProvider {

	private static CommandProvider instance;

	public static void setInstance(CommandProvider instance) {
		CommandProvider.instance = instance;
	}

	public static CommandProvider getInstance() {
		return instance;
	}

	public static Command getCommand(List<Command> commands, String source) throws InvalidCommandException {
		int end = source.indexOf(" ");
		if (end == -1) {
			end = source.length();
		}
		String id = source.substring(0, end);
		for (Command command : commands) {
			if (id.equals(command.id)) {
				return command;
			}
		}
		throw new InvalidCommandException(source, id);
	}

	public void execute(String commandString) {
		ChatProvider chat = getChatProvider();
		try {
			Command command = CommandProvider.getCommand(getCommands(), commandString);
			List<LexerToken> tokens = CommandLexer.getTokens(command, commandString);
			Map<String, Parameter> parameters = CommandParser.getParameters(command, commandString, tokens);
			try {
				command.execute(this, parameters);
			} catch (Exception e) {
				throw new CommandRunException(command, parameters, e);
			}
		} catch (InvalidCommandException e) {
			chat.sendMessage(null, e.toString());
			NanobassQolMod.LOGGER.log(Level.WARNING, "Invalid Command", e);
		} catch (CommandLexException e) {
			chat.sendMessage(null, e.toString());
			NanobassQolMod.LOGGER.log(Level.WARNING, "Lexer Failed", e);
		} catch (CommandParseException e) {
			chat.sendMessage(null, e.toString());
			NanobassQolMod.LOGGER.log(Level.WARNING, "Parser Failed", e);
		} catch (BlockNotFoundException e) {
			chat.sendMessage(null, e.toString());
			NanobassQolMod.LOGGER.log(Level.WARNING, "Block not Found", e);
		} catch (BlockStateNotValidException e) {
			chat.sendMessage(null, e.toString());
			NanobassQolMod.LOGGER.log(Level.WARNING, "BlockState not Valid", e);
		} catch (CommandRunException e) {
			chat.sendMessage(null, "Run Failed, Contact Mod Creator and send them the Stack Trace");
			chat.sendMessage(null, e.toString());
			chat.sendException(null, e.getCause());
			NanobassQolMod.LOGGER.log(Level.WARNING, "Run Failed", e);
		}
	}

	public abstract void register(Command command);

	public abstract List<Command> getCommands();

	public abstract Command getCommand(String id);

	public abstract ChatProvider getChatProvider();

}
