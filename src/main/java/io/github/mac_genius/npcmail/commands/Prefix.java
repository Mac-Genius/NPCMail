package io.github.mac_genius.npcmail.commands;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

/**
 * Created by Mac on 3/28/2016.
 */
public class Prefix implements ConversationPrefix {
    private String prefix;

    public Prefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix(ConversationContext conversationContext) {
        return prefix;
    }
}
