package io.github.mac_genius.npcmail.database.models;

import java.util.ArrayList;

/**
 * Created by Mac on 3/28/2016.
 */
public class Vote extends Mail {
    private String link;

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
    public Vote(String name, String type, String displayName, String doneMessage, String link, ArrayList<String> lore, long expire, long refresh, int slot, int worth) {
        super(name, type, displayName, doneMessage, lore, expire, refresh, slot, worth);
        this.link = link;
    }

    public Vote(String name, String type, String displayName, String doneMessage, String link, String lore, long expire, long refresh, int slot, int worth) {
        super(name, type, displayName, doneMessage, lore, expire, refresh, slot, worth);
        this.link = link;
    }

    public Vote() {}

    /**
     * Gets the link for the vote.
     *
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * Sets the link for the vote.
     *
     * @param link the link
     */
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public void updateMail(Mail mail) {
        this.setDisplayName(mail.getDisplayName());
        this.setDoneMessage(mail.getDoneMessage());
        this.setLore(mail.getLore());
        this.setExpire(mail.getExpire());
        this.setRefresh(mail.getRefresh());
        this.setSlot(mail.getSlot());
        this.setWorth(mail.getWorth());
        if (mail instanceof Vote) {
            this.link = ((Vote) mail).link;
        }
    }
}
