package io.github.mac_genius.npcmail.database.models;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mac on 3/28/2016.
 */
public class Mail implements Comparable<Mail> {
    private String name;
    private String type;
    private String displayName;
    private String doneMessage;
    private ArrayList<String> lore;
    private long expire;
    private long refresh;
    private int slot;
    private int worth;

    /**
     * Instantiates a new Mail.
     *
     * @param name        the name
     * @param type        the type
     * @param displayName the display name
     * @param doneMessage the done message
     * @param lore        the lore
     * @param expire      the expire
     * @param refresh     the refresh
     * @param slot        the slot
     * @param worth       the worth
     */
    public Mail(String name, String type, String displayName, String doneMessage, ArrayList<String> lore, long expire, long refresh, int slot, int worth) {
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.doneMessage = doneMessage;
        this.lore = lore;
        this.expire = expire;
        this.refresh = refresh;
        this.slot = slot;
        this.worth = worth;
    }

    public Mail(String name, String type, String displayName, String doneMessage, String lore, long expire, long refresh, int slot, int worth) {
        this.name = name;
        this.type = type;
        this.displayName = displayName;
        this.doneMessage = doneMessage;
        this.lore = getLoreList(lore);
        this.expire = expire;
        this.refresh = refresh;
        this.slot = slot;
        this.worth = worth;
    }

    public Mail() {}

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets display name.
     *
     * @param displayName the display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets done message.
     *
     * @return the done message
     */
    public String getDoneMessage() {
        return doneMessage;
    }

    /**
     * Sets done message.
     *
     * @param doneMessage the done message
     */
    public void setDoneMessage(String doneMessage) {
        this.doneMessage = doneMessage;
    }

    /**
     * Gets lore.
     *
     * @return the lore
     */
    public ArrayList<String> getLore() {
        return lore;
    }

    public String getLoreString() {
        String s = "";
        for (String a : lore) {
            s += a + "|";
        }
        return s.substring(0, s.length() - 1);
    }

    public ArrayList<String> getLoreList(String input) {
        Scanner in = new Scanner(input);
        in.useDelimiter("\\|");
        ArrayList<String> lore = new ArrayList<>();
        while (in.hasNext()) {
            String next = in.next();
            lore.add(next);
        }
        return lore;
    }

    /**
     * Sets lore.
     *
     * @param lore the lore
     */
    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public void addLore(String s) {
        lore.add(s);
    }

    public void removeLast() {
        lore.remove(lore.size() - 1);
    }

    /**
     * Gets expire.
     *
     * @return the expire
     */
    public long getExpire() {
        return expire;
    }

    /**
     * Sets expire.
     *
     * @param expire the expire
     */
    public void setExpire(long expire) {
        this.expire = expire;
    }

    /**
     * Gets refresh.
     *
     * @return the refresh
     */
    public long getRefresh() {
        return refresh;
    }

    /**
     * Sets refresh.
     *
     * @param refresh the refresh
     */
    public void setRefresh(long refresh) {
        this.refresh = refresh;
    }

    /**
     * Gets slot.
     *
     * @return the slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Sets slot.
     *
     * @param slot the slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    /**
     * Gets worth.
     *
     * @return the worth
     */
    public int getWorth() {
        return worth;
    }

    /**
     * Sets worth.
     *
     * @param worth the worth
     */
    public void setWorth(int worth) {
        this.worth = worth;
    }


    @Override
    public int compareTo(Mail o) {
        return this.name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object mail) {
        if (mail instanceof Mail) {
            if (name.equalsIgnoreCase(((Mail) mail).getName())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void updateMail(Mail mail) {
        this.displayName = mail.displayName;
        this.doneMessage = mail.doneMessage;
        this.lore = mail.lore;
        this.expire = mail.expire;
        this.refresh = mail.refresh;
        this.slot = mail.slot;
        this.worth = mail.worth;
    }
}
