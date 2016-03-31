package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class NamePrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public NamePrompt(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        for (Mail m : NPCMail.getSingleton().getMail().getAllActiveMail()) {
            if (m.getName().equalsIgnoreCase(s)) {
                return new NamePrompt("That name has already been taken. Please choose another", mail, player);
            }
        }
        mail.setName(s);
        return new DisplayNamePrompt("Please enter a display name for the mail.", mail, player);
    }
}
