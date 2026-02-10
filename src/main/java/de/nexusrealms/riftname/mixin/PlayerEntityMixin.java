package de.nexusrealms.riftname.mixin;

import de.nexusrealms.riftname.RiftnameApi;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(method = "getDisplayName", at = @At("TAIL"), cancellable = true)
    public void getFormattedName(CallbackInfoReturnable<Text> cir){
        cir.setReturnValue(RiftnameApi.getFormattedName(cir.getReturnValue(), getUuid(), getEntityWorld().getScoreboard()));
    }
}
