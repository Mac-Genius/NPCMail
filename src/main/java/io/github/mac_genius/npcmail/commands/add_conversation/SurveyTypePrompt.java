package io.github.mac_genius.npcmail.commands.add_conversation;

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
public class SurveyTypePrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;
    private ArrayList<String> surveyType;

    public SurveyTypePrompt(String message, Mail mail, Player player) {
        this.message = message;
        this.mail = mail;
        this.player = player;
        surveyType = new ArrayList<>();
        surveyType.add("select");
        surveyType.add("response");
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return null;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (surveyType.contains(s.toLowerCase())) {
            ((Survey)mail).setSurveyType(s.toLowerCase());
            return new SurveyTypePrompt("Please enter the question that will be asked.", mail, player);
        } else {
            return new SurveyTypePrompt("Please use one of the listed types of surveys.", mail, player);
        }
    }
}
