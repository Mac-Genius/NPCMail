package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.database.models.Vote;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class LinkPrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public LinkPrompt(String message, Mail mail, Player player) {
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
        ((Vote)mail).setLink(s);
        player.sendRawMessage("Setup complete!");
        NPCMail.getSingleton().getMail().addMail(mail);
        return null;
    }
}
