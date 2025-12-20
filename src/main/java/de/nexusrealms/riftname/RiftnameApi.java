package de.nexusrealms.riftname;

import com.mojang.datafixers.util.Either;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Optional;
import java.util.UUID;

public class RiftnameApi {
    public static void setTag(ServerPlayerEntity player, String tag){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setTag(player, Text.literal(tag), true);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setTag(ServerPlayerEntity player, Text tag){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setTag(player, tag, false);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static Optional<Boolean> toggleTagStyleOverride(ServerPlayerEntity player){
        Optional<Boolean> b = player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).toggleTagStyleOverride(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
        return b;
    }
    public static void clearTag(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearTag(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setNick(ServerPlayerEntity player, String nick){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setNick(player, Text.literal(nick), true);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setNick(ServerPlayerEntity player, Text nick){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setNick(player, nick, false);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static Optional<Boolean> toggleNickStyleOverride(ServerPlayerEntity player){
        Optional<Boolean> b = player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).toggleNickStyleOverride(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
        return b;
    }
    public static void clearNick(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearNick(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearStyle(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearStyle(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setHexColor(ServerPlayerEntity player, TextColor color){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setHexColor(player, color);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void addFormatting(ServerPlayerEntity player, Formatting formatting){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).addFormatting(player, formatting);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static Text getFormattedName(Text name, UUID uuid, Scoreboard scoreboard){
        if(!Riftname.NAME_COMPONENT.isProvidedBy(scoreboard)) return name;
        NameComponent component = Riftname.NAME_COMPONENT.get(scoreboard);
        return component.getFormattedName(name, uuid);
    }
    public static Optional<Text> getFormattedTag(ServerPlayerEntity player){
        return player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).getFormattedTag(player);
    }
}
