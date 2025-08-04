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
    public static boolean clearTagStyle(ServerPlayerEntity player){
            NameComponent component = Riftname.NAME_COMPONENT.get(player.getScoreboard());
            boolean bool = component.clearStyleFromTag(player);
            Riftname.NAME_COMPONENT.sync(player.getScoreboard());
            return bool;
    }
    public static void clearTag(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearTag(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static boolean setTagStyle(ServerPlayerEntity player, Style style){
        boolean b = player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setTagStyle(player, style);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
        return b;
    }
    public static void setTagText(ServerPlayerEntity player, Text text){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setTagText(player, text);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setNick(ServerPlayerEntity player, String nick){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setNick(player, nick);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearNick(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearNick(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setHexColor(ServerPlayerEntity player, TextColor color){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).setHexColor(player, color);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearHexColor(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearHexColor(player);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void addFormatting(ServerPlayerEntity player, Formatting formatting){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).addFormatting(player, formatting);
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearFormattings(ServerPlayerEntity player){
        player.getScoreboard().getComponent(Riftname.NAME_COMPONENT).clearFormattings(player);
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
