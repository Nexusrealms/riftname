package de.nexusrealms.riftname.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import de.nexusrealms.riftname.RiftnameApi;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
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
                        .then(literal("stringy")
                                .then(argument("tag", StringArgumentType.string())
                                        .requires(ServerCommandSource::isExecutedByPlayer)
                                        .executes(commandContext -> {
                                            try {
                                                String tag = StringArgumentType.getString(commandContext, "tag");
                                                RiftnameApi.setStringyTag(commandContext.getSource().getPlayer(), tag);
                                                commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set.stringy", tag), false);

                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            return 1;
                                        })))
                        .then(literal("text")
                                .then(argument("tag", TextArgumentType.text(registryAccess))
                                        .requires(ServerCommandSource::isExecutedByPlayer)
                                        .executes(commandContext -> {
                                            Text tag = TextArgumentType.getTextArgument(commandContext, "tag");
                                            RiftnameApi.setTextTag(commandContext.getSource().getPlayer(), tag);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set.text").append(tag).append("!"), false);
                                            return 1;
                                        })))));

    }
}
