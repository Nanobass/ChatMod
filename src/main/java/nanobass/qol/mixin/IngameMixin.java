package nanobass.qol.mixin;

import com.badlogic.gdx.Input;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import nanobass.qol.Chat;
import nanobass.qol.ChatMessage;
import nanobass.qol.ChatProvider;
import nanobass.qol.command.CommandProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;

@Mixin({ InGame.class })
public abstract class IngameMixin extends GameState {

	@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lcom/badlogic/gdx/Input;isKeyJustPressed(I)Z"))
	private boolean escape(Input instance, int i, Operation<Boolean> original) {
		boolean isKeyJustPressed = original.call(instance, i);
		if (isKeyJustPressed && Chat.INSTANCE.active) {
			Chat.INSTANCE.active = false;
			Chat.INSTANCE.line.clear();
			return false;
		}
		return isKeyJustPressed;
	}

	@WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/entities/Player;update(Lfinalforeach/cosmicreach/world/Zone;)V"))
	private boolean disablePlayer(Player instance, Zone world) {
		return !Chat.INSTANCE.active;
	}

	@Inject(method = "update", at = @At("TAIL"))
	private void processChat(CallbackInfo ci) {
		Queue<ChatMessage> messageQueue = ChatProvider.getInstance().getMessageQueue();
		while (!messageQueue.isEmpty()) {
			ChatMessage message = messageQueue.poll();
			if (message.isCommand()) {
				CommandProvider.getInstance().execute(message.getMessage().substring(1));
			} else {
				ChatProvider.getInstance().sendMessage(message);
			}
		}
	}

}
