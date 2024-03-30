package nanobass.qol;

public class ChatMessage {
	private ChatAuthor author;
	private String message;

	public ChatMessage(ChatAuthor author, String message) {
		this.author = author;
		this.message = message;
	}

	public ChatAuthor getAuthor() {
		return author;
	}

	public String getMessage() {
		return message;
	}

	public boolean isCommand() {
		return message.startsWith("/");
	}

	@Override
	public String toString() {
		if (author == null)
			return String.format("%%f%s", message);
		return String.format("%%f<%s> %s", author, message);
	}

}
