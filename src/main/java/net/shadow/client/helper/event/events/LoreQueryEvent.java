/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.helper.event.events;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.shadow.client.helper.event.events.base.NonCancellableEvent;

import java.util.List;

public class LoreQueryEvent extends NonCancellableEvent {

    final ItemStack source;
    final List<Text> lore;

    public LoreQueryEvent(ItemStack stack, List<Text> currentLore) {
        this.source = stack;
        this.lore = currentLore;
    }

    public ItemStack getSource() {
        return source;
    }

    public List<Text> getLore() {
        return lore;
    }

    public void addLore(String v) {
        lore.add(Text.of(v));
    }

    public void addClientLore(String v) {
        addLore("§7" + v + "§r");
    }
}
