package io.github.mac_genius.npcmail.commands;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.commands.add_conversation.SelectType;
import io.github.mac_genius.npcmail.inventories.MainMenuChange;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

/**
 * Created by Mac on 3/28/2016.
 */
public class NPCMail_Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("npcmail")) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (strings.length == 0) {
                    help(player, command);
                } else {
                    if (strings.length == 1) {
                        String arg1 = strings[0].toLowerCase();
                        switch (arg1) {
                            case "addmail":
                                addMail(player, command);
                                break;
                            case "editmenu":
                                editMenu(player, command);
                                break;
                            default:
                                help(player, command);
                                break;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addMail(Player player, Command command) {
        if (player.hasPermission("npcmail.addmail")) {
            ConversationFactory factory =
                    new ConversationFactory(NPCMail.getSingleton())
                            .withTimeout(60)
                            .withEscapeSequence("cancel")
                            .withPrefix(new Prefix("Adding Mail: "));
            Conversation conversation = factory.withFirstPrompt(
                    new SelectType(player, "What kind of mail should this be? (simple, vote, survey, menu:vote, menu:survey)"))
                    .withLocalEcho(false).buildConversation(player);
            conversation.begin();
        } else {
            player.sendMessage(command.getPermissionMessage());
        }
    }

    private void help(Player player, Command command) {

    }

    private void editMenu(Player player, Command command) {
        if (player.hasPermission("npcmail.editmenu")) {
            NPCMail.getSingleton().getServer().getPluginManager().registerEvents(new MainMenuChange(player), NPCMail.getSingleton());
        } else {
            player.sendMessage(command.getPermissionMessage());
        }
    }
}
