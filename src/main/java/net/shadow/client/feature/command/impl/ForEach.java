/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;
import net.shadow.client.helper.util.Utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForEach extends Command {
    final ExecutorService runner = Executors.newFixedThreadPool(1);
    String partial;
    boolean recieving;

    public ForEach() {
        super("ForEach", "Do something for each player", "foreach", "for", "fe");
        Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
            if (!recieving) return;
            PacketEvent pe = (PacketEvent) event;
            if (pe.getPacket() instanceof CommandSuggestionsS2CPacket packet) {
                Suggestions all = packet.getSuggestions();
                for (Suggestion i : all.getList()) {
                    String name = i.getText();
                    if (name.contains(ShadowMain.client.player.getName().toString())) continue;
                    ShadowMain.client.player.sendChatMessage(partial + " " + name);
                    message(partial + " " + name);
                }
                message("Foreach operation completed");
                recieving = false;
            }
        });
    }

    @Override
    public String[] getSuggestions(String fullCommand, String[] args) {
        if (args.length == 1) {
            return new String[]{"player", "tab"};
        } else if (args.length == 2) {
            return new String[]{"(delay)"};
        } else if (args.length == 3) {
            return new String[]{"(string)"};
        }
        return super.getSuggestions(fullCommand, args);
    }

    @Override
    public void onExecute(String[] args) {
        int delay = Utils.Math.tryParseInt(args[1], 0);
        switch (args[0]) {
            case "player" -> {
                for (PlayerListEntry playerListEntry : Objects.requireNonNull(ShadowMain.client.getNetworkHandler()).getPlayerList()) {
                    if (Utils.Players.isPlayerNameValid(playerListEntry.getProfile().getName()) && !playerListEntry.getProfile().getId()
                            .equals(Objects.requireNonNull(ShadowMain.client.player).getUuid())) {
                        runner.execute(() -> {
                            try {
                                ShadowMain.client.player.sendChatMessage(String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replaceAll("%s", playerListEntry.getProfile().getName()));
                                Thread.sleep(delay);
                            } catch (Exception ignored) {
                            }
                        });
                    }
                }
            }
            case "tab" -> {
                partial = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                ShadowMain.client.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, partial + " "));
                recieving = true;
            }
            default -> error("Argument 1 has to be either player or tab");
        }
    }
}
