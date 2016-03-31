package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.database.models.Survey;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 3/28/2016.
 */
public class QuestionPrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public QuestionPrompt(String message, Mail mail, Player player) {
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
        ((Survey)mail).setQuestion(s);
        if (((Survey)mail).getSurveyType().equalsIgnoreCase("response")) {
            player.sendRawMessage("Setup complete!");
            ((Survey) mail).setAnswers(new ArrayList<>());
            NPCMail.getSingleton().getMail().addMail(mail);
            return null;
        } else {
            return new AnswerPrompt("Please enter an answer to your question", mail, player);
        }
    }
}
