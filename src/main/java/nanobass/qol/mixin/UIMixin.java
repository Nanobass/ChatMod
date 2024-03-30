package nanobass.qol.mixin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.Hotbar;
import finalforeach.cosmicreach.ui.UI;
import nanobass.qol.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ UI.class })
public abstract class UIMixin implements InputProcessor {

	@Shadow
	public static Hotbar hotbar;
	@Shadow
	public static SpriteBatch batch;
	@Shadow
	private Viewport uiViewport;
	@Shadow
	OrthographicCamera uiCamera;
	@Shadow
	ShapeRenderer shapeRenderer;
	@Shadow
	private boolean renderDebugInfo;

	@Shadow
	protected abstract void drawDebugText();

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void headRender(CallbackInfo ci) {
		if (Gdx.input.isKeyJustPressed(CustomSettings.keyChat.getValue())) {

			Chat.INSTANCE.active = true;
		}
		if (Chat.INSTANCE.active) {
			Chat.INSTANCE.timer = 450;

			if (UI.renderUI) {
				Gdx.gl.glClear(256);
				Gdx.gl.glEnable(3042);
				Gdx.gl.glDepthFunc(519);
				Gdx.gl.glBlendFunc(770, 771);
				Gdx.gl.glCullFace(1028);
				uiViewport.apply(false);
				shapeRenderer.setProjectionMatrix(uiCamera.combined);
				shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
				shapeRenderer.end();

				Gdx.gl.glClear(256);
				Gdx.gl.glEnable(2929);
				Gdx.gl.glDepthFunc(513);
				Gdx.gl.glEnable(2884);
				Gdx.gl.glCullFace(1029);
				Gdx.gl.glEnable(3042);
				Gdx.gl.glBlendFunc(770, 771);
				uiViewport.apply(false);

				batch.setProjectionMatrix(uiCamera.combined);
				batch.begin();
				FontRenderer.fontTexture.bind(0);
				renderChat();
				renderChatHistory();
				if (renderDebugInfo)
					drawDebugText();
				batch.end();
			}

			ci.cancel();
		} else {
			Chat.INSTANCE.timer--;
			if (Chat.INSTANCE.timer < 0)
				Chat.INSTANCE.timer = 0;
		}
	}

	@Inject(method = "render", at = @At("TAIL"))
	private void tailRender(CallbackInfo ci) {
		if (UI.renderUI) {
			batch.setProjectionMatrix(uiCamera.combined);
			batch.begin();
			FontRenderer.fontTexture.bind(0);
			if (Chat.INSTANCE.timer > 0)
				renderChatHistory();
			batch.end();
		}
		Gdx.gl.glActiveTexture(33984);
		Gdx.gl.glBindTexture(3553, 0);
	}

	private void renderChat() {
		StringBuilder chat = new StringBuilder(Chat.INSTANCE.line);
		chat.insert(Chat.INSTANCE.line.getCursor(), "_");
		FontRenderer.drawText(batch, this.uiViewport, chat.toString(), -this.uiViewport.getWorldWidth() / 2.0f + 10f, this.uiViewport.getWorldHeight() / 2.0f - 25f);
	}

	private void renderChatHistory() {
		StringBuilder history = new StringBuilder();
		for (ChatMessage entry : ChatProvider.getInstance().getChatHistory()) {
			history.append(LineWrapping.createWrapped(entry.toString(), 80)).append("\n");
		}
		String text = history.toString();
		Vector2 dim = FontRenderer.getTextDimensions(uiViewport, text, new Vector2());
		FontRenderer.drawText(batch, this.uiViewport, history.toString(), -this.uiViewport.getWorldWidth() / 2.0f + 10f, this.uiViewport.getWorldHeight() / 2.0f - 60f - dim.y);
	}

	@Overwrite
	public boolean keyDown(int keycode) {
		if (Chat.INSTANCE.keyDown(keycode)) {
			return true;
		}
		return hotbar.keyDown(keycode);
	}

	@Overwrite
	public boolean keyTyped(char chr) {
		return Chat.INSTANCE.keyTyped(chr);
	}

}
