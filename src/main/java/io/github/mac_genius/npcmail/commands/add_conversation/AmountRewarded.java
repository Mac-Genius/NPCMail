package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.database.models.Survey;
import io.github.mac_genius.npcmail.database.models.Vote;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class AmountRewarded extends NumericPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public AmountRewarded(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, Number number) {
        mail.setWorth(number.intValue());
        if (mail instanceof Vote) {
            return new LinkPrompt("Please enter a link for players to vote at.", mail, player);
        } else if (mail instanceof Survey) {
            return new SurveyTypePrompt("Please enter the survey type. (select, response)", mail, player);
        } else {
            player.sendRawMessage("Setup complete!");
            NPCMail.getSingleton().getMail().addMail(mail);
            return null;
        }

    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }
}
