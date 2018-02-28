package de.dienstplan.pb.dienstplan2googlekalender;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.dienstplan.pb.dienstplan2googlekalender.Business.LayerManager;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Event;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;

/**
 * Created by Paule on 06.02.2018.
 */

public class LayerDayFormater implements DayFormatter {
    HashMap<Integer, ArrayList<Event>> eventMap;
    public LayerDayFormater(ArrayList<Event> events, LayerManager layerManager) {
        eventMap = new HashMap<Integer, ArrayList<Event>>();
        HashMap<String, Layer> layerMap = layerManager.getLayerDictByName();
        if(layerMap.size() > 0) {
            for(Event event : events) {
                if(layerMap.containsKey(event.getTitle())) {
                    Integer day = event.getStart().get(Calendar.DAY_OF_MONTH);
                    ArrayList<Event> dayEvents = eventMap.get(day);
                    if(dayEvents == null) {
                        dayEvents = new ArrayList<Event>();
                        eventMap.put(day, dayEvents);
                    }
                    dayEvents.add(event);
                }
            }
        }

    }
    @NonNull
    @Override
    public String format(@NonNull CalendarDay day) {
        String result = DayFormatter.DEFAULT.format(day);
        ArrayList<Event> events = eventMap.get(day.getDay());
        if(events != null && events.size() > 0) {
            for(Event event : events) {
                if(result.length() > 0) {
                    result += "\n";
                }
                result += event.getTitle();
            }
        }
        return result;
    }
}
