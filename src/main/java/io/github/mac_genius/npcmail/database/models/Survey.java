package io.github.mac_genius.npcmail.database.models;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Mac on 3/28/2016.
 */
public class Survey extends Mail {
    private String surveyType;
    private String question;
    private ArrayList<String> answers;


    /**
     * Instantiates a new Mail.
     *
     * @param name        the name
     * @param type        the type
     * @param displayName the display name
     * @param doneMessage the done message
     * @param surveyType  the survey type
     * @param question    the question
     * @param answers     the answers
     * @param lore        the lore
     * @param expire      the expire
     * @param refresh     the refresh
     * @param slot        the slot
     * @param worth       the worth
     */
    public Survey(String name, String type, String displayName, String doneMessage, String surveyType, String question, ArrayList<String> answers, ArrayList<String> lore, long expire, long refresh, int slot, int worth) {
        super(name, type, displayName, doneMessage, lore, expire, refresh, slot, worth);
        this.surveyType = surveyType;
        this.question = question;
        this.answers = answers;
    }

    public Survey(String name, String type, String displayName, String doneMessage, String surveyType, String question, String answers,String lore, long expire, long refresh, int slot, int worth) {
        super(name, type, displayName, doneMessage, lore, expire, refresh, slot, worth);
        this.surveyType = surveyType;
        this.question = question;
        this.answers = getAnswerList(answers);
    }

    public Survey() {}

    /**
     * Gets survey type.
     *
     * @return the survey type
     */
    public String getSurveyType() {
        return surveyType;
    }

    /**
     * Sets survey type.
     *
     * @param surveyType the survey type
     */
    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    /**
     * Gets question.
     *
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets question.
     *
     * @param question the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Gets answers.
     *
     * @return the answers
     */
    public ArrayList<String> getAnswers() {
        return answers;
    }

    /**
     * Sets answers.
     *
     * @param answers the answers
     */
    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getAnswerString() {
        String s = "";
        for (String a : answers) {
            s += a + "|";
        }
        return s.substring(0, s.length() - 1);
    }

    public ArrayList<String> getAnswerList(String list) {
        Scanner in = new Scanner(list);
        in.useDelimiter("|");
        ArrayList<String> answers = new ArrayList<>();
        while (in.hasNext()) {
            answers.add(in.next());
        }
        return answers;
    }

    public void addAnswer(String answer) {
        answers.add(answer);
    }

    public void removeLastAnswer() {
        answers.remove(answers.size() - 1);
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
        if (mail instanceof Survey) {
            this.answers = ((Survey) mail).answers;
            this.question = ((Survey) mail).question;
            this.surveyType = ((Survey) mail).surveyType;
        }
    }
}
