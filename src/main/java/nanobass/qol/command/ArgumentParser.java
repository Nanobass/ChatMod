package nanobass.qol.command;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.world.blocks.Block;
import finalforeach.cosmicreach.world.blocks.BlockState;
import nanobass.qol.exception.BlockNotFoundException;
import nanobass.qol.exception.BlockStateNotValidException;

public class ArgumentParser {

	public static float parseFloat(String token, float current) {
		if (token.equals("~")) {
			return current;
		} else if (token.startsWith("~")) {
			return Float.parseFloat(token.substring(1)) + current;
		} else {
			return Float.parseFloat(token);
		}
	}

	public static Block parseBlock(String blockId, boolean allowAutoBase) throws BlockNotFoundException {
		Array.ArrayIterator<Block> arrayIterator = Block.allBlocks.iterator();
		while (arrayIterator.hasNext()) {
			Block block = arrayIterator.next();
			boolean matches = block.getStringId().equals(blockId);
			if (!matches && allowAutoBase) {
				matches = block.getStringId().equals("base:" + blockId);
			}
			if (matches) {
				return block;
			}
		}
		throw new BlockNotFoundException(blockId);
	}

	public static BlockState parseBlockState(String blockState, boolean allowAutoBase) throws BlockStateNotValidException {

		int blockIdEnd = blockState.indexOf("[");
		if (blockIdEnd == -1) {
			blockIdEnd = blockState.length();
		}
		int stateStringStart = blockState.indexOf("[");
		if (stateStringStart == -1) {
			stateStringStart = blockState.length();
		} else {
			stateStringStart++;
		}
		int stateStringEnd = blockState.indexOf("]");
		if (stateStringEnd == -1) {
			stateStringEnd = blockState.length();
		}

		String blockId = blockState.substring(0, blockIdEnd);
		String stateString = blockState.substring(stateStringStart, stateStringEnd);

		Block block;
		try {
			block = parseBlock(blockId, allowAutoBase);
		} catch (BlockNotFoundException e) {
			throw new BlockStateNotValidException(e, stateString);
		}

		if (stateString.isEmpty()) {
			return block.getDefaultBlockState();
		}
		try {
			return block.getBlockStateFromString(stateString);
		} catch (RuntimeException e) {
			throw new BlockStateNotValidException(block, stateString);
		}
	}
	
}
