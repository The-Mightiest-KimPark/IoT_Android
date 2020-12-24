package com.example.smartrefri.model;

import java.io.Serializable;

public class AlarmMode implements Serializable {
    String email;
    int alarm_mode;

    public AlarmMode(String email, int alarm_mode) {
        this.email = email;
        this.alarm_mode = alarm_mode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAlarm_mode() {
        return alarm_mode;
    }

    public void setAlarm_mode(int alarm_mode) {
        this.alarm_mode = alarm_mode;
    }
}
