package de.dienstplan.pb.dienstplan2googlekalender.Business;

import android.app.Activity;
import android.content.SharedPreferences;

import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;

/**
 * Created by Paule on 07.02.2018.
 */

public class Settings {
    static String AccountNameKey = "AccountName";
    static String CalendarNameKey = "CalendarName";
    static String CalendarIdKey = "CalendarId";

    SharedPreferences preferences;
    public Settings(Activity activity) {
        preferences = activity.getSharedPreferences("Settings", 0);
    }

    public boolean setCalendarItem(CalendarItem item) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AccountNameKey, item.getAccountName());
        editor.putString(CalendarNameKey, item.getCalendarName());
        editor.putLong(CalendarIdKey, item.getCalendarId());
        return editor.commit();
    }

    public CalendarItem getCalendarItem() {
        SharedPreferences.Editor editor = preferences.edit();
        String accountName = preferences.getString(AccountNameKey, "");
        String calendarName = preferences.getString(CalendarNameKey, "");
        long id = preferences.getLong(CalendarIdKey, 0);
        CalendarItem item = new CalendarItem();
        item.setCalendarId(id);
        item.setCalendarName(calendarName);
        item.setAccountName(accountName);
        return item;
    }
}
