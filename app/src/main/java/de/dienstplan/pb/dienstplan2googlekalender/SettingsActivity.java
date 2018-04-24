package de.dienstplan.pb.dienstplan2googlekalender;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.dienstplan.pb.dienstplan2googlekalender.Business.CalendarManager;
import de.dienstplan.pb.dienstplan2googlekalender.Business.Settings;
import de.dienstplan.pb.dienstplan2googlekalender.Model.CalendarItem;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Settings settings;
    List<CalendarItem> calendarItems;
    ArrayAdapter<String> accountAdapter;
    ArrayAdapter<CalendarItem> calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            Toolbar toolbar = findViewById(R.id.toolbarSettings);
            setSupportActionBar(toolbar);
        }
        catch(Exception e) {
            Static.showAlert(this, e.getMessage());
        }
        settings = new Settings(this);

        CalendarItem settingsCalendarItem = settings.getCalendarItem();

        CalendarManager calendarManager = new CalendarManager(this);
        calendarItems = calendarManager.getAvailableCalendars();

        int selectAccountPos = 0;
        int selectCalendarPos = 0;

        HashSet<String> accounts = new HashSet<>();
        String selectedAccountName = null;
        for(CalendarItem iter : calendarItems) {
            String accountName = iter.getAccountName();
            accounts.add(accountName);
            if(selectedAccountName == null) {
                selectedAccountName = iter.getAccountName();
            }
            if(accountName.equals(settingsCalendarItem.getAccountName())) {
                selectedAccountName = accountName;
                selectAccountPos = accounts.size() - 1;
            }
        }

        Spinner accountSpinner = findViewById(R.id.spinnerAccountPicker);
        accountAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountAdapter.addAll(accounts);
        accountSpinner.setAdapter(accountAdapter);
        accountSpinner.setOnItemSelectedListener(this);
        accountSpinner.setSelection(selectAccountPos);
        refreshCalendarListByAccountName(selectedAccountName);

        for(int pos = 0; pos < calendarAdapter.getCount(); pos++) {
            CalendarItem item = calendarAdapter.getItem(pos);
            if(item.getCalendarId() == settingsCalendarItem.getCalendarId()) {
                final Spinner calendarSpinner = findViewById(R.id.spinnerCalendarPicker);
                final int selPos = pos;
                calendarSpinner.post(new Runnable() {
                    @Override
                    public void run() {
                        calendarSpinner.setSelection(selPos, true);
                    }
                });
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_cancel:
                finish();
                break;
            case R.id.action_settings_save:
                Spinner calendarSpinner = findViewById(R.id.spinnerCalendarPicker);
                CalendarItem calendarItem = (CalendarItem)calendarSpinner.getSelectedItem();
                settings.setCalendarItem(calendarItem);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String accountName = accountAdapter.getItem(position);
        refreshCalendarListByAccountName(accountName);
    }

    private void refreshCalendarListByAccountName(String accountName) {
        calendarAdapter = new ArrayAdapter<CalendarItem>(this, android.R.layout.simple_spinner_item);
        calendarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(CalendarItem calendarItem : calendarItems) {
            if(calendarItem.getAccountName().equals(accountName)) {
                calendarAdapter.add(calendarItem);
            }
        }

        Spinner calendarSpinner = findViewById(R.id.spinnerCalendarPicker);
        calendarSpinner.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
