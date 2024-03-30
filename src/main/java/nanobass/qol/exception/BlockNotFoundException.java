package nanobass.qol.exception;

public class BlockNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private String blockId;

	public BlockNotFoundException(String blockId) {
		super(blockId);
		this.blockId = blockId;
	}

	public String getBlockId() {
		return blockId;
	}

	@Override
	public String toString() {
		return String.format("%%c BlockId %s not found", blockId);
	}
}
