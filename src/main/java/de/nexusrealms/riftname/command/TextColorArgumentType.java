package de.nexusrealms.riftname.command;

import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.serialization.JsonOps;
import de.nexusrealms.riftname.mixin.TextColorAccessor;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class TextColorArgumentType implements ArgumentType<TextColor> {
    private static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType((value)
            -> Text.translatable("argument.textcolor.invalid", value));
    public static TextColorArgumentType textColor() {
        return new TextColorArgumentType();
    }

    public static TextColor getTextColor(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, TextColor.class);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        ArrayList<String> list = new ArrayList<>(TextColorAccessor.getByNameMap().keySet());
        list.add("#ff004f");
        return CommandSource.suggestMatching(list, builder);
    }

    @Override
    public Collection<String> getExamples() {
        ArrayList<String> list = new ArrayList<>(TextColorAccessor.getByNameMap().keySet());
        list.add("#ff004f");
        return list;
    }

    @Override
    public TextColor parse(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();

        while(reader.canRead()) {
            reader.skip();
        }

        String string = reader.getString().substring(i, reader.getCursor());

        return TextColor.CODEC.parse(JsonOps.INSTANCE, new JsonPrimitive(string)).result().orElseThrow(() -> INVALID_COLOR_EXCEPTION.create(string));
    }
}
