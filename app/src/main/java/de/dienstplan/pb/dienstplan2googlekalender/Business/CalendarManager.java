package de.dienstplan.pb.dienstplan2googlekalender.Business;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
                long eventStart = cursor.getLong(0);
                long eventEnd = cursor.getLong(1);
                String title = cursor.getString(2);
                Event event = new Event();
                Calendar calStart = GregorianCalendar.getInstance();
                calStart.setTimeInMillis(eventStart);
                Calendar calEnd = GregorianCalendar.getInstance();
                calEnd.setTimeInMillis(eventEnd);
                event.setStart(calStart);
                event.setEnd(calEnd);
                event.setTitle(title);
                result.add(event);
            }
        }
        catch (SecurityException exc) {
            Static.showAlert(activity, exc.getMessage());
        }

        return result;
    }

    public void deleteAllLayerEvents(LayerManager layerManager, CalendarItem calendarItem) {

        List<Layer> layerList = layerManager.getLayerList();
        if(layerList.size() == 0) {
            return;
        }
        ContentResolver cr = activity.getContentResolver();
        String where = String.format("%s=? AND (",
                CalendarContract.Events.CALENDAR_ID,
                CalendarContract.Events.CALENDAR_DISPLAY_NAME);
        String[] whereArgs = new String[layerList.size() + 1];
        whereArgs[0] = String.valueOf(calendarItem.getCalendarId());
        int n = 1;
        for(Layer layer : layerList) {
            where += String.format(" OR %s=?");
            whereArgs[n] = layer.getName();
        }
        where += ")";
        try {
            cr.delete(CalendarContract.Events.CONTENT_URI, where, whereArgs);
        }
        catch (SecurityException exc) {
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
