/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.command.impl;

import net.shadow.client.feature.command.Command;

public class Test extends Command {
    public Test() {
        super("Test", "REAL", "test");
    }



    @Override
    public void onExecute(String[] args) {
    }
}
