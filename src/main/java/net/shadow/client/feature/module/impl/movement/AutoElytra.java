/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.feature.module.impl.movement;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.shadow.client.ShadowMain;
import net.shadow.client.feature.config.DoubleSetting;
import net.shadow.client.feature.module.Module;
import net.shadow.client.feature.module.ModuleType;
import net.shadow.client.helper.util.Utils;

import java.util.Objects;

public class AutoElytra extends Module {

    //    final SliderValue fallDist = (SliderValue) this.config.create("Fall distance", 3, 2, 10, 1).description("How far to fall for the elytra to equip");

    final DoubleSetting fallDist = this.config.create(new DoubleSetting.Builder(3).name("Fall distance").description("How long to fall for the elytra to equip").min(2).max(10).precision(1).get());

    public AutoElytra() {
        super("AutoElytra", "Automatically equips an elytra from your inventory if you fell long enough", ModuleType.MOVEMENT);
    }

    boolean equippedElytra() {
        return Objects.requireNonNull(ShadowMain.client.player).getInventory().armor.get(2).getItem() == Items.ELYTRA;
    }

    @Override
    public void tick() {
        if (Objects.requireNonNull(ShadowMain.client.player).fallDistance > fallDist.getValue()) {
            if (!equippedElytra()) { // do we not have an elytra equipped?
                for (int i = 0; i < (9 * 4 + 1); i++) { // gotta equip
                    ItemStack stack = ShadowMain.client.player.getInventory().getStack(i); // is it an elytra?
                    if (stack.getItem() == Items.ELYTRA) {
                        Utils.Inventory.moveStackToOther(Utils.Inventory.slotIndexToId(i), 6); // equip
                        break; // we found the item, cancel the loop
                    }
                }
            }
        }
    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public String getContext() {
        float fd = Objects.requireNonNull(ShadowMain.client.player).fallDistance;
        if (fd > fallDist.getMin()) {
            return Utils.Math.roundToDecimal(fd, 1) + " | " + fallDist.getValue();
        }
        return null;
    }

    @Override
    public void onWorldRender(MatrixStack matrices) {

    }

    @Override
    public void onHudRender() {

    }
}

