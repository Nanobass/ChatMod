package nanobass.qol.command;

import nanobass.qol.ChatProvider;
import nanobass.qol.NanobassQolMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandEngine extends CommandProvider {

	private List<Command> commands;

	public CommandEngine() {
		this.commands = new ArrayList<>();
	}

	@Override
	public void register(Command command) {
		NanobassQolMod.LOGGER.info(String.format("Command Registered: %s", command));
		commands.add(command);
	}

	@Override
	public List<Command> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	@Override
	public Command getCommand(String id) {
		return commands.stream().filter(command -> command.id.equals(id)).findFirst().get();
	}

	@Override
	public ChatProvider getChatProvider() {
		return ChatProvider.getInstance();
	}

}
