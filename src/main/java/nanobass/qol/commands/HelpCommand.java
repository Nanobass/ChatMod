package nanobass.qol.commands;

import nanobass.qol.command.Command;
import nanobass.qol.command.CommandProvider;

import java.util.List;
import java.util.Map;

public class HelpCommand extends Command {
	public static final int COMMANDS_PER_PAGE = 12;

	public static int ceilDiv(int x, int y) {
		final int q = x / y;
		if ((x ^ y) >= 0 && (q * y != x)) {
			return q + 1;
		}
		return q;
	}

	public HelpCommand() {
		super("help", "Help", new Argument("page", 0, false, ArgumentType.Number, ArgumentType.String));
	}

	@Override
	public void execute(CommandProvider provider, Map<String, Parameter> parameters) {
		List<Command> commands = provider.getCommands();
		boolean hasPageArgument = parameters.containsKey("page");
		if (hasPageArgument && parameters.get("page").type == ArgumentType.String) {
			String id = parameters.get("page").getAsString();
			Command command = provider.getCommand(id);
			StringBuilder builder = new StringBuilder();
			builder.append(command.toString()).append("\n").append(command.description);
			provider.getChatProvider().sendMessage(null, builder.toString());
		} else {
			int pages = ceilDiv(commands.size(), COMMANDS_PER_PAGE);
			int page = 0;
			if (hasPageArgument) {
				page = parameters.get("page").getAsInteger() - 1;
			}
			if (page < 0 || page > pages) {
				provider.getChatProvider().sendMessage(null, "%cInvalid Page");
				return;
			}
			if (!hasPageArgument) {
				provider.getChatProvider().sendMessage(null, String.format("%%6%d Pages", pages));
			} else {
				provider.getChatProvider().sendMessage(null, String.format("%%6Page %d", page + 1));
			}
			for (Command command : commands.subList(page * COMMANDS_PER_PAGE, Math.min(page * COMMANDS_PER_PAGE + COMMANDS_PER_PAGE, commands.size()))) {
				provider.getChatProvider().sendMessage(null, command.toString());
			}
		}
	}
}
