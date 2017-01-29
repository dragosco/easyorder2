package motacojo.mbds.fr.easyorder30.entities;

import java.util.Date;

/**
 * Created by thais on 29/01/2017.
 */

public class Buzz {
    private Person sender;
    private Date sentDate;
    private String message;

    public Buzz(Person sender) {
        this.sender = sender;
        this.sentDate = new Date();
        this.message = "";
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
