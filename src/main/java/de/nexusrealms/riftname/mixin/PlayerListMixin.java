package de.nexusrealms.riftname.mixin;

import de.nexusrealms.riftname.RiftnameApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListMixin {
    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void onGetDisplayName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(RiftnameApi.getFormattedName(cir.getReturnValue(), entry.getProfile().getId(), MinecraftClient.getInstance().player.getScoreboard()));
    }
}