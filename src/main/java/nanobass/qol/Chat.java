package nanobass.qol;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import dev.crmodders.flux.util.text.TextEditor;
import nanobass.qol.command.CommandEngine;
import nanobass.qol.command.CommandProvider;


import java.util.*;
import java.util.stream.Collectors;

public class Chat extends ChatProvider implements InputProcessor {

	public static final Chat INSTANCE = new Chat();

	public static void initialize() {
		ChatProvider.setInstance(Chat.INSTANCE);
		CommandProvider.setInstance(new CommandEngine());
	}

	public static final ChatAuthor SERVER_AUTHOR = new ChatAuthor("cosmicreach");
	public static final ChatAuthor LOCAL_AUTHOR = new ChatAuthor("localplayer");

	public boolean active;
	public TextEditor line;
	public int timer;

	public List<ChatMessage> chatHistory;
	public List<ChatMessage> messageHistory;
	public Queue<ChatMessage> messageQueue;

	public Chat() {
		this.active = false;
		this.line = new TextEditor();
		this.chatHistory = new ArrayList<>();
		this.messageHistory = new ArrayList<>();
		this.messageQueue = new LinkedList<>();
	}

	@Override
	public List<ChatMessage> getChatHistory() {
		return Collections.unmodifiableList(chatHistory);
	}

	@Override
	public List<ChatMessage> getLocalMessageHistory() {
		return Collections.unmodifiableList(messageHistory);
	}

	@Override
	public Queue<ChatMessage> getMessageQueue() {
		return messageQueue;
	}

	@Override
	public void sendMessage(ChatMessage message) {
		chatHistory.add(message);
		int lineCount = chatHistory.stream().map(m -> m.getMessage().lines().count()).collect(Collectors.summingInt(l -> l.intValue()));
		if (lineCount > 16) {
			chatHistory.remove(0);
		}
		timer = 450;
	}

	public void recieveMessage(ChatMessage message) {
		messageHistory.add(message);
		messageQueue.add(message);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (active) {
			switch (keycode) {
			case Input.Keys.LEFT:
				line.setCursor(line.getCursor() - 1);
				break;
			case Input.Keys.RIGHT:
				line.setCursor(line.getCursor() + 1);
				break;
			case Input.Keys.UP:
				if (messageHistory.size() > 0) {
					line.clear();
					messageHistory.get(messageHistory.size() - 1).getMessage().chars().forEach(c -> line.append((char) c));
				}
				break;
			case Input.Keys.HOME:
				line.setCursor(0);
				break;
			case Input.Keys.END:
				line.setCursor(line.length());
				break;
			case Input.Keys.ENTER:
				if (!line.isEmpty()) {
					recieveMessage(new ChatMessage(LOCAL_AUTHOR, line.toString()));
				}
				active = false;
				line.clear();
				break;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char chr) {
		if (active) {
			return line.append(chr);
		}
		return false;
	}

	@Override
	public boolean touchDown(int var1, int var2, int var3, int var4) {
		return false;
	}

	@Override
	public boolean touchUp(int var1, int var2, int var3, int var4) {
		return false;
	}

	@Override
	public boolean touchCancelled(int var1, int var2, int var3, int var4) {
		return false;
	}

	@Override
	public boolean touchDragged(int var1, int var2, int var3) {
		return false;
	}

	@Override
	public boolean mouseMoved(int var1, int var2) {
		return false;
	}

	@Override
	public boolean scrolled(float var1, float var2) {
		return false;
	}

}
