package nanobass.qol.exception;

import finalforeach.cosmicreach.world.blocks.Block;

public class BlockStateNotValidException extends Exception {

	private static final long serialVersionUID = 1L;

	private Block block;
	private String state;

	public BlockStateNotValidException(Block block, String state) {
		super(String.format("%s[%s]", block, state));
		this.block = block;
		this.state = state;
	}

	public BlockStateNotValidException(BlockNotFoundException cause, String state) {
		super(String.format("%s[%s]", cause.toString(), state), cause);
		this.block = null;
		this.state = state;
	}

	public Block getBlock() {
		return block;
	}

	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		if (getCause() instanceof BlockNotFoundException e) {
			return e.toString();
		}
		return String.format("%%c %s[%s] not found", block.toString(), state);
	}

}
