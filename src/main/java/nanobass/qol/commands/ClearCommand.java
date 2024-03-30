package nanobass.qol.commands;

import nanobass.qol.Chat;
import nanobass.qol.command.Command;
import nanobass.qol.command.CommandProvider;

import java.util.Map;

public class ClearCommand extends Command {

	public ClearCommand() {
		super("clear", "Clears Chat");
	}

	@Override
	public void execute(CommandProvider provider, Map<String, Parameter> parameters) {
		Chat.INSTANCE.chatHistory.clear();
	}
}
