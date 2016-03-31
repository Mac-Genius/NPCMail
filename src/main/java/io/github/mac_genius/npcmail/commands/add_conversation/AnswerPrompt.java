package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.database.models.Survey;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class AnswerPrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public AnswerPrompt(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        player.sendRawMessage("Current Answers:");
        for (String s : ((Survey)mail).getAnswers()) {
            player.sendRawMessage(s);
        }
        player.sendRawMessage("______________________");
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (s.equalsIgnoreCase(".done")) {
            player.sendRawMessage("Setup complete!");
            NPCMail.getSingleton().getMail().addMail(mail);
            return null;
        } else if (s.equalsIgnoreCase(".remove")) {
            ((Survey)mail).removeLastAnswer();
            return new AnswerPrompt("Last answer removed! Please enter another.", mail, player);
        } else {
            ((Survey)mail).addAnswer(s);
            return new AnswerPrompt("Please enter another.", mail, player);
        }
    }
}
