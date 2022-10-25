package com.moonsworth.apollo.impl.bukkit.command;

import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;
import com.moonsworth.apollo.api.Apollo;
import com.moonsworth.apollo.api.bridge.ApolloBlockPos;
import com.moonsworth.apollo.api.bridge.ApolloPlayer;
import com.moonsworth.apollo.api.module.impl.EVNTModule;
import com.moonsworth.apollo.api.module.impl.HeartTextureModule;
import com.moonsworth.apollo.api.module.impl.NotificationModule;
import com.moonsworth.apollo.api.module.impl.ServerRuleModule;
import com.moonsworth.apollo.api.protocol.*;
import com.moonsworth.apollo.impl.bukkit.ApolloBukkitPlatform;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class VignetteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify a vignette.");
            return true;
        }

        ApolloPlayer apolloPlayer = ApolloBukkitPlatform.getInstance().tryWrapPlayer(sender);
        if (apolloPlayer != null) {
            if (args[0].equals("reset")) {
                Apollo.getApolloModuleManager().getModule(EVNTModule.class)
                        .ifPresent(module -> module.displayVignette(apolloPlayer, "", 0.0f));
                sender.sendMessage(ChatColor.GREEN + "Reset vignette!");
                return true;
            }

            if(args[0].equals("changeCharacter")) {
                Apollo.getApolloModuleManager().getModule(EVNTModule.class)
                        .ifPresent(module -> module.updateCharacterResources(apolloPlayer, CharacterType.Froska, null, null, "lunar:event/models/MCL_Froska/MCL_Froska2.png"));
                Apollo.getApolloModuleManager().getModule(EVNTModule.class)
                        .ifPresent(module -> module.reloadCosmetics(apolloPlayer));
            }

            if (args[0].equals("characterSelection")) {
                Apollo.getApolloModuleManager().getModule(EVNTModule.class).ifPresent(module -> {
                    module.handleDemo();
                    var abilityMessage = CharacterAbilityMessage.newBuilder();
                    for (CharacterType value : CharacterType.values()) {
                        if (value == CharacterType.UNRECOGNIZED) {
                            continue;
                        }
                        var a = RenderableString.newBuilder().setColor(0xFFFF0000).setContent(ByteString.copyFromUtf8("Tester!"));
                        var b = RenderableString.newBuilder().setColor(0xFFFF5500).setContent(ByteString.copyFromUtf8("  * Ability 2!"));
                        var c = RenderableString.newBuilder().setColor(0xFFFF5555).setContent(ByteString.copyFromUtf8("Testing!"));
                        var d = RenderableString.newBuilder().setColor(0xFF00FF55).setContent(ByteString.copyFromUtf8("  * Ability 3!"));
                        var thing = CharacterAbility.newBuilder().setType(value).addAbilities(a).addAbilities(b).addAbilities(c).addAbilities(d);
                        abilityMessage.addAbilities(thing.build());
                    }
                    apolloPlayer.sendPacket(abilityMessage.build());
                    module.displayGui(apolloPlayer, OpenGuiMessage.Gui.CHARACTER_SELECTION);
                });
                return true;
            }

            if (args[0].equals("hud")) {
                Apollo.getApolloModuleManager().getModule(EVNTModule.class).ifPresent(module -> {
                    var teamStatus = EventTeamStatus.newBuilder()
                            .setBottomCrystalHealth(100)
                            .setBottomWitherHealth(100)
                            .setMiddleCrystalHealth(75)
                            .setMiddleWitherHealth(75)
                            .setTopCrystalHealth(50)
                            .setTopWitherHealth(50)
                            .build();
                    var status = EventGameStatusMessage.newBuilder()
                            .setTier3Health(100)
                            .setGameStartTime(System.currentTimeMillis())
                            .setTeamOneStatus(teamStatus)
                            .setTeamTwoStatus(teamStatus);
                    List<EventPlayerStatus> playerStatusList = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        playerStatusList.add(EventPlayerStatus.newBuilder().setPlayerId(ByteString.copyFromUtf8(apolloPlayer.getUniqueId().toString())).setHealth(i * 20).setUltimatePercentage(i * 3f).build());
                    }
                    apolloPlayer.sendPacket(EventPlayerStatusMessage.newBuilder().addAllTeamOneStatus(playerStatusList).addAllTeamTwoStatus(playerStatusList).build());
                    apolloPlayer.sendPacket(status.build());
                });
                return true;
            }

            if (args[0].equals("observer")) {
                var builder = CharacterOverviewMessage.newBuilder();
                for (int i = 0; i < 10; i++) {
                    builder.addPlayers(EventPlayer.newBuilder().setPlayerName(ByteString.copyFromUtf8("Player " + i)).setTeamOne(i % 2 == 0).setPlayerId(ByteString.copyFromUtf8(UUID.randomUUID().toString())).setCharacterSelected(CharacterType.values()[new Random().nextInt(0, 9)]).build());
                }
                apolloPlayer.sendPacket(builder.build());
                Apollo.getApolloModuleManager().getModule(EVNTModule.class).ifPresent(module -> {
                    module.displayGui(apolloPlayer, OpenGuiMessage.Gui.OBSERVER_UI);
                });
                return true;
            }

            if (args.length == 2 && args[0].equals("health")) {
                Integer value = Ints.tryParse(args[1]);
                if (value == null) {
                    sender.sendMessage(ChatColor.RED + "Not a valid number, use -1 for removal!");
                    return true;
                }
                Apollo.getApolloModuleManager().getModule(HeartTextureModule.class).ifPresent(module -> {
                    if (value == -1) {
                        module.clearHeartLocation(apolloPlayer);
                        sender.sendMessage(ChatColor.GREEN + "Clear Custom Health!");
                    } else {
                        module.setHeartXLocationOverride(apolloPlayer, value, false);
                        sender.sendMessage(ChatColor.GREEN + "Set X Loc Custom Health!");
                    }
                });
                return true;
            }
            if (args.length == 2 && args[0].equals("bright")) {
                Integer value = Ints.tryParse(args[1]);
                if (value == null) {
                    Boolean t = Boolean.parseBoolean(args[1]);
                    Apollo.getApolloModuleManager().getModule(ServerRuleModule.class).ifPresent(rule -> {
                        rule.getAffectBrightness().update(t);
                        sender.sendMessage(
                                ChatColor.YELLOW + "Setting value for brightness to " + rule.getAffectBrightness()
                                        .get());

                    });
                    return true;
                }
                Apollo.getApolloModuleManager().getModule(ServerRuleModule.class).ifPresent(rule -> {
                    rule.getBrightness().update(value);
                    sender.sendMessage(ChatColor.GREEN + "Setting value for brightness " + rule.getBrightness().get());

                });
                return true;
            }
            NamespacedKey textureLocation = NamespacedKey.fromString(args[0]);
            if (textureLocation == null) {
                sender.sendMessage(ChatColor.RED + "Please specify a valid texture location.");
                return true;
            }
            if (args.length == 4 && args[1].equals("notify")) {
                String title = args[2];
                String description = args[3];
                Apollo.getApolloModuleManager().getModule(NotificationModule.class).get()
                        .notifyAll(ChatColor.translateAlternateColorCodes('&', title),
                                ChatColor.translateAlternateColorCodes('&', description), textureLocation.toString());
                sender.sendMessage(ChatColor.GREEN + "Displayed notify!");

                return true;
            }
            if (args.length == 2 && args[1].equals("cooldown")) {
                apolloPlayer.sendPacket(
                        CooldownMessage.newBuilder().setDurationMs(8000).setName(ByteString.copyFromUtf8("Text"))
                                .setIconLocation(RenderableIcon.newBuilder()
                                        .setLocation(ByteString.copyFromUtf8(textureLocation.toString())).setSize(10)
                                        .build()).build());
                sender.sendMessage(ChatColor.GREEN + "Displayed cooldown!");
                return true;
            }

            Apollo.getApolloModuleManager().getModule(EVNTModule.class)
                    .ifPresent(module -> module.displayVignette(apolloPlayer, textureLocation.toString(), 1.0f));

            sender.sendMessage(ChatColor.GREEN + "Displayed vignette!");
        }
        return true;
    }
}
