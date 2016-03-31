package io.github.mac_genius.npcmail.database.cache;

import io.github.mac_genius.npcmail.NPCMail;
import io.github.mac_genius.npcmail.database.models.Mail;
import io.github.mac_genius.npcmail.utils.MailTools;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mac on 3/28/2016.
 */
public class PlayerMail {
    private HashMap<String, Long> lastSelected;

    public PlayerMail() {
        lastSelected = new HashMap<>();
    }

    public ItemStack[] getMainMenu() {
        ArrayList<Mail> menu = new ArrayList<>();
        for (Mail m : NPCMail.getSingleton().getMail().getAllActiveMail()) {
            if (m.getType().equalsIgnoreCase("simple") || m.getType().toLowerCase().startsWith("menu:")) {
                menu.add(m);
            }
        }
        ItemStack[] items = new ItemStack[27];
        int index = 0;
        for (int i = 0; i < menu.size(); i++) {
            if (items[i] == null) {
                if (menu.get(index).getSlot() < 0) {
                    items[i] = MailTools.createItem(menu.get(index), lastSelected.get(menu.get(index).getName()));
                } else {
                    if (items[menu.get(index).getSlot()] == null) {
                        items[menu.get(index).getSlot()] = MailTools.createItem(menu.get(index), lastSelected.get(menu.get(index).getName()));
                    } else {
                        ItemStack stack = MailTools.createItem(menu.get(index), lastSelected.get(menu.get(index).getName()));
                        if (stack != null) {
                            ItemStack move = items[menu.get(index).getSlot()];
                            items[menu.get(index).getSlot()] = stack;
                            items[i] = move;
                        }
                    }
                }
                index++;
            }
        }
        return items;
    }

    public ItemStack[] getVoteMenu() {
        ArrayList<Mail> menu = new ArrayList<>();
        for (Mail m : NPCMail.getSingleton().getMail().getAllActiveMail()) {
            if (m.getType().equalsIgnoreCase("vote")) {
                menu.add(m);
            }
        }
        ItemStack[] items = new ItemStack[27];
        int index = 0;
        for (int i = 0; i < menu.size(); i++) {
            items[i] = MailTools.createItem(menu.get(i), lastSelected.get(menu.get(i).getName()));
        }
        return items;
    }

    public ItemStack[] getSurveyMenu() {
        ArrayList<Mail> menu = new ArrayList<>();
        for (Mail m : NPCMail.getSingleton().getMail().getAllActiveMail()) {
            if (m.getType().equalsIgnoreCase("survey")) {
                menu.add(m);
            }
        }
        ItemStack[] items = new ItemStack[27];
        int index = 0;
        for (int i = 0; i < menu.size(); i++) {
            items[i] = MailTools.createItem(menu.get(i), lastSelected.get(menu.get(i).getName()));
        }
        return items;
    }

    public long getWhenSelected(String mailName) {
        return lastSelected.get(mailName);
    }

    public void addMail(Mail mail) {
        lastSelected.put(mail.getName(), (long)0);
    }

    public void addMail(String mailName, long lastClicked) {
        lastSelected.put(mailName, lastClicked);
    }

    public void addMail(Mail mail, long lastClicked) {
        lastSelected.put(mail.getName(), lastClicked);
    }

    public void removeMail(String name) {
        lastSelected.remove(name);
    }

    public boolean hasMail(String mail) {
        return lastSelected.containsKey(mail);
    }

    public boolean hasMail(Mail mail) {
        return lastSelected.containsKey(mail.getName());
    }

    public int getAvailableMail() {
        int amount = 0;
        for (Mail m : NPCMail.getSingleton().getMail().getAllActiveMail()) {
            if (MailTools.canCollect(m, lastSelected.get(m.getName()))) {
                amount++;
            }
        }
        return amount;
    }
}
