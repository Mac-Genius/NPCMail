package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.utils.MailTools;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class DisplayNamePrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public DisplayNamePrompt(String message, Mail mail, Player player) {
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
        mail.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));
        return new FinishMessagePrompt("Please enter what you want this to say when clicked.", mail, player);
    }
}
