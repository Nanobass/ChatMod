package nanobass.qol.command;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import nanobass.qol.command.Command.Argument;
import nanobass.qol.command.Command.ArgumentType;
import nanobass.qol.command.Command.Parameter;
import nanobass.qol.exception.BlockNotFoundException;
import nanobass.qol.exception.BlockStateNotValidException;
import nanobass.qol.exception.CommandParseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParser {

	@Deprecated
	public static float parseFloat(String token, float current) {
		return ArgumentParser.parseFloat(token, current);
	}

	@Deprecated
	public static Block parseBlock(String blockId, boolean allowAutoBase) throws BlockNotFoundException {
		return ArgumentParser.parseBlock(blockId, allowAutoBase);
	}

	@Deprecated
	public static BlockState parseBlockState(String blockState, boolean allowAutoBase) throws BlockStateNotValidException {
		return ArgumentParser.parseBlockState(blockState, allowAutoBase);
	}

	private static Parameter parseParameter(Argument argument, ArgumentType type, Parser parser) throws BlockNotFoundException, BlockStateNotValidException {
		switch (type) {
		case String:
			return new Parameter(argument, type, parser.advance().token);
		case Position: {
			Player player = InGame.getLocalPlayer();
			Vector3 pos = player.getEntity().getPosition();
			float x = parseFloat(parser.advance().token, pos.x);
			float y = parseFloat(parser.advance().token, pos.y);
			float z = parseFloat(parser.advance().token, pos.z);
			return new Parameter(argument, type, new Vector3(x, y, z));
		}
		case Number:
			return new Parameter(argument, type, Float.parseFloat(parser.advance().token));
		case Boolean:
			return new Parameter(argument, type, Boolean.parseBoolean(parser.advance().token));
		case JSON:
			return new Parameter(argument, type, new JsonReader().parse(parser.advance().token));
		case BlockState:
			return new Parameter(argument, type, parseBlockState(parser.advance().token, true));
		case Block:
			return new Parameter(argument, type, parseBlock(parser.advance().token, true));
		default:
			throw new RuntimeException("We should never Get here!!!!");
		}
	}

	public static Map<String, Parameter> getParameters(Command command, String source, List<LexerToken> tokens) throws CommandParseException, BlockNotFoundException, BlockStateNotValidException {
		Map<String, Parameter> parameters = new HashMap<>();
		Parser parser = new Parser(tokens);

		for (Argument argument : command.arguments) {
			BlockNotFoundException bnfe = null;
			BlockStateNotValidException bsnve = null;
			for (ArgumentType type : argument.types) {
				try {
					parser.mark();
					Parameter param = parseParameter(argument, type, parser);
					parameters.put(argument.id, param);
					if (param != null) {
						break;
					}
				} catch (BlockNotFoundException e) {
					if (bnfe != null) {
						bnfe = e;
					}
				} catch (BlockStateNotValidException e) {
					if (bsnve != null) {
						bsnve = e;
					}
				} catch (Exception e) {
					parser.reset();
				}
			}

			if (bnfe != null) {
				throw bnfe;
			}

			if (bsnve != null) {
				throw bsnve;
			}

			if (parameters.get(argument.id) == null && argument.required) {
				throw new CommandParseException(source, tokens, parser.getPosition(), argument);
			}
		}

		return parameters;
	}

}
