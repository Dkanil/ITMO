package org.example.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@Named("clockBean")
@ApplicationScoped
public class ClockBean implements Serializable {
    private Date time = new Date();

    public Date getTime() {
        return time;
    }

    public void refreshTime() {
        this.time = new Date();
    }
}
