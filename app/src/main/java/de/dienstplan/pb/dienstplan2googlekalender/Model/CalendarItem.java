package de.dienstplan.pb.dienstplan2googlekalender.Model;

/**
 * Created by Paule on 07.02.2018.
 */

public class CalendarItem {
    String accountName;
    String calendarName;
    long calendarId;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }
}
