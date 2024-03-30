package nanobass.qol.exception;

public class InvalidCommandException extends Exception {

	private static final long serialVersionUID = 1L;

	private String source;
	private String id;

	public InvalidCommandException(String source, String id) {
		this.source = source;
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("%c");
		builder.append("Invalid Command: /");
		builder.append(id);
		return builder.toString();
	}
}
