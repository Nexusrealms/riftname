package de.nexusrealms.riftname;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class NameComponent implements AutoSyncedComponent {
    private Map<UUID, Pair<Text, Boolean>> tags = new HashMap<>();
    private Map<UUID, Pair<Text, Boolean>> nickNames = new HashMap<>();
    private Map<UUID, Style> nameStyles = new HashMap<>();

    public Text getFormattedName(Text name, UUID uuid){
        Pair<Text, Boolean> nickPair = nickNames.get(uuid);
        Pair<Text, Boolean> tagPair = tags.get(uuid);
        Style nameStyle = nameStyles.getOrDefault(uuid, Style.EMPTY);
        Text newName;
        if(nickPair != null){
            MutableText nick = nickPair.getFirst().copy();
            boolean overrideStyle = nickPair.getSecond();
            if(overrideStyle) {
                nick.setStyle(nameStyle);
            }
            newName = nick;
        } else {
            newName = name.copy().setStyle(nameStyle);
        }
        if(tagPair != null){
            MutableText tag = tagPair.getFirst().copy();
            tag = Text.literal("[").setStyle(tag.getStyle()).append(tag).append("] ").setStyle(tag.getStyle());
            if(tagPair.getSecond()){
                tag.setStyle(nameStyle);
            }
            return tag.append(newName);
        }
        return newName;
    }

    public void setNick(ServerPlayerEntity player, Text nick, boolean overrideStyle){
        nickNames.compute(player.getUuid(), (uuid, textBooleanPair) -> {
            if(textBooleanPair != null){
                return textBooleanPair.mapFirst(text -> nick);
            } else {
                return new Pair<>(nick, overrideStyle);
            }
        });
    }
    public Optional<Boolean> toggleNickStyleOverride(ServerPlayerEntity player){
        nickNames.computeIfPresent(player.getUuid(), (uuid, textBooleanPair) ->
            textBooleanPair.mapSecond(b -> !b)
        );
        return nickNames.containsKey(player.getUuid()) ? Optional.of(nickNames.get(player.getUuid()).getSecond()) : Optional.empty();
    }
    public void clearNick(ServerPlayerEntity player){
        nickNames.remove(player.getUuid());
    }
    public void setHexColor(ServerPlayerEntity player, TextColor color){
        nameStyles.compute(player.getUuid(), (uuid, style) -> Objects.requireNonNullElse(style, Style.EMPTY).withColor(color));
    }
    public void clearStyle(ServerPlayerEntity player){
        nameStyles.remove(player.getUuid());
    }
    public void addFormatting(ServerPlayerEntity player, Formatting formatting){
        nameStyles.compute(player.getUuid(), (uuid, style) -> Objects.requireNonNullElse(style, Style.EMPTY).withFormatting(formatting));
    }
    public void clearTag(ServerPlayerEntity player){
        tags.remove(player.getUuid());
    }
    public Optional<Boolean> toggleTagStyleOverride(ServerPlayerEntity player){
        tags.computeIfPresent(player.getUuid(), (uuid, textBooleanPair) ->
                textBooleanPair.mapSecond(b -> !b)
        );
        return tags.containsKey(player.getUuid()) ? Optional.of(tags.get(player.getUuid()).getSecond()) : Optional.empty();
    }
    public void setTag(ServerPlayerEntity player, Text tag, boolean overrideStyle){
        tags.compute(player.getUuid(), (uuid, textBooleanPair) -> {
            if(textBooleanPair != null){
                return textBooleanPair.mapFirst(text -> tag);
            } else {
                return new Pair<>(tag, overrideStyle);
            }
        });
    }

    public Optional<Text> getFormattedTag(ServerPlayerEntity player){
        Pair<Text, Boolean> pair = tags.get(player.getUuid());
        if(pair != null){
            MutableText tag = pair.getFirst().copy();
            if (pair.getSecond()) tag.setStyle(nameStyles.getOrDefault(player.getUuid(), Style.EMPTY));
            return Optional.of(tag);
        }
        return Optional.empty();
    }
    @Override
    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        tags = Riftname.Codecs.PLAYER_NICK_NAME_CODEC.parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.getCompound("tags")).result().orElse(new HashMap<>());
        nickNames = Riftname.Codecs.PLAYER_NICK_NAME_CODEC.parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.getCompound("nicknames")).result().orElse(new HashMap<>());
        nameStyles = Riftname.Codecs.PLAYER_STYLE_CODEC.parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.getCompound("nameStyles")).result().orElse(new HashMap<>());
    }

    @Override
    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbt.put("tags", Riftname.Codecs.PLAYER_NICK_NAME_CODEC.encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), tags).getOrThrow());
        nbt.put("nameStyles", Riftname.Codecs.PLAYER_STYLE_CODEC.encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), nameStyles).getOrThrow());
        nbt.put("nicknames", Riftname.Codecs.PLAYER_NICK_NAME_CODEC.encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), nickNames).getOrThrow());
    }
}
