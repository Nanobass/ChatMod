package nanobass.qol.exception;

public class CommandLexException extends Exception {

	private static final long serialVersionUID = 1L;

	private String source;
	private int position;

	public CommandLexException(String source, int position, Throwable what) {
		super(what);
		this.source = source;
		this.position = position;
	}

	public String getSource() {
		return source;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("%c");
		builder.append(source).append("\n");
		StringBuilder squiggle = new StringBuilder(" ".repeat(source.length()));
		squiggle.insert(position, "~~~~");
		builder.append(squiggle);
		return builder.toString();
	}

}
