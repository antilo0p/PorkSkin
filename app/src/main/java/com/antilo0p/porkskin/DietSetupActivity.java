package com.antilo0p.porkskin;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.antilo0p.porkskin.util.DietWeekDao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DietSetupActivity extends AppCompatActivity {
    private static EditText DesayunoTime;
    private static EditText ComidaTime;
    private static EditText CenaTime;
    private static TextView SemanaStartSelected;
    private static SeekBar SemanaSeekBar;
    private static CheckBox RecordatorioCheckBox;
    private static Button btnStartDiet;
    private static ImageButton btnImgDesayuno;
    private static ImageButton btnImgComida;
    private static ImageButton btnImgCena;

    private static Button btnResetDB;
    private static Button btnViewDiet;
    private static TextView strStatus;
    private static int TimePickerInput;
    private static final int PERMISSION_REQUEST_CODE = 1;

    private static String strDesayunoTime;
    private static String strComidaTime;
    private static String strCenaTime;
    private Context context;
    private Activity activity;

    DietWeekDao weekDAO;

    private SharedPreferences VsharedPref;

    public final static String START_WEEK = "com.antilo0p.porkskin.START_WEEK";
    public final static String SET_REMINDERS = "com.antilo0p.porkskin.SET_REMINDERS";
    public final static String TIME_DESAYUNO = "com.antilo0p.porkskin.TIME_DESAYUNO";
    public final static String TIME_COMIDA = "com.antilo0p.porkskin.TIME_COMIDA";
    public final static String TIME_CENA = "com.antilo0p.porkskin.TIME_CENA";
    public final static String DIET_STARTED= "com.antilo0p.porkskin.DIET_STARTED";

    public final static String DIET_LIST_MEALS= "com.antilo0p.porkskin.DIET_LIST_MEALS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_setup);

        SemanaSeekBar = (SeekBar) findViewById(R.id.sbWeek);
        DesayunoTime = (EditText) findViewById(R.id.editTimeDesayuno);
        ComidaTime = (EditText) findViewById(R.id.editTimeComida);
        CenaTime = (EditText) findViewById(R.id.editTimeCena);
        strStatus = (TextView) findViewById(R.id.strSatus);
        btnStartDiet = (Button) findViewById(R.id.buttonStartDiet);

        btnImgDesayuno = (ImageButton) findViewById(R.id.imgBtnDesayuno);
        btnImgComida = (ImageButton) findViewById(R.id.imgBtnComida);
        btnImgCena = (ImageButton) findViewById(R.id.imgBtnCena);

        RecordatorioCheckBox = (CheckBox) findViewById(R.id.checkReminder);

        btnResetDB = (Button) findViewById(R.id.resetDBbtn);
        btnViewDiet = (Button) findViewById(R.id.viewDietBtn);

       // btnStartDiet.getBackground().setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        context = getApplicationContext();
        activity = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        SemanaStartSelected = (TextView) findViewById(R.id.strSemana);
        SemanaSeekBar.requestFocus();
        VsharedPref = context.getSharedPreferences("setup",MODE_PRIVATE);

        SemanaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                if ( progressChanged == 0 ) {
                    SemanaStartSelected.setText("Una semana a Dieta ");
                } else if ( progressChanged == 1) {
                    SemanaStartSelected.setText("Una semana a Dieta ");
                } else if ( progressChanged == 2) {
                    SemanaStartSelected.setText("Dos semanas de Dieta ");
                } else if ( progressChanged == 3) {
                    SemanaStartSelected.setText("Tres semanas de Dieta");
                } else if ( progressChanged == 4) {
                    SemanaStartSelected.setText("Un Mes de Dieta!");
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                View v1 = getWindow().getDecorView().getRootView();
                Snackbar snackbar = Snackbar
                        .make(v1, "Desliza tu dedo para seleccionar las semanas que quieres estar a Dieta", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (progressChanged == 0 || progressChanged == 1 ) {
                    SemanaStartSelected.setText("Hare solo la primer semana de Dieta");
                } else if ( progressChanged == 2 ) {
                    SemanaStartSelected.setText("Hare dos semanas de Dieta!");
                } else if ( progressChanged == 3 ) {
                        SemanaStartSelected.setText("Hare tres semanas de Dieta!");
                } else if ( progressChanged == 4 ){
                    SemanaStartSelected.setText("Hare un mes de Dieta!");
                }
            }
        });

        btnImgDesayuno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerInput = v.getId();
                showTimePickerDialog(v,"breakfast");
        }
        });

        btnImgComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerInput = v.getId();
                showTimePickerDialog(v,"lunch");
            }
        });

        btnImgCena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerInput = v.getId();
                showTimePickerDialog(v,"diner");
            }
        });

        ValidatePreferences();

        btnStartDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartDiet(v);
            }
        });

        btnResetDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetDBDiet(v);
            }
        });

        btnViewDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDiet(v);
            }
        });

    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkReminder:
                if (checked) {
                    RecordatorioCheckBox.setText(R.string.diet_meals_meal_reminder);
                } else {
                    RecordatorioCheckBox.setText(R.string.diet_meals_no_meal_reminder);
                }
                break;
        }
    }

    // TODO: refactor to accepts VIew v as param1
    public void ValidatePreferences() {

        String settingDesayuno = VsharedPref.getString(getString(R.string.diet_setting_breakfast_time),"");
        String settingComida = VsharedPref.getString(getString(R.string.diet_setting_lunch_time),"");
        String settingCena = VsharedPref.getString(getString(R.string.diet_setting_diner_time),"");
        boolean dietStarted = VsharedPref.getBoolean(getString(R.string.diet_setting_dietStarted),false);
        int settingStart = VsharedPref.getInt(getString(R.string.diet_setting_startWeek),0);
        boolean settingReminder = VsharedPref.getBoolean(getString(R.string.diet_setting_reminders), false);

        if ( settingReminder ) {
            RecordatorioCheckBox.setChecked(true);
            RecordatorioCheckBox.setText(R.string.diet_meals_meal_reminder);
        } else {
            RecordatorioCheckBox.setChecked(false);
            RecordatorioCheckBox.setText(R.string.diet_meals_no_meal_reminder);
        }
        DesayunoTime.setText(settingDesayuno);
        CenaTime.setText(settingCena);
        ComidaTime.setText(settingComida);
// TODO: Fix egg chicken issue, pref, setup, list?
        if (dietStarted) {
            Log.d("DietStarted","Diet started detected");
            if ( settingStart > 0 ) {
                String strSettingStart = getString(R.string.diet_start_on_week, settingStart);

                strStatus.setText(strSettingStart);
                SemanaSeekBar.setProgress(settingStart);
                if ( checkPermission()) {

                    Log.d("ValidatePrefs", "DietStarted and SettingStart > 0");
                    btnStartDiet.setVisibility(View.INVISIBLE);
                } else {
                    requestPermission();
                }
            } else {
                strStatus.setText(R.string.diet_no_start);
                Log.d("DietStarted","settingStart is 0");
            }

        } else {
            View v1 = getWindow().getDecorView().getRootView();
                Snackbar snackbar = Snackbar
                        .make(v1, "Desliza tu dedo para seleccionar las semanas que quieres estar a Dieta", Snackbar.LENGTH_LONG);
                snackbar.show();
        }
    }

    public void StartDiet(View v){
        SharedPreferences.Editor editor = VsharedPref.edit();
        int startDietWeek;
        if ( SemanaSeekBar.getProgress() == 0 ) {
            startDietWeek = 1;
        } else {
            startDietWeek = Integer.valueOf(SemanaSeekBar.getProgress());
        }
        editor.putInt(getString(R.string.diet_setting_startWeek), startDietWeek);
        editor.putString(getString(R.string.diet_setting_breakfast_time), DesayunoTime.getText().toString());
        editor.putString(getString(R.string.diet_setting_lunch_time), ComidaTime.getText().toString());
        editor.putString(getString(R.string.diet_setting_diner_time), CenaTime.getText().toString());
        editor.putBoolean(getString(R.string.diet_setting_dietStarted), true);
        editor.putBoolean(getString(R.string.diet_meals_loaded), false);
        if ( RecordatorioCheckBox.isChecked() ) {
            editor.putBoolean(getString(R.string.diet_setting_reminders), true);
        } else {
            editor.putBoolean(getString(R.string.diet_setting_reminders), false);
        }

        editor.apply();


        if ( checkPermission()) {
            Snackbar.make(v,"Permiso concedido, Ahora podemos ayudarte con recordatorios.",Snackbar.LENGTH_LONG).show();
            addCalendarEvent(v);
            VsharedPref = context.getSharedPreferences("setup",MODE_PRIVATE);
            SharedPreferences.Editor ed = VsharedPref.edit();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(START_WEEK, startDietWeek);
            intent.putExtra(TIME_DESAYUNO, DesayunoTime.getText().toString());
            intent.putExtra(TIME_COMIDA, ComidaTime.getText().toString());
            intent.putExtra(TIME_CENA, CenaTime.getText().toString());
            intent.putExtra(DIET_STARTED, true);
            Log.d("startDiet","Starting MealList");

            if( ! DesayunoTime.getText().toString().trim().equalsIgnoreCase("")  &&
                    ! ComidaTime.getText().toString().trim().equalsIgnoreCase("")  &&
                   ! CenaTime.getText().toString().trim().equalsIgnoreCase("") )  {

                if (startDietWeek == 1 ) {
                    weekDAO.loadWeeks(4, DesayunoTime.getText().toString(), ComidaTime.getText().toString(),  CenaTime.getText().toString());
                    ed.putBoolean(getString(R.string.diet_meals_loaded), true);
                    ed.apply();
                    Log.d("bgJOB","Week: " + Integer.valueOf(startDietWeek).toString());
                } else  if (startDietWeek == 2 ){
                    weekDAO.loadWeeks(3, DesayunoTime.getText().toString(), ComidaTime.getText().toString(),  CenaTime.getText().toString());
                    ed.putBoolean(getString(R.string.diet_meals_loaded), true);
                    ed.apply();

                    Log.d("bgJOB","Week: " + Integer.valueOf(startDietWeek).toString());
                } else if ( startDietWeek == 3) {
                    weekDAO.loadWeeks(2, DesayunoTime.getText().toString(), ComidaTime.getText().toString(),  CenaTime.getText().toString());
                    ed.putBoolean(getString(R.string.diet_meals_loaded), true);
                    ed.apply();

                    Log.d("bgJOB","Week: " + Integer.valueOf(startDietWeek).toString());
                } else if (startDietWeek == 4) {
                    weekDAO.loadWeeks(1, DesayunoTime.getText().toString(), ComidaTime.getText().toString(),  CenaTime.getText().toString());
                    ed.putBoolean(getString(R.string.diet_meals_loaded), true);
                    ed.apply();

                    Log.d("bgJOB","Week: " + Integer.valueOf(startDietWeek).toString());
                } else {
                    Log.d("bgJOB","Why are we here?");
                }
                strStatus.setText(R.string.diet_saved);
                strStatus.setVisibility(View.VISIBLE);

                startActivity(intent);
            }


        } else {
            Snackbar.make(v,"Permisions de Calendario requeridos.",Snackbar.LENGTH_LONG).show();
            requestPermission();
        }

    }
    public void ViewDiet(View v){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(DIET_LIST_MEALS, "1");
            startActivity(intent);
    }
    public void ResetDBDiet(View v){
        weekDAO = new DietWeekDao(activity);
        weekDAO.resetDB();

        SharedPreferences.Editor editor = VsharedPref.edit();
        editor.putBoolean(getString(R.string.diet_setting_dietStarted), false);
        editor.putBoolean(getString(R.string.diet_meals_loaded), false);
        editor.apply();

        btnStartDiet.setVisibility(View.VISIBLE);

        Snackbar.make(v,"Database Reset Applied.",Snackbar.LENGTH_LONG).show();

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_CALENDAR)){
            Toast.makeText(context,"Calendar permission allows us to access events data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_CALENDAR},PERMISSION_REQUEST_CODE);
        }
    }

    public long getLongAsDate(int year, int month, int day, int hour, int minute) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month );
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTimeInMillis();
    }

    public void addCalendarEvent(View v) {

        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;


        Calendar time = Calendar.getInstance();
        long beginTime = getLongAsDate(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR) ,time.get(Calendar.MINUTE));
        int duration = 1000 * 60 * 20;
        startMillis = beginTime+duration;
                // 15 minutes
        endMillis = startMillis +duration;
        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "Porskin Test");
        values.put(CalendarContract.Events.DESCRIPTION, "Test Event from PorkSkin");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_TENTATIVE);
        values.put(CalendarContract.Events.EVENT_TIMEZONE,"US/Central");

        if (checkPermission()) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                long eventID = Long.parseLong(uri.getLastPathSegment());
                ContentValues rvalues = new ContentValues();
                rvalues.put(CalendarContract.Reminders.MINUTES, 15);
                rvalues.put(CalendarContract.Reminders.EVENT_ID, eventID);
                rvalues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                Uri ruri = cr.insert(CalendarContract.Reminders.CONTENT_URI, rvalues);
                if (ruri != null) {
                    long reminderID = Long.parseLong(uri.getLastPathSegment());
                }
            }

        }
    }

    public void showTimePickerDialog(View v, String picker) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putString("pick", picker);
        newFragment.setArguments(args);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        newFragment.show(getSupportFragmentManager(), picker );
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        String pick;
        String bt;
        String lt;
        String ct;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pick = getArguments().getString("pick");
            bt = DesayunoTime.getText().toString();
            lt =  ComidaTime.getText().toString();
            ct =  CenaTime.getText().toString();

        }

           @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
               int hour;
               int minute;
               final Calendar c = Calendar.getInstance();
               if (pick != null) {
                   //TODO: FIX this later
                   //getDialog().setTitle(pick);
                   switch (pick){
                       case "breakfast":
                           if (bt != null){
                               String[] sep = bt.split(":");
                               int h = Integer.valueOf(sep[0]);
                               int m = Integer.valueOf(sep[1]);
                               hour = h;
                               minute = m;
                           } else{
                               hour = c.get(Calendar.HOUR_OF_DAY);
                               minute = c.get(Calendar.MINUTE);
                           }
                           break;
                       case "lunch":
                           if (lt != null){
                               String[] sep = lt.split(":");
                               int h = Integer.valueOf(sep[0]);
                               int m = Integer.valueOf(sep[1]);
                               hour = h;
                               minute = m;
                           } else {
                               hour = c.get(Calendar.HOUR_OF_DAY);
                               minute = c.get(Calendar.MINUTE);
                           }
                           break;
                       case "diner":
                           if (ct != null){
                               String[] sep = ct.split(":");
                               int h = Integer.valueOf(sep[0]);
                               int m = Integer.valueOf(sep[1]);
                               hour = h;
                               minute = m;
                           }else {
                               hour = c.get(Calendar.HOUR_OF_DAY);
                               minute = c.get(Calendar.MINUTE);
                           }
                           break;
                       default:
                           hour = c.get(Calendar.HOUR_OF_DAY);
                           minute = c.get(Calendar.MINUTE);
                   }
               } else {
                   hour = c.get(Calendar.HOUR_OF_DAY);
                   minute = c.get(Calendar.MINUTE);
               }

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            switch ( TimePickerInput ) {
                case R.id.editTimeDesayuno:
                    DesayunoTime.setText(hourOfDay + ":" + minute);
                    strDesayunoTime = (hourOfDay + ":" + minute);
                    break;

                case R.id.editTimeComida:
                    ComidaTime.setText(hourOfDay + ":" + minute);
                    strComidaTime = (hourOfDay + ":" + minute);
                    break;

                case R.id.editTimeCena:
                    CenaTime.setText(hourOfDay + ":" + minute);
                    strCenaTime = (hourOfDay + ":" + minute);
                    break;

                case R.id.imgBtnDesayuno:
                    DesayunoTime.setText(hourOfDay + ":" + minute);
                    strDesayunoTime = (hourOfDay + ":" + minute);
                    break;

                case R.id.imgBtnComida:
                    ComidaTime.setText(hourOfDay + ":" + minute);
                    strComidaTime = (hourOfDay + ":" + minute);
                    break;

                case R.id.imgBtnCena:
                    CenaTime.setText(hourOfDay + ":" + minute);
                    strCenaTime = (hourOfDay + ":" + minute);
                    break;
                default:
                    break;
            }
        }
    }
}


