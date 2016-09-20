package es.antoniomb.dto;

import es.antoniomb.dto.enums.MigrationWeb;

import java.io.Serializable;

/**
 * Created by amiranda on 20/9/16.
 */
public class MigrationInput implements Serializable {

    private String from;
    private String fromUsername;
    private String fromPassword;
    private MigrationWeb source;
    private String to;
    private String toUsername;
    private String toPassword;
    private MigrationWeb target;

    public MigrationInput() {
    }

    public MigrationInput(String from, String fromUsername, String fromPassword, String to, String toUsername, String toPassword) {
        this.from = from;
        this.fromUsername = fromUsername;
        this.fromPassword = fromPassword;
        this.to = to;
        this.toUsername = toUsername;
        this.toPassword = toPassword;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getFromPassword() {
        return fromPassword;
    }

    public void setFromPassword(String fromPassword) {
        this.fromPassword = fromPassword;
    }

    public MigrationWeb getSource() {
        return source;
    }

    public void setSource(MigrationWeb source) {
        this.source = source;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getToPassword() {
        return toPassword;
    }

    public void setToPassword(String toPassword) {
        this.toPassword = toPassword;
    }

    public MigrationWeb getTarget() {
        return target;
    }

    public void setTarget(MigrationWeb target) {
        this.target = target;
    }
}
