package de.dienstplan.pb.dienstplan2googlekalender.Model;

import java.util.Calendar;

/**
 * Created by Paule on 07.02.2018.
 */

public class Event {
    private Calendar start;
    private Calendar end;
    private String title;
    private Layer layer;

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar date) {
        this.start = date;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
