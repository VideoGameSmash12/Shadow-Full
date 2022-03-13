/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Plugins extends Command {

    private boolean pendingCmdTree = false;

    public Plugins() {
        super("Plugins", "Finds server plugins via command suggestions", "pl", "plugins");
        if (ShadowMain.client.getNetworkHandler() != null) {
            Events.registerEventHandler(EventType.PACKET_RECEIVE, event -> {
                if (!pendingCmdTree) {
                    return;
                }
                PacketEvent packetEvent = (PacketEvent) event;
                if (packetEvent.getPacket() instanceof CommandSuggestionsS2CPacket cmdTree) {
                    pendingCmdTree = false;
                    Suggestions suggestions = cmdTree.getSuggestions();
                    Set<String> plugins = new HashSet<>();
                    for (Suggestion suggestion : suggestions.getList()) {
                        String[] cmd = suggestion.getText().split(":");
                        if (cmd.length > 1) {
                            plugins.add(cmd[0]);
                        }
                    }
                    if (plugins.isEmpty()) {
                        error("No plugins found");
                        return;
                    }
                    Iterator<String> iterator = plugins.iterator();
                    StringBuilder message = new StringBuilder(iterator.next());
                    while (iterator.hasNext()) {
                        message.append(", ").append(iterator.next());
                    }
                    message(message.toString());
                }

            });
        }
    }

    @Override
    public void onExecute(String[] args) {
        if (ShadowMain.client.getNetworkHandler() != null) {
            ShadowMain.client.getNetworkHandler().sendPacket(new RequestCommandCompletionsC2SPacket(0, "/"));
            pendingCmdTree = true;
        }
    }
}
