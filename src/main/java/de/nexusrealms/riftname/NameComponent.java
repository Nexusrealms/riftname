package de.nexusrealms.riftname;

import com.mojang.datafixers.util.Either;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class NameComponent implements AutoSyncedComponent {
    private Map<UUID, Either<Text, String>> tags = new HashMap<>();
    private Map<UUID, List<Formatting>> formattings = new HashMap<>();
    private Map<UUID, String> nickNames = new HashMap<>();
    private Map<UUID, TextColor> hexColors = new HashMap<>();
    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        tags = Riftname.Codecs.PLAYER_TAG_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompound("tags")).getOrThrow();
        formattings = Riftname.Codecs.PLAYER_FORMATTING_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompound("formattings")).getOrThrow();
        nickNames = Riftname.Codecs.PLAYER_NICK_NAME_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompound("nicknames")).getOrThrow();
        hexColors = Riftname.Codecs.PLAYER_TEXT_COLOR_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getCompound("hexcolors")).getOrThrow();

    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.put("tags", Riftname.Codecs.PLAYER_TAG_CODEC.encodeStart(NbtOps.INSTANCE, tags).getOrThrow());
        nbtCompound.put("formattings", Riftname.Codecs.PLAYER_FORMATTING_CODEC.encodeStart(NbtOps.INSTANCE, formattings).getOrThrow());
        nbtCompound.put("nicknames", Riftname.Codecs.PLAYER_NICK_NAME_CODEC.encodeStart(NbtOps.INSTANCE, nickNames).getOrThrow());
        nbtCompound.put("hexcolors", Riftname.Codecs.PLAYER_TEXT_COLOR_CODEC.encodeStart(NbtOps.INSTANCE, hexColors).getOrThrow());
    }
    public Text getFormattedName(Text name, UUID uuid){
        String nick = nickNames.get(uuid);
        MutableText tag = null;
        Either<Text, String> either = tags.get(uuid);
        boolean shouldFormatTag = false;
        if(either != null){
            if(either.left().isPresent()){
                tag = (MutableText) either.left().get();
            } else if (either.right().isPresent()){
                tag = Text.literal(either.right().get());
                shouldFormatTag = true;
            }
        }
        Style nameStyle = Style.EMPTY;
        if(formattings.containsKey(uuid)){
            List<Formatting> formattingList = formattings.get(uuid);
            nameStyle = nameStyle.withFormatting(formattingList.toArray(new Formatting[0]));
        }
        if(hexColors.containsKey(uuid)) {
            nameStyle = nameStyle.withColor(hexColors.get(uuid));
        }
        Text newname = nick != null ? Text.literal(nick) : name;
        ((MutableText) newname).setStyle(nameStyle);
        if(tag != null){
            tag = Text.literal("[").setStyle(tag.getStyle()).append(tag).append("] ").setStyle(tag.getStyle());
            if(shouldFormatTag){
                tag.setStyle(nameStyle);
            }
            return tag.append(newname);
        }
        return newname;
    }

    public void setNick(ServerPlayerEntity player, String nick){
        nickNames.put(player.getUuid(), nick);
    }
    public void clearNick(ServerPlayerEntity player){
        nickNames.remove(player.getUuid());
    }
    public void setHexColor(ServerPlayerEntity player, TextColor color){
        hexColors.put(player.getUuid(), color);
    }
    public void clearHexColor(ServerPlayerEntity player){
        hexColors.remove(player.getUuid());
    }
    public void addFormatting(ServerPlayerEntity player, Formatting formatting){
        formattings.merge(player.getUuid(), new ArrayList<>(List.of(formatting)), (oldlist, newlist) -> {
            oldlist.addAll(newlist);
            return oldlist;
        });
    }
    public void clearFormattings(ServerPlayerEntity player){
        formattings.remove(player.getUuid());
    }
    public void clearTag(ServerPlayerEntity player){
        tags.remove(player.getUuid());
    }
    public boolean clearStyleFromTag(ServerPlayerEntity player){
        Either<Text, String> either = tags.get(player.getUuid());
        if(either != null){
            either.ifLeft(text -> setTag(player, Either.right(text.getString())));
            return either.left().isPresent();
        }
        return false;
    }
    public void setTag(ServerPlayerEntity player, Either<Text, String> tag){
        tags.put(player.getUuid(), tag);
    }
}
