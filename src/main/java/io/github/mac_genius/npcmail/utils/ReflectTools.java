package io.github.mac_genius.npcmail.utils;

import io.github.mac_genius.npcmail.NPCMail;

import java.lang.reflect.Field;

/**
 * Created by Mac on 3/27/2016.
 */
public class ReflectTools {
    public static void setObject(Object o, String field, Object value) {
        try {
            Class s = o.getClass();
            Field f = s.getDeclaredField(field);
            f.setAccessible(true);
            f.set(o, value);
            f.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            if (e instanceof IllegalAccessException) {
                NPCMail.getSingleton().getLogger().warning("Illegal access for " + field + " in " + o.getClass().getName() + "!");
            } else {
                NPCMail.getSingleton().getLogger().warning("No field called " + field + " in " + o.getClass().getName() + "!");
            }
        }
    }
}
