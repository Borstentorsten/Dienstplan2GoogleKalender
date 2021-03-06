package de.dienstplan.pb.dienstplan2googlekalender.Business;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Event;
import de.dienstplan.pb.dienstplan2googlekalender.Model.Layer;
import de.dienstplan.pb.dienstplan2googlekalender.Static;

/**
 * Created by Paule on 06.02.2018.
 */

public class CalendarManager {
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] CALENDAR_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int CALENDAR_PROJECTION_ID_INDEX = 0;
    private static final int CALENDAR_PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int CALENDAR_PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int CALENDAR_PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    Activity activity;

    public CalendarManager(Activity activity) {
        this.activity = activity;
    }

    public List<CalendarItem> getAvailableCalendars() {
        ArrayList<CalendarItem> retList = new ArrayList<>();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        ContentResolver cr = activity.getContentResolver();
        String query = "";
        String[] selectionArgs = null;
        try {
            Cursor cursor = cr.query(uri, CALENDAR_PROJECTION, query, selectionArgs, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(CALENDAR_PROJECTION_DISPLAY_NAME_INDEX);
                String accountName = cursor.getString(CALENDAR_PROJECTION_ACCOUNT_NAME_INDEX);
                long id = cursor.getLong(CALENDAR_PROJECTION_ID_INDEX);
                CalendarItem item = new CalendarItem();
                item.setAccountName(accountName);
                item.setCalendarName(name);
                item.setCalendarId(id);
                retList.add(item);
                int x = 0;
            }

        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }

        return retList;
    }

    public ArrayList<Event> getEvents(CalendarItem calendar, Calendar start, Calendar end) {
        ArrayList<Event> result = new ArrayList<>();
        ContentResolver cr = activity.getContentResolver();
        try {
            Uri uri = CalendarContract.Events.CONTENT_URI;
            String[] projection = new String[]
                    {CalendarContract.Events.DTSTART,
                     CalendarContract.Events.DTEND, CalendarContract.Events.TITLE};
            String selection = String.format("%s=? AND %s>=? AND %s<=?",
                    CalendarContract.Events.CALENDAR_ID,
                    CalendarContract.Events.DTSTART,
                    CalendarContract.Events.DTEND);
            String[] selArgs = new String[] {
                    String.valueOf(calendar.getCalendarId()),
                    String.valueOf(start.getTimeInMillis()),
                    String.valueOf(end.getTimeInMillis())};
            Cursor cursor = cr.query(uri, projection, selection, selArgs, null);
            while(cursor.moveToNext()) {
                long eventStart =  cursor.getLong(0);
                long eventEnd = cursor.getLong(1);
                String title = cursor.getString(2);
                Event event = new Event();
                Date startDate = new Date(eventStart);
                Date endDate = new Date(eventEnd);
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                event.setStart(cal);
                cal.setTime(endDate);
                event.setEnd(cal);
                event.setTitle(title);
                result.add(event);
            }
        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }

        return result;
    }

    public Event setLayerToCalendar(Layer layer, CalendarDay date, List<Layer> layerList,
                                   CalendarItem calendarItem) {
        Event event = null;
        deleteAllLayerEvents(layerList, calendarItem, date);
        if(layer == null) {

        }
        else {
            event = new Event();
            Calendar start = GregorianCalendar.getInstance();
            Calendar end = GregorianCalendar.getInstance();
            start.setTime(date.getDate());
            start.set(Calendar.HOUR_OF_DAY, layer.getStart().get(Calendar.HOUR_OF_DAY));
            start.set(Calendar.MINUTE, layer.getStart().get(Calendar.MINUTE));
            end.setTime(date.getDate());
            end.set(Calendar.HOUR_OF_DAY, layer.getEnd().get(Calendar.HOUR_OF_DAY));
            end.set(Calendar.MINUTE, layer.getEnd().get(Calendar.MINUTE));
            event.setStart(start);
            event.setEnd(end);
            event.setTitle(layer.getName());
            setEvent(event, calendarItem);
        }
        return event;
    }
    public void deleteAllLayerEvents(List<Layer> layerList, CalendarItem calendarItem, CalendarDay day) {
        ContentResolver cr = activity.getContentResolver();
        String where = String.format("%s=? AND %s>=? AND %s<=? AND (",
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTSTART);
        ArrayList<String> whereArgs = new ArrayList<>();
        whereArgs.add(String.valueOf(calendarItem.getCalendarId()));
        Calendar dtStart = (Calendar)day.getCalendar().clone();
        dtStart.set(Calendar.HOUR_OF_DAY, 0);
        dtStart.set(Calendar.MINUTE, 0);
        Calendar dtEnd = (Calendar)day.getCalendar().clone();
        dtEnd.set(Calendar.HOUR_OF_DAY, 23);
        dtEnd.set(Calendar.MINUTE, 59);
        whereArgs.add(String.valueOf(dtStart.getTimeInMillis()));
        whereArgs.add(String.valueOf(dtEnd.getTimeInMillis()));

        int n = 1;
        for(Layer layer : layerList) {
            if(n > 1)
                where += " OR ";
            where += String.format("%s = ?", CalendarContract.Events.TITLE);
            whereArgs.add(layer.getName());
            n++;
        }
        where += ")";
        try {
            int deleted = cr.delete(CalendarContract.Events.CONTENT_URI, where, whereArgs.toArray(new String[0]));
            int x = 0;
        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }
        catch(Exception exc) {
            Static.showAlert(activity, exc.getMessage());
        }
    }

    public void setEvent(Event event, CalendarItem calendarItem) {
        ContentResolver cr = activity.getContentResolver();
        ContentValues contentValues = new ContentValues();

        Calendar start = event.getStart();
        Calendar end = event.getEnd();

        contentValues.put(CalendarContract.Events.CALENDAR_ID, calendarItem.getCalendarId());
        contentValues.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        contentValues.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
        contentValues.put(CalendarContract.Events.TITLE, event.getTitle());
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        try {
            cr.insert(CalendarContract.Events.CONTENT_URI, contentValues);
        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }
    }
}
