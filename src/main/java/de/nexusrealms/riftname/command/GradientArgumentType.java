package de.nexusrealms.riftname.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class GradientArgumentType implements ArgumentType<Text> {
    @Override
    public Text parse(StringReader stringReader) throws CommandSyntaxException {
        MutableText text = Text.empty();
        while (stringReader.canRead()){
            Style style = readStyle(stringReader);
            int i = stringReader.getCursor();
            while (stringReader.canRead() && stringReader.peek() != '&'){
                stringReader.skip();
            }
            String s = stringReader.getString().substring(i, stringReader.getCursor());
            text.append(Text.literal(s).setStyle(style));
        }
        return text;
    }
    public static GradientArgumentType text() {
        return new GradientArgumentType();
    }

    public static Text getText(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, Text.class);
    }
    private Style readStyle(StringReader reader) throws CommandSyntaxException {
        Style style = Style.EMPTY;
        reader.expect('&');
        int i = reader.getCursor();
        for (int j = 0; j < 7; j++) {
            reader.skip();
        }
        String s = reader.getString().substring(i, i + 7);
        while (reader.canRead() && reader.peek() == '&'){
            reader.skip();
            char c = reader.read();
            Formatting formatting = Formatting.byCode(c);
            style = style.withFormatting(formatting);
        }
        style = style.withColor(TextColor.parse(s).getOrThrow());
        return style;
    }
}
