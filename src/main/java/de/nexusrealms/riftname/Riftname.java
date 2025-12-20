package de.nexusrealms.riftname;


import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.nexusrealms.riftname.command.FormattingArgumentType;
import de.nexusrealms.riftname.command.GradientArgumentType;
import de.nexusrealms.riftname.command.RiftnameCommands;
import de.nexusrealms.riftname.command.TextColorArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Uuids;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.function.Function;

public class Riftname implements ModInitializer, ScoreboardComponentInitializer {
	public static final String MOD_ID = "riftname";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ComponentKey<NameComponent> NAME_COMPONENT =
			ComponentRegistryV3.INSTANCE.getOrCreate(Identifier.of(MOD_ID, "name"), NameComponent.class);
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ArgumentTypeRegistry.registerArgumentType(Identifier.of(MOD_ID, "formatting"), FormattingArgumentType.class, ConstantArgumentSerializer.of(FormattingArgumentType::formatting));
		ArgumentTypeRegistry.registerArgumentType(Identifier.of(MOD_ID, "text_color"), TextColorArgumentType.class, ConstantArgumentSerializer.of(TextColorArgumentType::textColor));
		ArgumentTypeRegistry.registerArgumentType(Identifier.of(MOD_ID, "gradient"), GradientArgumentType.class, ConstantArgumentSerializer.of(GradientArgumentType::text));

		LOGGER.info(Codecs.createPairCodec(Codec.STRING, Codec.INT).encodeStart(JsonOps.INSTANCE, new Pair<>("hello", 12345)).getOrThrow().toString());
		CommandRegistrationCallback.EVENT.register(RiftnameCommands::registerCommands);
		LOGGER.info("Hello Fabric world!");
	}

	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry scoreboardComponentFactoryRegistry) {
		scoreboardComponentFactoryRegistry.registerScoreboardComponent(NAME_COMPONENT, (scoreboard, minecraftServer) -> new NameComponent());
	}

	public static class Codecs {
		private static <A, B> Codec<Pair<A, B>> createPairCodec(Codec<A> aCodec, Codec<B> bCodec){
			return RecordCodecBuilder.create(pairInstance -> pairInstance.group(
					aCodec.fieldOf("a").forGetter(Pair::getFirst),
					bCodec.fieldOf("b").forGetter(Pair::getSecond)
					).apply(pairInstance, Pair::new));
		}

		public static final Codec<Map<UUID,  Pair<Text, Boolean>>> PLAYER_NICK_NAME_CODEC = Codec.unboundedMap(Uuids.CODEC, createPairCodec(TextCodecs.CODEC, Codec.BOOL)).xmap(HashMap::new, Function.identity());;
		public static final Codec<Map<UUID, Style>> PLAYER_STYLE_CODEC = Codec.unboundedMap(Uuids.CODEC, Style.Codecs.CODEC).xmap(HashMap::new, Function.identity());
	}
}