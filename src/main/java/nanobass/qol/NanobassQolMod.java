package nanobass.qol;

import nanobass.qol.command.Command;
import nanobass.qol.command.Command.Argument;
import nanobass.qol.command.Command.ArgumentType;
import nanobass.qol.command.CommandProvider;
import nanobass.qol.commands.ClearCommand;
import nanobass.qol.commands.HelpCommand;
import nanobass.qol.commands.RespawnCommand;
import nanobass.qol.commands.TeleportCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.util.Map;
import java.util.logging.Logger;

public class NanobassQolMod implements PreLaunchEntrypoint, ModInitializer {
	public static final String MOD_ID = "nanobass_chat";
	public static final Logger LOGGER = Logger.getLogger("nanobass_chat");

	public void onPreLaunch() {
		Chat.initialize();
		LOGGER.info("Chat Initialized");
	}

	@Override
	public void onInitialize() {
		CommandProvider command = CommandProvider.getInstance();
		command.register(new HelpCommand());
		command.register(new TeleportCommand());
		command.register(new RespawnCommand());
		command.register(new ClearCommand());
		command.register(new Command("id", "id", new Argument("state", 0, true, ArgumentType.BlockState)) {
			@Override
			public void execute(CommandProvider provider, Map<String, Parameter> parameters) {
				System.out.println(parameters.get("state").getAsBlockState().toString());
			}
		});
	}

}
