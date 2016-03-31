package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.utils.MailTools;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 3/28/2016.
 */
public class LorePrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public LorePrompt(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        player.sendRawMessage(MailTools.getColorPalette());
        player.sendRawMessage("Current Lore:");
        for (String s : mail.getLore()) {
            player.sendRawMessage(s);
        }
        player.sendRawMessage("______________________");
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(".done")) {
            return new ExpirePrompt("Enter when you want this to expire or 0 to never expire. ex: 7d14h37m59s", mail, player);
        } else if (s.equalsIgnoreCase(".remove")) {
            mail.removeLast();
            return new LorePrompt("Last lore removed! Please enter another.", mail, player);
        } else {
            mail.addLore(ChatColor.translateAlternateColorCodes('&', s));
            return new LorePrompt("Please enter another.", mail, player);
        }
    }
}
