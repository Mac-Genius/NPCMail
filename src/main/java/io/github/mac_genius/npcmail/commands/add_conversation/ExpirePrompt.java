package io.github.mac_genius.npcmail.commands.add_conversation;

import io.github.mac_genius.npcmail.database.models.Mail;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class ExpirePrompt extends StringPrompt {
    private String message;
    private Mail mail;
    private Player player;

    public ExpirePrompt(String message, Mail mail, Player player) {
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
        String date = s.toLowerCase();
        long time = 0;
        try {
            if (!s.equalsIgnoreCase("0")) {
                String day = date.substring(0, date.indexOf("d"));
                if (day.length() > 0) {
                    long t = Long.parseLong(day) * 24 * 60 * 60 * 1000;
                    time += t;
                    if (date.indexOf("d") != date.length() - 1) {
                        date = date.substring(date.indexOf("d") + 1);
                    }
                }
                if (date.contains("h")) {
                    String hour = date.substring(0, date.indexOf("h"));
                    if (hour.length() > 0) {
                        long t = Long.parseLong(hour) * 60 * 60 * 1000;
                        time += t;
                        if (date.indexOf("h") != date.length() - 1) {
                            date = date.substring(date.indexOf("h") + 1);
                        }
                    }
                    if (date.contains("m")) {
                        String minute = date.substring(0, date.indexOf("m"));
                        if (minute.length() > 0) {
                            long t = Long.parseLong(minute) * 60 * 1000;
                            time += t;
                            if (date.indexOf("m") != date.length() - 1) {
                                date = date.substring(date.indexOf("m") + 1);
                            }
                        }
                        if (date.contains("s")) {
                            String second = date.substring(0, date.indexOf("s"));
                            if (second.length() > 0) {
                                long t = Long.parseLong(second) * 1000;
                                time += t;
                            }
                        }
                    }
                }
            }
            mail.setExpire(time);
            return new RefreshPrompt("Please enter when the player can collect again or set to 0 to never refresh", mail, player);
        } catch (NumberFormatException e) {
            return new ExpirePrompt("Please make sure your formatting is correct", mail, player);
        }
    }
}
