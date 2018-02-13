package de.dienstplan.pb.dienstplan2googlekalender;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;

/**
 * Created by Paule on 06.02.2018.
 */

public class LayerDayFormater implements DayFormatter {
    @NonNull
    @Override
    public String format(@NonNull CalendarDay day) {
        return String.format("%d\r\n%s", day.getDay(), "NewLine");
    }
}
