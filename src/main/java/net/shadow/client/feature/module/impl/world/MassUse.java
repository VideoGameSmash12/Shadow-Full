/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.world;


import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.event.EventType;
import net.shadow.client.helper.event.Events;
import net.shadow.client.helper.event.events.PacketEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MassUse extends Module {
    final List<Packet<?>> dontRepeat = new ArrayList<>();
    //    SliderValue     uses       = (SliderValue) this.config.create("Uses", 3, 1, 100, 0).description("How many times to use the item");
    final DoubleSetting uses = this.config.create(new DoubleSetting.Builder(3).name("Uses").description("How many times to use the item").min(1).max(100).precision(0).get());

    public MassUse() {
        super("MassUse", "Uses an item or block several times", ModuleType.WORLD);
        Events.registerEventHandler(EventType.PACKET_SEND, event -> {
            if (!this.isEnabled()) {
                return;
            }
            PacketEvent pe = (PacketEvent) event;
            if (dontRepeat.contains(pe.getPacket())) {
                dontRepeat.remove(pe.getPacket());
                return;
            }
            if (pe.getPacket() instanceof PlayerInteractBlockC2SPacket p1) {
                PlayerInteractBlockC2SPacket pp = new PlayerInteractBlockC2SPacket(p1.getHand(), p1.getBlockHitResult());
                for (int i = 0; i < uses.getValue(); i++) {
                    dontRepeat.add(pp);
                    Objects.requireNonNull(client.getNetworkHandler()).sendPacket(pp);
                }
            } else if (pe.getPacket() instanceof PlayerInteractItemC2SPacket p1) {
                PlayerInteractItemC2SPacket pp = new PlayerInteractItemC2SPacket(p1.getHand());
                for (int i = 0; i < uses.getValue(); i++) {
                    dontRepeat.add(pp);
                    Objects.requireNonNull(client.getNetworkHandler()).sendPacket(pp);
                }
            }
        });
    }

    @Override
    public void tick() {

    }

    @Override
    public void enable() {

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

