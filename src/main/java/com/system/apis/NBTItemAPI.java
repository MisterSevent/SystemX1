package com.system.apis;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;

public class NBTItemAPI {
    private static final String NBT_KEY = "LEGITITEMCOMMAND";
    private static String nmsVersion = getNMSVersion();
    private static Class<?> craftMetaItemClass = getOBCClass("inventory.CraftMetaItem");
    private static Class<?> nbtTagCompound = getNMSClass("NBTTagCompound");
    private static Field unhandledTagsField;
    private static Method setString;
    private static Method getString;

    private NBTItemAPI() {
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + nmsVersion + "." + name);
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + nmsVersion + "." + name);
        } catch (ClassNotFoundException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static String getNMSVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split(Pattern.quote("."))[3];
    }

    public static void setNBTString(ItemMeta meta, String key, String value) {
        try {
            Object tag = nbtTagCompound.newInstance();
            setString.invoke(tag, key, value);
            ((Map) unhandledTagsField.get(meta)).put("LEGITITEMCOMMAND", tag);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException var4) {
            var4.printStackTrace();
        }

    }

    public static String getNBTString(ItemMeta meta, String key) {
        if (meta != null && key != null) {
            try {
                Object tag = ((Map) unhandledTagsField.get(meta)).get("LEGITITEMCOMMAND");
                return tag == null ? null : (String) getString.invoke(tag, key);
            } catch (InvocationTargetException | IllegalAccessException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    static {
        try {
            if (nbtTagCompound != null) {
                setString = nbtTagCompound.getMethod("setString", String.class, String.class);
                getString = nbtTagCompound.getMethod("getString", String.class);
            }
        } catch (NoSuchMethodException var2) {
            var2.printStackTrace();
        }

        try {
            if (craftMetaItemClass != null) {
                unhandledTagsField = craftMetaItemClass.getDeclaredField("unhandledTags");
                unhandledTagsField.setAccessible(true);
            }
        } catch (NoSuchFieldException var1) {
            var1.printStackTrace();
        }
    }
}
