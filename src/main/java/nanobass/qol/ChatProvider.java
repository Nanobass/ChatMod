package nanobass.qol;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public abstract class ChatProvider {

	private static ChatProvider instance;

	public static void setInstance(ChatProvider instance) {
		ChatProvider.instance = instance;
	}

	public static ChatProvider getInstance() {
		return instance;
	}

	public abstract List<ChatMessage> getChatHistory();

	public abstract void sendMessage(ChatMessage message);

	public void sendMessage(ChatAuthor author, String message) {
		sendMessage(new ChatMessage(author, message));
	}

	public void sendException(ChatAuthor author, Throwable t) {
		List<StackTraceElement> fullStackTrace = Arrays.asList(t.getStackTrace());
		List<StackTraceElement> shortStackTrace = fullStackTrace.subList(0, Math.min(12, fullStackTrace.size()));

		sendMessage(author, "%c" + t.toString());
		for (StackTraceElement trace : shortStackTrace) {
			sendMessage(author, "%c" + String.format("  at %s:%d", trace.getClassName(), trace.getLineNumber()));
		}
	}

	public abstract Queue<ChatMessage> getMessageQueue();

	public abstract List<ChatMessage> getLocalMessageHistory();
}
