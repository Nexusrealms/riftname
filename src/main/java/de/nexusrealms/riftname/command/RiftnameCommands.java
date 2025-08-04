package de.nexusrealms.riftname.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.nexusrealms.riftname.RiftnameApi;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.StyleArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
public class RiftnameCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("nick")
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearNick(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.clear"), false);
                            return 1;
                        }))
                .then(literal("set")
                        .then(argument("nickname", StringArgumentType.string())
                                .requires(ServerCommandSource::isExecutedByPlayer)
                                .executes(commandContext -> {
                                    String nick = StringArgumentType.getString(commandContext, "nickname");
                                    RiftnameApi.setNick(commandContext.getSource().getPlayer(), nick);
                                    commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.set", nick), false);
                                    return 1;
                                }))));
        dispatcher.register(literal("formatname")
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearFormattings(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.formatting.clear"), false);
                            return 1;
                        }))
                .then(literal("add")
                        .then(argument("formatting", FormattingArgumentType.formatting())
                                .requires(ServerCommandSource::isExecutedByPlayer)
                                .executes(commandContext -> {
                                    Formatting formatting = FormattingArgumentType.getFormatting(commandContext, "formatting");
                                    RiftnameApi.addFormatting(commandContext.getSource().getPlayer(), formatting);
                                    commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.formatting.set", formatting.asString()), false);
                                    return 1;
                                }))));
        dispatcher.register(literal("colorname")
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearHexColor(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.color.clear"), false);
                            return 1;
                        }))
                .then(literal("set")
                        .then(argument("color", TextColorArgumentType.textColor())
                                .requires(ServerCommandSource::isExecutedByPlayer)
                                .executes(commandContext -> {
                                    TextColor textColor = TextColorArgumentType.getTextColor(commandContext, "color");
                                    RiftnameApi.setHexColor(commandContext.getSource().getPlayer(), textColor);
                                    commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.color.set", textColor.toString()), false);
                                    return 1;
                                }))));
        dispatcher.register(literal("playertag")
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearTag(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.clear"), false);
                            return 1;
                        }))
                .then(literal("unstyle")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            boolean bool = RiftnameApi.clearTagStyle(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.unstyle." + (bool ? "success" : "failure")), false);
                            return 1;
                        }))
                .then(literal("set")
                        .then(literal("style")
                                .then(argument("style", StyleArgumentType.style(registryAccess))
                                        .requires(ServerCommandSource::isExecutedByPlayer)
                                        .executes(commandContext -> {
                                            try {
                                                Style style = StyleArgumentType.getStyle(commandContext, "style");
                                                boolean bool = RiftnameApi.setTagStyle(commandContext.getSource().getPlayer(), style);
                                                if(bool){
                                                    commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set.style").append(RiftnameApi.getFormattedTag(commandContext.getSource().getPlayer()).get()), false);
                                                } else {
                                                    commandContext.getSource().sendError(Text.translatable("message.riftname.tag.set.style.not"));
                                                }
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            return 1;
                                        })))
                        .then(literal("text")
                                .then(argument("text", TextArgumentType.text(registryAccess))
                                        .requires(ServerCommandSource::isExecutedByPlayer)
                                        .executes(commandContext -> {
                                            Text tag = TextArgumentType.getTextArgument(commandContext, "text");
                                            RiftnameApi.setTagText(commandContext.getSource().getPlayer(), tag);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set.text").append(RiftnameApi.getFormattedTag(commandContext.getSource().getPlayer()).get()).append("!"), false);
                                            return 1;
                                        })))));

    }
}
