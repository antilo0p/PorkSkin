package com.antilo0p.porkskin;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.antilo0p.porkskin.models.DietMeal;
import com.antilo0p.porkskin.models.DietWeek;
import com.antilo0p.porkskin.util.DietMealAdapter;
import com.antilo0p.porkskin.util.DietMealDao;
import com.antilo0p.porkskin.util.DietWeekDao;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rigre on 16/05/2016.
 */
public class MealListFragment extends Fragment {

    boolean dietStarted;
    String dateShow;
    Date dateShowParsed;
    String type;
    private String title;

    Activity activity;
    private View view;
    private Context context;


    DietMealDao mealDAO;
    DietWeekDao weekDAO;
    ArrayList<DietMeal> dietMeals;

    TextView appDBstatus;

    private RecyclerView mRecyclerView;
    private DietMealAdapter dietMealAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GetMealsTask task;

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private static final SimpleDateFormat formatterTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.ENGLISH);

    public MealListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        Calendar cal = Calendar.getInstance();
        String defaultDate = formatterTime.format(cal.getTime());

        if (extras != null ) {
            dietStarted = extras.getBoolean("dietStarted", false);
            dateShow = extras.getString("date", defaultDate);
            type = extras.getString("type", "TODAY");
        } else {
            dietStarted = false;
            dateShow = defaultDate;
            type = "TODAY";
        }
        context = getContext();

        Log.d("FRAGMealListonCreate","dietStarted:" + dietStarted + " dateShow:" + dateShow + " type:" + type );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cardlist, container, false);
        activity = this.getActivity();
        mealDAO = new DietMealDao(activity);
        weekDAO = new DietWeekDao(activity);

        ArrayList<DietWeek> weeks = weekDAO.getWeeks();

        if (view != null) {
            appDBstatus = (TextView) view.findViewById(R.id.appDBstatus);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.cardList);

            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            task = new GetMealsTask(activity);
            task.execute(type);
            Log.d("FRAGDBStatus", "Weeks:" + weekDAO.count() + " Meals: " + mealDAO.count());
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(task!=null) task.cancel(true);
        task = null;

    }

    public class GetMealsTask extends AsyncTask<String, Void, ArrayList<DietMeal>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetMealsTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        protected ArrayList<DietMeal> doInBackground(String... args) {
            int count = args.length;
            String tipo = args[0];
            if (tipo != null) {
                Log.d("FRAGdoBG","received tipo:" + tipo);
            }
            ArrayList<DietMeal> mealList;
            SharedPreferences sharedPref = context.getSharedPreferences("setup", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            boolean mealsLoaded = sharedPref.getBoolean(getString(R.string.diet_meals_loaded),false);

            Log.d("FRAGdoBG", "mealsLoaded: " + mealsLoaded + " dietStarted: " + dietStarted);
            if (dietStarted && mealsLoaded && tipo != null ) {
                switch (tipo) {
                    case "TODAY":
                        mealList = mealDAO.getMealsForToday();
                        break;

                    case "TOMORROW":
                        mealList = mealDAO.getMealsForTomorrow();
                        break;

                    case "WEEK":
                        mealList = mealDAO.getMealsAndWeek();
                        break;

                    case "ALL":
                        mealList = mealDAO.getMealsAndWeek();
                        break;

                    default:
                        mealList = mealDAO.getMealsForToday();
                }

            } else {
                mealList = mealDAO.getMealsForToday();
            }
            // TODO: REMOVE THIS EVENTUALLY!!!
            try {
                mealDAO.backupDB();
            } catch (IOException e) {
                Log.d("FRAGdoBGBKDB","Error IO: " + e.getMessage());
            }

            Log.d("FRAGdoBG","mealList total:" + Integer.valueOf(mealList.size()).toString() );
            return mealList;
        }


        @Override
        protected void onPostExecute(ArrayList<DietMeal> mList) {
            if (isAdded()) {
                if (activityWeakRef.get() != null
                        && !activityWeakRef.get().isFinishing()) {
                    dietMeals  =  mList;
                    if (mList != null) if (mList.size() != 0) {
                        dietMealAdapter = new DietMealAdapter(getContext(),
                                mList);
                        mRecyclerView.setAdapter(dietMealAdapter);
                        String status = String.format(getResources().getString(R.string.meal_appdb_status), weekDAO.count(), mealDAO.count());
                        Log.d("FRAGDBStatusPost", "Weeks:" + weekDAO.count() + " Meals: " + mealDAO.count());

                        appDBstatus.setText(status);


                    } else {
                        View v = view.findViewById(R.id.overview_coordinator_layout);
                        if (v != null) {
                            Snackbar.make(v, "No se encontraron comidas", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        appDBstatus.setText("No meals found");

                        Log.d("FRAGDBStatusPost", "NO MEALS FOUND");

                    }
                }
            }

        }
    }


}