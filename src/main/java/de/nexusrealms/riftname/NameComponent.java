package de.nexusrealms.riftname;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;

public class NameComponent implements AutoSyncedComponent {
    private Map<UUID, Pair<Text, Optional<Style>>> tags = new HashMap<>();
    private Map<UUID, List<Formatting>> formattings = new HashMap<>();
    private Map<UUID, String> nickNames = new HashMap<>();
    private Map<UUID, TextColor> hexColors = new HashMap<>();

    public Text getFormattedName(Text name, UUID uuid){
        String nick = nickNames.get(uuid);
        MutableText tag = null;
        Pair<Text, Optional<Style>> pair = tags.get(uuid);
        boolean shouldFormatTag = true;
        if(pair != null){
            shouldFormatTag = pair.getSecond().isPresent();
            tag = pair.getFirst().copy();
            pair.getSecond().ifPresent(tag::setStyle);
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
        Pair<Text, Optional<Style>> pair = tags.get(player.getUuid());
        if(pair.getSecond().isPresent()){
            setTag(player, new Pair<>(pair.getFirst(), Optional.empty()));
            return true;
        }
        return false;
    }
    public void setTag(ServerPlayerEntity player, Pair<Text, Optional<Style>> tag){
        tags.put(player.getUuid(), tag);
    }
    public boolean setTagText(ServerPlayerEntity player, Text text){
        if(tags.computeIfPresent(player.getUuid(), (uuid, textOptionalPair) -> textOptionalPair.mapFirst(ignored -> text)) == null){
            setTag(player, new Pair<>(text, Optional.empty()));
            return true;
        }
        return false;
    }
    public boolean setTagStyle(ServerPlayerEntity player, Style style){
        return tags.computeIfPresent(player.getUuid(), (uuid, textOptionalPair) -> textOptionalPair.mapSecond(ignored -> Optional.of(style))) != null;

    }
    public Optional<Text> getFormattedTag(ServerPlayerEntity player){
        Pair<Text, Optional<Style>> pair = tags.get(player.getUuid());
        if(pair != null){
            MutableText tag = pair.getFirst().copy();
            pair.getSecond().ifPresent(tag::setStyle);
            return Optional.of(tag);
        }
        return Optional.empty();
    }
    @Override
    public void readData(ReadView readView) {
        tags = readView.read("tags", Riftname.Codecs.PLAYER_TAG_CODEC).get();
        formattings = readView.read("formattings", Riftname.Codecs.PLAYER_FORMATTING_CODEC).get();
        nickNames = readView.read("nicknames", Riftname.Codecs.PLAYER_NICK_NAME_CODEC).get();
        hexColors = readView.read("hexcolors", Riftname.Codecs.PLAYER_TEXT_COLOR_CODEC).get();
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.put("tags", Riftname.Codecs.PLAYER_TAG_CODEC, tags);
        writeView.put("formattings", Riftname.Codecs.PLAYER_FORMATTING_CODEC, formattings);
        writeView.put("nicknames", Riftname.Codecs.PLAYER_NICK_NAME_CODEC, nickNames);
        writeView.put("hexcolors", Riftname.Codecs.PLAYER_TEXT_COLOR_CODEC, hexColors);
    }
}
