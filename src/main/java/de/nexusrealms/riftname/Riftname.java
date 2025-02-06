package de.nexusrealms.riftname;


import com.google.common.base.Functions;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import de.nexusrealms.riftname.command.FormattingArgumentType;
import de.nexusrealms.riftname.command.RiftnameCommands;
import de.nexusrealms.riftname.command.TextColorArgumentType;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistryV3;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

		CommandRegistrationCallback.EVENT.register(RiftnameCommands::registerCommands);
		LOGGER.info("Hello Fabric world!");
	}

	@Override
	public void registerScoreboardComponentFactories(ScoreboardComponentFactoryRegistry scoreboardComponentFactoryRegistry) {
		scoreboardComponentFactoryRegistry.registerScoreboardComponent(NAME_COMPONENT, (scoreboard, minecraftServer) -> new NameComponent());
	}

	public static class Codecs {
		public static final Codec<Map<UUID, Either<Text, String>>> PLAYER_TAG_CODEC = Codec.unboundedMap(Uuids.CODEC, Codec.either(TextCodecs.CODEC, Codec.STRING)).xmap(HashMap::new, Function.identity());
		public static final Codec<Map<UUID, String>> PLAYER_NICK_NAME_CODEC = Codec.unboundedMap(Uuids.CODEC, Codec.STRING).xmap(HashMap::new, Function.identity());;
		public static final Codec<Map<UUID, TextColor>> PLAYER_TEXT_COLOR_CODEC = Codec.unboundedMap(Uuids.CODEC, TextColor.CODEC).xmap(HashMap::new, Function.identity());;
		public static final Codec<Map<UUID, List<Formatting>>> PLAYER_FORMATTING_CODEC = Codec.unboundedMap(Uuids.CODEC, Formatting.CODEC.listOf()).xmap(HashMap::new, Function.identity());;
	}
}