package com.tonywww.dustandash.cthulhu.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.tonywww.dustandash.cthulhu.api.BossFightAPI;
import com.tonywww.dustandash.cthulhu.api.GraphemeAPI;
import com.tonywww.dustandash.cthulhu.api.HealthDrainAPI;
import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import com.tonywww.dustandash.cthulhu.entity.CthulhuBossPhase1Entity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuGraphemeEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuLawFieldEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuPillarEntity;
import com.tonywww.dustandash.cthulhu.entity.CthulhuStormGolemEntity;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import com.tonywww.dustandash.cthulhu.grapheme.WordDictionary;
import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import com.tonywww.dustandash.cthulhu.render.CthulhuMinionRenderHelper;
import com.tonywww.dustandash.registeries.ModEntites;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Optional;

public final class CthulhuCommand {

    private CthulhuCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("cthulhu")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("start")
                        .executes(context -> start(context.getSource())))
                .then(Commands.literal("stop")
                        .executes(context -> stopAll(context.getSource()))
                        .then(Commands.literal("current")
                                .executes(context -> stopCurrent(context.getSource()))))
                .then(Commands.literal("status")
                        .executes(context -> status(context.getSource())))
                .then(Commands.literal("phase")
                        .then(Commands.argument("phase", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        new String[]{"PHASE_1", "PHASE_2", "PHASE_3", "FINAL_TRUTH"},
                                        builder
                                ))
                                .executes(context -> phase(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "phase")
                                ))))
                .then(Commands.literal("giveletter")
                        .then(Commands.argument("letter", StringArgumentType.word())
                                .executes(context -> giveLetter(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "letter")
                                ))))
                .then(Commands.literal("letters")
                        .then(Commands.literal("status")
                                .executes(context -> lettersStatus(context.getSource())))
                        .then(Commands.literal("clear")
                                .executes(context -> lettersClear(context.getSource()))))
                .then(Commands.literal("spell")
                        .then(Commands.argument("word", StringArgumentType.greedyString())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        new String[]{"VOID", "VITAL", "SHIELD", "BREAK", "DELETE", "MODIFY", "EXIST", "REALITY", "REALITY EXISTS"},
                                        builder
                                ))
                                .executes(context -> spell(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "word")
                                ))))
                .then(Commands.literal("drain")
                        .then(Commands.literal("vitality")
                                .executes(context -> drainVitality(context.getSource())))
                        .then(Commands.literal("soul")
                                .executes(context -> drainSoul(context.getSource())))
                        .then(Commands.literal("clear")
                                .executes(context -> clearDrain(context.getSource())))
                        .then(Commands.literal("status")
                                .executes(context -> drainStatus(context.getSource()))))
                .then(Commands.literal("spawn")
                        .then(Commands.literal("boss1")
                                .executes(context -> spawnBoss1(context.getSource())))
                        .then(Commands.literal("storm")
                                .executes(context -> spawnStorm(context.getSource())))
                        .then(Commands.literal("pillar")
                                .then(Commands.argument("letters", StringArgumentType.word())
                                        .executes(context -> spawnPillar(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "letters"),
                                                "GENERIC"
                                        ))
                                        .then(Commands.argument("type", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                        new String[]{"GENERIC", "FAMINE", "ZERO", "DESPAIR", "EROSION"},
                                                        builder
                                                ))
                                                .executes(context -> spawnPillar(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "letters"),
                                                        StringArgumentType.getString(context, "type")
                                                )))))
                        .then(Commands.literal("grapheme")
                                .then(Commands.argument("letter", StringArgumentType.word())
                                        .executes(context -> spawnGrapheme(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "letter")
                                        ))))
                        .then(Commands.literal("field")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                new String[]{"SILENCE", "EVAPORATION", "VOID", "STASIS"},
                                                builder
                                        ))
                                        .then(Commands.argument("letter", StringArgumentType.word())
                                                .executes(context -> spawnField(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "type"),
                                                        StringArgumentType.getString(context, "letter")
                                                )))))
                        .then(Commands.literal("minion")
                                .then(Commands.argument("entity", StringArgumentType.word())
                                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                new String[]{"zombie", "spider", "skeleton", "enderman"},
                                                builder
                                        ))
                                        .then(Commands.argument("mode", StringArgumentType.word())
                                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                                        new String[]{"noise", "wireframe", "text_static"},
                                                        builder
                                                ))
                                                .executes(context -> spawnMinion(
                                                        context.getSource(),
                                                        StringArgumentType.getString(context, "entity"),
                                                        StringArgumentType.getString(context, "mode")
                                                ))))));
    }

    private static int start(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BossFightInstance instance = BossFightAPI.startFight((ServerLevel) player.level(), player.blockPosition());
        source.sendSuccess(() -> Component.literal("Started Azathoth fight at " + instance.corePos().toShortString()), true);
        return 1;
    }

    private static int stopAll(CommandSourceStack source) {
        int stopped = BossFightManager.get().terminateAll(source.getServer());
        if (stopped <= 0) {
            source.sendFailure(Component.literal("No active Azathoth fights."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Stopped " + stopped + " Azathoth fight(s)."), true);
        return stopped;
    }

    private static int stopCurrent(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BossFightInstance instance = BossFightAPI.getActiveFight((ServerLevel) player.level());
        if (instance == null) {
            source.sendFailure(Component.literal("No active Azathoth fight in this dimension."));
            return 0;
        }

        BossFightAPI.terminateFight(instance);
        source.sendSuccess(() -> Component.literal("Stopped Azathoth fight."), true);
        return 1;
    }

    private static int status(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BossFightInstance instance = BossFightAPI.getActiveFight((ServerLevel) player.level());
        if (instance == null) {
            source.sendFailure(Component.literal("No active Azathoth fight in this dimension."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(
                "Azathoth phase=" + instance.phase().name()
                        + ", participants=" + instance.participants().size()
                        + ", age=" + instance.age()
        ), false);
        return 1;
    }

    private static int phase(CommandSourceStack source, String phaseName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BossFightInstance instance = BossFightAPI.getActiveFight((ServerLevel) player.level());
        if (instance == null) {
            source.sendFailure(Component.literal("No active Azathoth fight in this dimension."));
            return 0;
        }

        try {
            FightPhase phase = FightPhase.byName(phaseName);
            if (phase == FightPhase.TERMINATED) {
                source.sendFailure(Component.literal("Use /cthulhu stop to terminate the fight."));
                return 0;
            }
            instance.setPhase(phase);
            BossFightManager.get().save(player.server);
            source.sendSuccess(() -> Component.literal("Set Azathoth phase to " + phase.name()), true);
            return 1;
        } catch (IllegalArgumentException exception) {
            source.sendFailure(Component.literal("Unknown phase: " + phaseName));
            return 0;
        }
    }

    private static int giveLetter(CommandSourceStack source, String letterText) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (letterText.isEmpty()) {
            source.sendFailure(Component.literal("Letter cannot be empty."));
            return 0;
        }

        GraphemeAPI.giveLetter(player, letterText.charAt(0));
        source.sendSuccess(() -> Component.literal("Granted grapheme " + Character.toUpperCase(letterText.charAt(0))), false);
        return 1;
    }

    private static int lettersStatus(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BossFightInstance instance = BossFightManager.get().findParticipantFight(player);
        if (instance == null) {
            source.sendFailure(Component.literal("You are not in an active Azathoth fight."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Graphemes: " + instance.letterInventory(player.getUUID())), false);
        return 1;
    }

    private static int lettersClear(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        BossFightInstance instance = BossFightManager.get().findParticipantFight(player);
        if (instance == null) {
            source.sendFailure(Component.literal("You are not in an active Azathoth fight."));
            return 0;
        }

        instance.clearLetters(player);
        BossFightManager.get().save(player.server);
        source.sendSuccess(() -> Component.literal("Cleared graphemes."), false);
        return 1;
    }

    private static int spell(CommandSourceStack source, String word) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        String normalized = WordDictionary.normalize(word);
        if (!WordDictionary.isKnown(normalized)) {
            source.sendFailure(Component.literal("Unknown law word: " + normalized));
            return 0;
        }

        BossFightInstance instance = BossFightAPI.getActiveFight((ServerLevel) player.level());
        if (instance == null) {
            source.sendFailure(Component.literal("No active Azathoth fight in this dimension."));
            return 0;
        }
        if (!instance.isTypingReady(player)) {
            source.sendFailure(Component.literal("Typing cooldown: " + instance.getTypingCooldownLeft(player) + " tick(s)."));
            return 0;
        }

        boolean success = GraphemeAPI.trySpell(player, normalized, p -> WordDictionary.apply(p, normalized));
        if (!success) {
            source.sendFailure(Component.literal("Missing graphemes for: " + normalized));
            return 0;
        }

        instance.markTypingCooldown(player);
        if (instance != null && instance.phase() == FightPhase.FINAL_TRUTH && "REALITY EXISTS".equals(normalized)
                && instance.submitFinalTruth(player)) {
            BossFightAPI.terminateFight(instance);
        }

        source.sendSuccess(() -> Component.literal("Spelled law word: " + normalized), true);
        return 1;
    }

    private static int drainVitality(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        HealthDrainAPI.applyVitalityDrain(player, CthulhuConfig.VITALITY_DRAIN_PERCENT);
        source.sendSuccess(() -> Component.literal("Applied vitality drain. Max health=" + player.getMaxHealth()), true);
        return 1;
    }

    private static int drainSoul(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        HealthDrainAPI.applySoulWither(player, CthulhuConfig.SOUL_WITHER_PER_MISSING);
        source.sendSuccess(() -> Component.literal("Applied soul wither. Max health=" + player.getMaxHealth()), true);
        return 1;
    }

    private static int clearDrain(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        HealthDrainAPI.clearAllDrain(player);
        source.sendSuccess(() -> Component.literal("Cleared Azathoth health drain."), true);
        return 1;
    }

    private static int drainStatus(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        source.sendSuccess(() -> Component.literal(
                "vitality=" + HealthDrainAPI.getVitalityDrain(player)
                        + ", soul=" + HealthDrainAPI.getSoulWither(player)
                        + ", maxHealth=" + player.getMaxHealth()
        ), false);
        return 1;
    }

    private static int spawnPillar(CommandSourceStack source, String letters, String typeName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = (ServerLevel) player.level();
        CthulhuPillarEntity pillar = new CthulhuPillarEntity(ModEntites.CTHULHU_PILLAR.get(), level);
        pillar.setDropLetters(letters);
        pillar.setPillarType(CthulhuPillarEntity.PillarType.byName(typeName));
        pillar.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0.0f);
        level.addFreshEntity(pillar);
        source.sendSuccess(() -> Component.literal("Spawned " + pillar.getPillarType().name() + " Cthulhu pillar with letters " + pillar.getDropLetters()), true);
        return 1;
    }

    private static int spawnBoss1(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = (ServerLevel) player.level();
        CthulhuBossPhase1Entity boss = new CthulhuBossPhase1Entity(ModEntites.CTHULHU_BOSS_PHASE1.get(), level);
        boss.moveTo(player.getX(), player.getY() + 3.0d, player.getZ(), player.getYRot(), 0.0f);
        level.addFreshEntity(boss);
        source.sendSuccess(() -> Component.literal("Spawned Azathoth phase-1 boss."), true);
        return 1;
    }

    private static int spawnStorm(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = (ServerLevel) player.level();
        CthulhuStormGolemEntity stormGolem = new CthulhuStormGolemEntity(ModEntites.CTHULHU_STORM_GOLEM.get(), level);
        stormGolem.moveTo(player.getX(), player.getY() + 4.0d, player.getZ(), player.getYRot(), 0.0f);
        level.addFreshEntity(stormGolem);
        source.sendSuccess(() -> Component.literal("Spawned Cthulhu text storm."), true);
        return 1;
    }

    private static int spawnGrapheme(CommandSourceStack source, String letterText) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (letterText.isEmpty()) {
            source.sendFailure(Component.literal("Letter cannot be empty."));
            return 0;
        }

        ServerLevel level = (ServerLevel) player.level();
        CthulhuGraphemeEntity grapheme = new CthulhuGraphemeEntity(ModEntites.CTHULHU_GRAPHEME.get(), level);
        grapheme.setLetter(letterText.charAt(0));
        grapheme.moveTo(player.getX(), player.getY() + 1.0d, player.getZ(), player.getYRot(), 0.0f);
        level.addFreshEntity(grapheme);
        source.sendSuccess(() -> Component.literal("Spawned grapheme " + grapheme.getLetter()), true);
        return 1;
    }

    private static int spawnField(CommandSourceStack source, String typeName, String letterText) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (letterText.isEmpty()) {
            source.sendFailure(Component.literal("Letter cannot be empty."));
            return 0;
        }

        CthulhuLawFieldEntity.LawFieldType type;
        try {
            type = CthulhuLawFieldEntity.LawFieldType.valueOf(typeName.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            source.sendFailure(Component.literal("Unknown law field type: " + typeName));
            return 0;
        }

        ServerLevel level = (ServerLevel) player.level();
        CthulhuLawFieldEntity field = new CthulhuLawFieldEntity(ModEntites.CTHULHU_LAW_FIELD.get(), level);
        field.setFieldType(type);
        field.setDropLetter(letterText.charAt(0));
        field.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0.0f);
        level.addFreshEntity(field);
        source.sendSuccess(() -> Component.literal("Spawned law field " + type.name() + " with letter " + field.getDropLetter()), true);
        return 1;
    }

    private static int spawnMinion(CommandSourceStack source, String entityName, String modeName) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Optional<CthulhuRenderMode> renderMode = CthulhuRenderMode.fromName(modeName);
        if (renderMode.isEmpty()) {
            source.sendFailure(Component.literal("Unknown Cthulhu render mode: " + modeName));
            return 0;
        }

        ResourceLocation entityId = parseEntityId(entityName);
        if (entityId == null) {
            source.sendFailure(Component.literal("Invalid entity type id: " + entityName));
            return 0;
        }

        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityId);
        if (entityType == null) {
            source.sendFailure(Component.literal("Unknown entity type: " + entityName));
            return 0;
        }

        ServerLevel level = (ServerLevel) player.level();
        Entity entity = entityType.create(level);
        if (!(entity instanceof LivingEntity livingEntity)) {
            source.sendFailure(Component.literal("Entity is not a living minion: " + entityId));
            return 0;
        }

        livingEntity.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0.0f);
        if (livingEntity instanceof Mob mob) {
            mob.setPersistenceRequired();
        }
        level.addFreshEntity(livingEntity);
        CthulhuMinionRenderHelper.setRenderMode(livingEntity, renderMode.get());
        source.sendSuccess(() -> Component.literal(
                "Spawned " + entityId + " with cthulhu_render=" + renderMode.get().serializedName()
        ), true);
        return 1;
    }

    private static ResourceLocation parseEntityId(String entityName) {
        String normalized = entityName.toLowerCase(Locale.ROOT);
        if (!normalized.contains(":")) {
            normalized = "minecraft:" + normalized;
        }
        return ResourceLocation.tryParse(normalized);
    }
}
