/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.helper.nbt;

import net.minecraft.nbt.NbtCompound;

import java.util.Arrays;

public class NbtList extends NbtElement {
    String name;
    NbtElement[] children;

    public NbtList(String name, NbtElement... children) {
        this.name = name;
        this.children = children;
    }

    @Override
    public String toString() {
        return "NbtArray{" +
                "name='" + name + '\'' +
                ", children=" + Arrays.toString(children) +
                '}';
    }

    @Override
    public void serialize(NbtCompound compound) {
        compound.put(name, get());
    }

    @Override
    public net.minecraft.nbt.NbtElement get() {
        net.minecraft.nbt.NbtList s = new net.minecraft.nbt.NbtList();
        for (NbtElement child : children) {
            s.add(child.get());
        }
        return s;
    }
}
