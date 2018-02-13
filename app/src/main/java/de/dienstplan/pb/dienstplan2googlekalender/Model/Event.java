package de.dienstplan.pb.dienstplan2googlekalender.Model;

import java.util.Calendar;

/**
 * Created by Paule on 07.02.2018.
 */

public class Event {
    private Calendar date;
    Layer layer;

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }


    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
