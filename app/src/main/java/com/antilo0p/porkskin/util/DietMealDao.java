package com.antilo0p.porkskin.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.antilo0p.porkskin.models.DietMeal;
import com.antilo0p.porkskin.models.DietWeek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietMealDao extends PorkskinDBDAO {

    public static final String MEAL_ID_WITH_PREFIX = "m.id";
    public static final String MEAL_NAME_WITH_PREFIX = "m.meal_name";
    public static final String MEAL_WEEK_ID_WITH_PREFIX = "m.meal_week_id";
    public static final String WEEK_ID_WITH_PREFIX = "w.id";
    public static final String WEEK_NAME_WITH_PREFIX = "w.week_name";
    public static final String WEEK_FASE_WITH_PREFIX = "w.week_phase";


    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private static final SimpleDateFormat formatterTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.ENGLISH);

    private static final String WHERE_ID_EQUALS = PorkSkinDBHelper.KEY_ID
            + " =?";

    private String[] allColumns = { PorkSkinDBHelper.KEY_ID,
            PorkSkinDBHelper.KEY_MEAL_NAME, PorkSkinDBHelper.KEY_MEAL_MEAL,
            PorkSkinDBHelper.KEY_MEAL_TYPE, PorkSkinDBHelper.KEY_MEAL_DATE,
            PorkSkinDBHelper.KEY_MEAL_WEEK_ID};

    public DietMealDao(Context context){
        super(context);
    }

    public long save(DietMeal dietMeal) {
        ContentValues values = new ContentValues();
        values.put(PorkSkinDBHelper.KEY_MEAL_NAME, dietMeal.getName());
        values.put(PorkSkinDBHelper.KEY_MEAL_MEAL, dietMeal.getMeal());
        values.put(PorkSkinDBHelper.KEY_MEAL_TYPE, dietMeal.getMealType());
        values.put(PorkSkinDBHelper.KEY_MEAL_DATE, formatterTime.format(dietMeal.getMealDaytime()));
        values.put(PorkSkinDBHelper.KEY_MEAL_WEEK_ID, dietMeal.getDietWeek().getId());

        return database.insert(PorkSkinDBHelper.TABLE_MEALS, null, values);
    }

    public long update(DietMeal dietMeal) {
        ContentValues values = new ContentValues();
        values.put(PorkSkinDBHelper.KEY_MEAL_NAME, dietMeal.getName());
        values.put(PorkSkinDBHelper.KEY_MEAL_TYPE, dietMeal.getMealType());
        values.put(PorkSkinDBHelper.KEY_MEAL_MEAL, dietMeal.getMealType());
        long result = database.update(PorkSkinDBHelper.TABLE_MEALS, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(dietMeal.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int count(){
        Cursor cursor = database.rawQuery("select count(*) from " + PorkSkinDBHelper.TABLE_MEALS, null);
        cursor.moveToFirst();
        int res = cursor.getInt(0);
        cursor.close();
        return res;
    }


    public int deleteMeal(DietMeal dietMeal) {
        return database.delete(PorkSkinDBHelper.TABLE_MEALS,
                WHERE_ID_EQUALS, new String[] { dietMeal.getId() + "" });
    }


    public ArrayList<DietMeal> getMealsAndWeek() {
        ArrayList<DietMeal> meals  = new ArrayList<DietMeal>();

        String query = "SELECT " + MEAL_ID_WITH_PREFIX + ","
                + MEAL_NAME_WITH_PREFIX + ", "
                + PorkSkinDBHelper.KEY_MEAL_MEAL + ", " + PorkSkinDBHelper.KEY_MEAL_TYPE + ", "
                + PorkSkinDBHelper.KEY_MEAL_DATE + ", " + PorkSkinDBHelper.KEY_MEAL_WEEK_ID + ", "
                + WEEK_NAME_WITH_PREFIX + ", " + WEEK_FASE_WITH_PREFIX
                + " FROM " + PorkSkinDBHelper.TABLE_MEALS + " m, "
                + PorkSkinDBHelper.TABLE_WEEKS + " w WHERE m." + PorkSkinDBHelper.KEY_MEAL_WEEK_ID
                + " = w." + PorkSkinDBHelper.KEY_ID;


        Log.d("query", query);
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            DietMeal dietMeal = new DietMeal();
            dietMeal.setId(cursor.getInt(0));
            dietMeal.setName(cursor.getString(1));
            dietMeal.setMeal(cursor.getString(2));
            dietMeal.setMealType(cursor.getString(3));
            Log.d("getMealsAndWeek","dietMealDateDB:"  + cursor.getString(4));
            try {
                dietMeal.setMealDaytime(formatterTime.parse(cursor.getString(4)));
            } catch (ParseException e) {
                dietMeal.setMealDaytime(new Date());
            }
            DietWeek dietWeek = new DietWeek();
            dietWeek.setId(cursor.getInt(5));
            dietWeek.setName(cursor.getString(6));
            dietWeek.setPhase(cursor.getString(7));
            dietMeal.setDietWeek(dietWeek);

            meals.add(dietMeal);
        }
        cursor.close();
        return meals;
    }

    public ArrayList<DietMeal> getMeals() {
        ArrayList<DietMeal> meals  = new ArrayList<DietMeal>();

        Cursor cursor = database.query(PorkSkinDBHelper.TABLE_MEALS,
                allColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            DietMeal dietMeal = new DietMeal();
            dietMeal.setId(cursor.getInt(0));
            dietMeal.setName(cursor.getString(1));
            dietMeal.setMeal(cursor.getString(2));
            dietMeal.setMealType(cursor.getString(3));
            Log.d("dietMealIterator", "mealDaytime:" + cursor.getString(4));
            try {
                dietMeal.setMealDaytime(formatterTime.parse(cursor.getString(4)));
            } catch (ParseException e) {
                dietMeal.setMealDaytime(null);
            }
            DietWeek dietWeek = new DietWeek();
            dietWeek.setId(cursor.getInt(5));
            dietMeal.setDietWeek(dietWeek);
            Log.d("dietMealIterator",dietMeal.toString());

            meals.add(dietMeal);
        }
        cursor.close();
        return meals;
    }
    public List<DietMeal> getMealsByWeek(DietWeek dietWeek) {
        List<DietMeal> meals  = new ArrayList<DietMeal>();

        String query = "SELECT * FROM " + PorkSkinDBHelper.TABLE_MEALS
                    + "WHERE " + PorkSkinDBHelper.KEY_MEAL_WEEK_ID
                    + " = " + dietWeek.getId();

        Log.d("query", query);
        Cursor cursor = database.rawQuery(query,null);

        while (cursor.moveToNext()) {
            DietMeal dietMeal = new DietMeal();
            dietMeal.setId(cursor.getInt(0));
            dietMeal.setName(cursor.getString(1));
            dietMeal.setMeal(cursor.getString(2));
            dietMeal.setMealType(cursor.getString(3));
            try {
                dietMeal.setMealDaytime(formatterTime.parse(cursor.getString(4)));
            } catch (ParseException e) {
                dietMeal.setMealDaytime(null);
            }
            dietMeal.setDietWeek(dietWeek);
            Log.d("dietMealIterator",dietMeal.toString());

            meals.add(dietMeal);
        }
        cursor.close();
        return meals;
    }

    // TODO: add/validate, assign correct data-card values
    public ArrayList<DietMeal> getMealsForToday() {
        ArrayList<DietMeal> meals  = new ArrayList<DietMeal>();
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(Calendar.HOUR_OF_DAY,4);
        Date today=gc.getTime();
        String todayString = formatterTime.format(today);

        GregorianCalendar gct = new GregorianCalendar();
        gct.set(Calendar.HOUR_OF_DAY,23);
        Date tomorrow = gct.getTime();
        String tomorrowString = formatterTime.format(tomorrow);

        GregorianCalendar gcy = new GregorianCalendar();
        gcy.add(Calendar.DAY_OF_YEAR,-1);
        Date yesterday=gcy.getTime();
        String yesterdayString = formatterTime.format(yesterday);

        String query2 = "SELECT " + MEAL_ID_WITH_PREFIX + ", "
                + MEAL_NAME_WITH_PREFIX + ", "
                + PorkSkinDBHelper.KEY_MEAL_MEAL + ", " + PorkSkinDBHelper.KEY_MEAL_TYPE + ", "
                + PorkSkinDBHelper.KEY_MEAL_DATE + ", " + PorkSkinDBHelper.KEY_MEAL_WEEK_ID + ", "
                + WEEK_NAME_WITH_PREFIX + ", " + WEEK_FASE_WITH_PREFIX
                + " FROM " + PorkSkinDBHelper.TABLE_MEALS + " m, "
                + PorkSkinDBHelper.TABLE_WEEKS + " w WHERE ( m." + PorkSkinDBHelper.KEY_MEAL_WEEK_ID
                + " = w." + PorkSkinDBHelper.KEY_ID
                + " )  AND m." + PorkSkinDBHelper.KEY_MEAL_DATE
                + " >= Datetime('" + todayString  + "') and  m." + PorkSkinDBHelper.KEY_MEAL_DATE
                + " <= Datetime('" + tomorrowString + "')";

        String query = "SELECT * FROM " + PorkSkinDBHelper.TABLE_MEALS
                + " WHERE " + PorkSkinDBHelper.KEY_MEAL_DATE
                + " = '" + formatter.format(new Date()) + "'";

        Log.d("getMealsForToday", query2);
        Cursor cursor = database.rawQuery(query2,null);

        while (cursor.moveToNext()) {
            DietMeal dietMeal = new DietMeal();
            dietMeal.setId(cursor.getInt(0));
            dietMeal.setName(cursor.getString(1));
            dietMeal.setMeal(cursor.getString(2));
            dietMeal.setMealType(cursor.getString(3));
            Log.d("getMealsForToday","MealTime: "+cursor.getString(4));
            try {
                dietMeal.setMealDaytime(formatterTime.parse(cursor.getString(4)));
            } catch (ParseException e) {
                Date time = new Date();
                dietMeal.setMealDaytime(time);
            }
            DietWeek dietWeek = new DietWeek();
            dietWeek.setId(cursor.getInt(5));
            dietWeek.setName(cursor.getString(6));
            dietWeek.setPhase(cursor.getString(7));
            dietMeal.setDietWeek(dietWeek);
            Log.d("dietMealIterator",dietMeal.toString());
            meals.add(dietMeal);
        }
        cursor.close();
        return meals;
    }

    // TODO: add/validate, assign correct data-card values
    public ArrayList<DietMeal> getMealsForTomorrow() {
        ArrayList<DietMeal> meals  = new ArrayList<DietMeal>();
        GregorianCalendar gc = new GregorianCalendar();
        // Today is tomorrow at 4 AM
        gc.add(Calendar.DAY_OF_YEAR,1);
        gc.set(Calendar.HOUR_OF_DAY,4);
        Date today=gc.getTime();
        String todayString = formatterTime.format(today);

        // Tomorrow is tomorrow at 23 PM
        GregorianCalendar gct = new GregorianCalendar();
        gct.add(Calendar.DAY_OF_YEAR,1);
        gct.set(Calendar.HOUR_OF_DAY,23);
        Date tomorrow = gct.getTime();
        String tomorrowString = formatterTime.format(tomorrow);


        String query3 = "SELECT " + MEAL_ID_WITH_PREFIX + ", "
                + MEAL_NAME_WITH_PREFIX + ", "
                + PorkSkinDBHelper.KEY_MEAL_MEAL + ", " + PorkSkinDBHelper.KEY_MEAL_TYPE + ", "
                + PorkSkinDBHelper.KEY_MEAL_DATE + ", " + PorkSkinDBHelper.KEY_MEAL_WEEK_ID + ", "
                + WEEK_NAME_WITH_PREFIX + ", " + WEEK_FASE_WITH_PREFIX
                + " FROM " + PorkSkinDBHelper.TABLE_MEALS + " m, "
                + PorkSkinDBHelper.TABLE_WEEKS + " w WHERE ( m." + PorkSkinDBHelper.KEY_MEAL_WEEK_ID
                + " = w." + PorkSkinDBHelper.KEY_ID
                + " )  AND m." + PorkSkinDBHelper.KEY_MEAL_DATE
                + " >= Datetime('" + todayString + "') and  m." + PorkSkinDBHelper.KEY_MEAL_DATE
                + " <= Datetime('" + tomorrowString + "')";

        String query = "SELECT * FROM " + PorkSkinDBHelper.TABLE_MEALS
                + " WHERE " + PorkSkinDBHelper.KEY_MEAL_DATE
                + " = '" + formatter.format(new Date()) + "'";

        Log.d("getMealsForTomorrow", query3);
        Cursor cursor = database.rawQuery(query3,null);

        while (cursor.moveToNext()) {
            DietMeal dietMeal = new DietMeal();
            dietMeal.setId(cursor.getInt(0));
            dietMeal.setName(cursor.getString(1));
            dietMeal.setMeal(cursor.getString(2));
            dietMeal.setMealType(cursor.getString(3));
            Log.d("getMealsForTomorrow","MealTime: "+cursor.getString(4));
            try {
                dietMeal.setMealDaytime(formatterTime.parse(cursor.getString(4)));
            } catch (ParseException e) {
                Date time = new Date();
                dietMeal.setMealDaytime(time);
            }
            DietWeek dietWeek = new DietWeek();
            dietWeek.setId(cursor.getInt(5));
            dietWeek.setName(cursor.getString(6));
            dietWeek.setPhase(cursor.getString(7));
            dietMeal.setDietWeek(dietWeek);
            Log.d("dietMealIterator",dietMeal.toString());
            meals.add(dietMeal);
        }
        cursor.close();
        return meals;
    }
}
