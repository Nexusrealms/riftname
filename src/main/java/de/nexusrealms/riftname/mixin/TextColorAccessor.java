package de.nexusrealms.riftname.mixin;

import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(TextColor.class)
public interface TextColorAccessor {
    @Accessor("BY_NAME")
    static Map<String, TextColor> getByNameMap(){
        throw new AssertionError();
    }
}
