package com.example.smartrefri.model;

import java.io.Serializable;

public class Mode implements Serializable {

    public String email;
    public int outing_mode;
    public int alarm_mode;

    public Mode(String email, int outing_mode) {
        this.email = email;
        this.outing_mode = outing_mode;
    }






    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getOuting_mode() {
        return outing_mode;
    }

    public void setOuting_mode(int outing_mode) {
        this.outing_mode = outing_mode;
    }


}
