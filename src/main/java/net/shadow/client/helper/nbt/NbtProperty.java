/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.helper.nbt;

import net.minecraft.nbt.*;

import java.util.UUID;

public class NbtProperty extends net.shadow.client.helper.nbt.NbtElement {
    String name;
    Object val;

    public NbtProperty(String name, Object value) {
        this.name = name;
        this.val = value;
    }

    public NbtProperty(Object value) {
        this("", value);
    }

    @Override
    public String toString() {
        return "NbtProperty{" +
                "name='" + name + '\'' +
                ", val=" + val +
                '}';
    }

    @Override
    public void serialize(NbtCompound compound) {
        if (val instanceof UUID nu) {
            compound.putUuid(name, nu);
        } else if (val instanceof Boolean b) {
            compound.putBoolean(name, b);
        } else compound.put(name, get());
    }

    @Override
    public net.minecraft.nbt.NbtElement get() {
        if (val instanceof String s) {
            return NbtString.of(s);
        } else if (val instanceof Integer i) {
            return NbtInt.of(i);
        } else if (val instanceof Long l) {
            return NbtLong.of(l);
        } else if (val instanceof Double d) {
            return NbtDouble.of(d);
        } else if (val instanceof Float f) {
            return NbtFloat.of(f);
        } else if (val instanceof Byte b) {
            return NbtByte.of(b);
        } else if (val instanceof Short s) {
            return NbtShort.of(s);
        } else return null; // no nbt representation of it
    }
}
