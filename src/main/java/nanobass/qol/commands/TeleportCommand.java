package nanobass.qol.commands;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.entities.Player;
import nanobass.qol.command.Command;
import nanobass.qol.command.CommandProvider;

import java.util.Map;

public class TeleportCommand extends Command {

	public TeleportCommand() {
		super("tp", "Teleports You", new Command.Argument("position", 0, true, ArgumentType.Position));
	}

	@Override
	public void execute(CommandProvider provider, Map<String, Parameter> parameters) {
		Player player = InGame.getLocalPlayer();
		Vector3 position = parameters.get("position").getAsPosition();
		player.setPosition(position.x, position.y, position.z);
	}
}
