package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.database.models.Mail;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class SlotPrompt extends NumericPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public SlotPrompt(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        mail.setSlot(number.intValue());
        return new AmountRewarded("How many tokoins should the player receive?", mail, player);
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }
}
