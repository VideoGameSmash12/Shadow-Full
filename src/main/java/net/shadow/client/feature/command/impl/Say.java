/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.ShadowMain;
import net.shadow.client.feature.command.Command;

import java.util.Objects;

public class Say extends Command {

    public Say() {
        super("Say", "Says something in chat", "say", "tell");
    }

    @Override
    public String[] getSuggestions(String fullCommand, String[] args) {
        if (args.length == 1) {
            return new String[]{"(message)"};
        }
        return super.getSuggestions(fullCommand, args);
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            error("not sure if i can say nothing");
            return;
        }
        Objects.requireNonNull(ShadowMain.client.player).sendChatMessage(String.join(" ", args));
    }
}
