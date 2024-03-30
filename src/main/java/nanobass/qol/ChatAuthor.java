package nanobass.qol;

public class ChatAuthor {
	private String name;

	public ChatAuthor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
}
