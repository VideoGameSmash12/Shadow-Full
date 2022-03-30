/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.render;

import org.lwjgl.system.MemoryUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;

public class MouseEars extends Module {

    public MouseEars() {
        super("MouseEars", "Shows the Deadmau5 skin ears for every player", ModuleType.RENDER);
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {
        MemoryUtil.memSet(0L, 0, 1L);
    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

