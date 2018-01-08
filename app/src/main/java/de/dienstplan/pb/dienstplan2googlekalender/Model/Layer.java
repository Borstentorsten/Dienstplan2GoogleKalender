package de.dienstplan.pb.dienstplan2googlekalender.Model;

import java.util.Calendar;

/**
 * Created by Paule on 23.11.2017.
 */

public class Layer {

    private String name;
    private Calendar start;
    private Calendar end;

    public void Layer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }
}
