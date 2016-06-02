package com.antilo0p.porkskin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.antilo0p.porkskin.models.DietMeal;
import com.antilo0p.porkskin.models.DietWeek;
import com.antilo0p.porkskin.util.DietMealAdapter;
import com.antilo0p.porkskin.util.DietMealDao;
import com.antilo0p.porkskin.util.DietWeekDao;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigre on 07/05/2016.
 */
// TODO: Migrate to Fragment
public class MealsListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DietMealAdapter dietMealAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Activity activity;
    ArrayList<DietMeal> dietMeals;
    List<DietWeek> dietWeeks;
    DietMealDao mealDAO;
    DietWeekDao weekDAO;

    TextView appDBstatus;
    private Context context;

    boolean dietStarted;
    int startWeek;
    String desayuno;
    String comida;
    String cena;

    private GetMealsTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = this;
        mealDAO = new DietMealDao(activity);
        weekDAO = new DietWeekDao(activity);

        Intent intent = getIntent();
        startWeek = intent.getIntExtra(DietSetupActivity.START_WEEK,1);
        desayuno = intent.getStringExtra(DietSetupActivity.TIME_DESAYUNO);
        comida= intent.getStringExtra(DietSetupActivity.TIME_COMIDA);
        cena = intent.getStringExtra(DietSetupActivity.TIME_CENA);

        dietStarted = intent.getBooleanExtra(DietSetupActivity.DIET_STARTED, false);

        Log.d("FromIntent", "Data:" + startWeek + "|" + desayuno + "|" + comida + "|" + cena);

        setContentView(R.layout.mealcardlist);
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        appDBstatus = (TextView) findViewById(R.id.appDBstatus);

        context = getApplicationContext();


        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        task = new GetMealsTask(activity);
        task.execute((Void) null);
        Log.d("DBStatus", "Weeks:" + weekDAO.count() + " Meals: " + mealDAO.count());

    }

    public class GetMealsTask extends AsyncTask<Void, Void, ArrayList<DietMeal>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetMealsTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        protected ArrayList<DietMeal> doInBackground(Void... arg0) {
            SharedPreferences sharedPref = context.getSharedPreferences("setup",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            boolean mealsLoaded = sharedPref.getBoolean(getString(R.string.diet_meals_loaded),false);
            if ( dietStarted && !mealsLoaded ) {
                Log.d("bgJOB","dietStarted: " + dietStarted);
                Log.d("bgJOB","Loading appropiate meals for dietStarted: " + dietStarted);
                Log.d("bgJOB","Loading Weeks: " + Integer.valueOf(startWeek).toString());
                   if (startWeek == 1 ) {
                        weekDAO.loadWeeks(4, desayuno, comida, cena);
                        editor.putBoolean(getString(R.string.diet_meals_loaded), true);
                        Log.d("bgJOB","Week: " + Integer.valueOf(startWeek).toString());
                    } else  if (startWeek == 2 ){
                        weekDAO.loadWeeks(3, desayuno, comida, cena);
                        editor.putBoolean(getString(R.string.diet_meals_loaded), true);
                        Log.d("bgJOB","Week: " + Integer.valueOf(startWeek).toString());
                    } else if ( startWeek == 3) {
                        weekDAO.loadWeeks(2, desayuno, comida, cena);
                        editor.putBoolean(getString(R.string.diet_meals_loaded), true);
                        Log.d("bgJOB","Week: " + Integer.valueOf(startWeek).toString());
                    } else if (startWeek == 4) {
                        weekDAO.loadWeeks(1, desayuno, comida, cena);
                        editor.putBoolean(getString(R.string.diet_meals_loaded), true);
                        Log.d("bgJOB","Week: " + Integer.valueOf(startWeek).toString());
                    } else {
                       Log.d("bgJOB","Why are we here?");
                   }
                editor.apply();
            }
            //ArrayList<DietMeal> mealList = mealDAO.getMealsForToday();
            ArrayList<DietMeal> mealList = mealDAO.getMealsAndWeek();
            try {
                mealDAO.backupDB();
            } catch (IOException e) {
                Log.d("backupDB","Error IO: " + e.getMessage());
            }

            return mealList;
        }



        @Override
        protected void onPostExecute(ArrayList<DietMeal> mList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                dietMeals  =  mList;
                if (mList != null) {
                    if (mList.size() != 0) {
                        dietMealAdapter = new DietMealAdapter(activity,
                                mList);
                        mRecyclerView.setAdapter(dietMealAdapter);
                        String status = String.format(getResources().getString(R.string.meal_appdb_status), weekDAO.count(), mealDAO.count());
                        appDBstatus.setText(status);


                    } else {
                        Toast.makeText(activity, R.string.no_meals_found,
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


}
