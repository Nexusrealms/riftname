package de.nexusrealms.riftname;

import com.mojang.datafixers.util.Either;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.UUID;

public class RiftnameApi {
    public static boolean clearTagStyle(ServerPlayerEntity player){
        if(Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).isPresent()){
            NameComponent component = Riftname.NAME_COMPONENT.get(player.getScoreboard());
            boolean bool = component.clearStyleFromTag(player);
            Riftname.NAME_COMPONENT.sync(player.getScoreboard());
            return bool;
        }
        return false;
    }
    public static void clearTag(ServerPlayerEntity player){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.clearTag(player));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setStringyTag(ServerPlayerEntity player, String tag){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.setTag(player, Either.right(tag)));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setTextTag(ServerPlayerEntity player, Text tag){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.setTag(player, Either.left(tag)));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setNick(ServerPlayerEntity player, String nick){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.setNick(player, nick));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearNick(ServerPlayerEntity player){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.clearNick(player));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void setHexColor(ServerPlayerEntity player, TextColor color){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.setHexColor(player, color));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearHexColor(ServerPlayerEntity player){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.clearHexColor(player));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void addFormatting(ServerPlayerEntity player, Formatting formatting){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.addFormatting(player, formatting));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static void clearFormattings(ServerPlayerEntity player){
        Riftname.NAME_COMPONENT.maybeGet(player.getScoreboard()).ifPresent(tagComponent -> tagComponent.clearFormattings(player));
        Riftname.NAME_COMPONENT.sync(player.getScoreboard());
    }
    public static Text getFormattedName(Text name, UUID uuid, Scoreboard scoreboard){
        if(!Riftname.NAME_COMPONENT.isProvidedBy(scoreboard)) return name;
        NameComponent component = Riftname.NAME_COMPONENT.get(scoreboard);
        return component.getFormattedName(name, uuid);
    }
}
