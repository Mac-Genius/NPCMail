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
public class FinishMessagePrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public FinishMessagePrompt(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        player.sendRawMessage(MailTools.getColorPalette());
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        mail.setDoneMessage(ChatColor.translateAlternateColorCodes('&', s));
        mail.setLore(new ArrayList<>());
        return new LorePrompt("Please enter the lore. Do .done to finish or .remove to remove the last lore.", mail, player);
    }
}
