package com.divesh.gymfl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;



import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;



public class fragment_gcalendar extends Fragment {
    public String[] mPermissions = {Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR};
    public static int MY_PERMISSIONS_REQUEST_CALENDAR =2;
    public static final String TAG ="fragment_gcalendar";
    private EditText email;

    private EditText recur;
    private Button submit;
    private int recurInt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gcalendar, container, false);
        email = view.findViewById(R.id.email);
        recur = view.findViewById(R.id.recurrence);
        submit = view.findViewById(R.id.add_event);




        //getting current shared preferences
        SharedPreferences shareRoutine = getActivity().getSharedPreferences("Routine", Context.MODE_PRIVATE);
        String Sunday=shareRoutine.getString("Sunday","Legs");
        String Monday=shareRoutine.getString("Monday", "Arms");
        String Tuesday=shareRoutine.getString("Tuesday","Rest");
        String Wednesday=shareRoutine.getString("Wednesday", "Back");
        String Thursday=shareRoutine.getString("Thursday", "Rest");
        String Friday=shareRoutine.getString("Friday","Arms");
        String Saturday=shareRoutine.getString("Saturday","Rest");
        final String [] scheduler = new String[]{Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday};
        final String[] dateSuffix = new String[] {"SU", "MO", "TU", "WE", "TH", "FR", "SA"};

        View.OnClickListener addEventListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String recurString = recur.getText().toString();
                    recurInt = Integer.parseInt(recurString);
                    String emailString = email.getText().toString();
                    boolean isemail = isEmailValid(emailString);
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        //ask for permission to write

                        Toast toast = Toast.makeText(getActivity(), "Permission to add to Calendar not allowed. Go to settings app to change", Toast.LENGTH_LONG);
                        toast.show();

                    } else {
                        if (isemail) {
                            for (int i = 0; i < scheduler.length; i++) {
                                addEvent(scheduler[i], recurInt, dateSuffix[i], emailString);
                            }

                        } else {
                            Toast toast = Toast.makeText(getActivity(), "Try a valid email address", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }catch (NumberFormatException  e) {

                    Toast toast = Toast.makeText(getActivity(), "Try using numbers", Toast.LENGTH_LONG);
                    toast.show();


                }
            }
        };


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            //ask for permission to write

            requestPermissions(mPermissions,
                    MY_PERMISSIONS_REQUEST_CALENDAR);

        }
        submit.setOnClickListener(addEventListener);
        submit.setVisibility(View.VISIBLE);






        return view;
    }

    private void addEvent(String title, int repeat, String day, String email){

        if(!"Rest".equals(title)) {


            //if do not have permissions get them and then add event

            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                //calendar id
                int calendarId = -1;
                String[] projection = new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.ACCOUNT_NAME};
                ContentResolver cr = getActivity().getContentResolver();
                Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                        CalendarContract.Calendars.ACCOUNT_NAME + "=? and (" +
                                CalendarContract.Calendars.NAME + "=? or " +
                                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                        new String[]{email, email,
                                email}, null);

                if (cursor.moveToFirst()) {

                    if (cursor.getString(1).equals(email))
                        calendarId = cursor.getInt(0); //youre calender id to be insered in above 2 answer


                }
                //getting start time
                long startMillis = 0;
                Calendar beginTime = Calendar.getInstance();
                switch(day){
                    case "SU":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        break;
                    case "MO":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        break;
                    case "TU":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        break;
                    case "WE":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        break;
                    case "TH":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        break;

                    case "FR":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        break;

                    case "SA":
                        beginTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        break;
                }

                startMillis = beginTime.getTimeInMillis();

                String recurrence = "FREQ=WEEKLY;BYDAY=" + day + ";COUNT=" + repeat;


                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.TITLE, title);
                values.put(CalendarContract.Events.DESCRIPTION, "workout session");
                values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
                values.put(CalendarContract.Events.ALL_DAY, false);
                values.put(CalendarContract.Events.RRULE, recurrence);
                values.put(CalendarContract.Events.DURATION, "P1H");
                TimeZone timeZone = TimeZone.getDefault();
                values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                long eventID = Long.parseLong(uri.getLastPathSegment());
//            QueryHandler.insertEvent(getActivity(), startMillis, endMillis, "Test");

                Toast toast = Toast.makeText(getActivity(), "Schedule added", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

    private void checkPermissions(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(getActivity(), p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(getActivity(), permissionsId, callbackId);
    }

    //check for valid input in email field
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
