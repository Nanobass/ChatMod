package nanobass.qol.mixin;

import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.KeybindsMenu;
import finalforeach.cosmicreach.settings.Keybind;
import nanobass.qol.CustomSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ KeybindsMenu.class })
public abstract class KeybindsMenuMixin extends GameState {

	@Shadow
	protected abstract void addKeybindButton(String label, Keybind keybind);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addExtraKeys(CallbackInfo ci) {
		addKeybindButton("Chat", CustomSettings.keyChat);
	}

}
