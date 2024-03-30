package nanobass.qol.commands;

import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.entities.Player;
import nanobass.qol.command.Command;
import nanobass.qol.command.CommandProvider;

import java.util.Map;

public class RespawnCommand extends Command {
	public RespawnCommand() {
		super("respawn", "Respawns the Player");
	}

	@Override
	public void execute(CommandProvider provider, Map<String, Parameter> parameters) {
		Player player = InGame.getLocalPlayer();
		player.respawn(InGame.world);
	}
}
