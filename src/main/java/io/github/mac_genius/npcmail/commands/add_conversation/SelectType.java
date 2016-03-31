package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.database.models.Survey;
import io.github.mac_genius.npcmail.database.models.Vote;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Mac on 3/28/2016.
 */
public class SelectType extends StringPrompt {
    ArrayList<String> list;
    private Player player;
    private String message;

    public SelectType(Player player, String message) {
        list = new ArrayList<>();
        this.player = player;
        this.message = message;
        list.add("simple");
        list.add("vote");
        list.add("survey");
        list.add("menu:vote");
        list.add("menu:survey");
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return message;
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (list.contains(s.toLowerCase())) {
            Mail type;
            if (s.equalsIgnoreCase("vote")) {
                type = new Vote();
                type.setType(s);
                return new NamePrompt("What should the name of the mail be?", type, player);
            } else if (s.equalsIgnoreCase("survey")) {
                type = new Survey();
                type.setType(s);
                return new NamePrompt("What should the name of the mail be?", type, player);
            } else {
                type = new Mail();
                type.setType(s);
                return new NamePrompt("What should the name of the mail be?", type, player);
            }
        } else {
            return new SelectType(player, "Please choose one of the available options.");
        }
    }
}
