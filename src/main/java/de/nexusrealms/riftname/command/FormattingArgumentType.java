package de.nexusrealms.riftname.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public class FormattingArgumentType extends EnumArgumentType<Formatting> {

    protected FormattingArgumentType() {
        super(Formatting.CODEC, Formatting::values);
    }
    public static EnumArgumentType<Formatting> formatting() {
        return new FormattingArgumentType();
    }

    public static Formatting getFormatting(CommandContext<ServerCommandSource> context, String id) {
        return (Formatting) context.getArgument(id, Formatting.class);
    }
}
