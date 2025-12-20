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

import java.util.Optional;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
public class RiftnameCommands {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(literal("nick")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearNick(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.clear"), false);
                            return 1;
                        }))
                .then(literal("set")
                        .then(literal("gradient")
                                .then(argument("nickname", GradientArgumentType.text())
                                        .executes(commandContext -> {
                                            Text nick = GradientArgumentType.getText(commandContext, "nickname");
                                            RiftnameApi.setNick(commandContext.getSource().getPlayer(), nick);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.set", nick), false);
                                            return 1;
                                        })))
                        .then(literal("simple")
                                .then(argument("nickname", StringArgumentType.string())
                                        .executes(commandContext -> {
                                            String nick = StringArgumentType.getString(commandContext, "nickname");
                                            RiftnameApi.setNick(commandContext.getSource().getPlayer(), nick);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.set", nick), false);
                                            return 1;
                                        })))
                        .then(literal("text")
                                .then(argument("nickname", TextArgumentType.text(registryAccess))
                                        .executes(commandContext -> {
                                            Text nick = TextArgumentType.getTextArgument(commandContext, "nickname");
                                            RiftnameApi.setNick(commandContext.getSource().getPlayer(), nick);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.set", nick), false);
                                            return 1;
                                        }))))
                .then(literal("togglestyleoverride")
                        .executes(commandContext -> {
                            Optional<Boolean> b = RiftnameApi.toggleNickStyleOverride(commandContext.getSource().getPlayer());
                            b.ifPresentOrElse(
                                    aBoolean -> commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.toggle.success." + aBoolean), false),
                                    () -> commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.nick.toggle.fail"), false));
                            return 1;
                        })));
        dispatcher.register(literal("namestyle")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearStyle(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.style.clear"), false);
                            return 1;
                        }))
                .then(literal("formatting")
                        .then(argument("formatting", FormattingArgumentType.formatting())
                                .executes(commandContext -> {
                                    Formatting formatting = FormattingArgumentType.getFormatting(commandContext, "formatting");
                                    RiftnameApi.addFormatting(commandContext.getSource().getPlayer(), formatting);
                                    commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.formatting.set", formatting.asString()), false);
                                    return 1;
                                })))
                .then(literal("color")
                        .then(argument("color", TextColorArgumentType.textColor())
                                .executes(commandContext -> {
                                    TextColor textColor = TextColorArgumentType.getTextColor(commandContext, "color");
                                    RiftnameApi.setHexColor(commandContext.getSource().getPlayer(), textColor);
                                    commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.color.set", textColor.toString()), false);
                                    return 1;
                                }))));
        dispatcher.register(literal("playertag")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(literal("clear")
                        .requires(ServerCommandSource::isExecutedByPlayer)
                        .executes(commandContext -> {
                            RiftnameApi.clearTag(commandContext.getSource().getPlayer());
                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.clear"), false);
                            return 1;
                        }))
                .then(literal("set")
                        .then(literal("gradient")
                                .then(argument("tag", GradientArgumentType.text())
                                        .executes(commandContext -> {
                                            Text tag = GradientArgumentType.getText(commandContext, "tag");
                                            RiftnameApi.setTag(commandContext.getSource().getPlayer(), tag);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set", tag), false);
                                            return 1;
                                        })))
                        .then(literal("simple")
                                .then(argument("tag", StringArgumentType.string())
                                        .executes(commandContext -> {
                                            String nick = StringArgumentType.getString(commandContext, "tag");
                                            RiftnameApi.setTag(commandContext.getSource().getPlayer(), nick);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set", nick), false);
                                            return 1;
                                        })))
                        .then(literal("text")
                                .then(argument("tag", TextArgumentType.text(registryAccess))
                                        .executes(commandContext -> {
                                            Text nick = TextArgumentType.getTextArgument(commandContext, "tag");
                                            RiftnameApi.setTag(commandContext.getSource().getPlayer(), nick);
                                            commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.set", nick), false);
                                            return 1;
                                        }))))
                .then(literal("togglestyleoverride")
                        .executes(commandContext -> {
                            Optional<Boolean> b = RiftnameApi.toggleTagStyleOverride(commandContext.getSource().getPlayer());
                            b.ifPresentOrElse(
                                    aBoolean -> commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.toggle.success." + aBoolean), false),
                                    () -> commandContext.getSource().sendFeedback(() -> Text.translatable("message.riftname.tag.toggle.fail"), false));
                            return 1;
                        })));

    }
}
