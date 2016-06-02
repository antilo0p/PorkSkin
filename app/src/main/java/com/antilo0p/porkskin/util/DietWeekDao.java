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
import java.util.Locale;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietWeekDao extends PorkskinDBDAO {


    private static final String WHERE_ID_EQUALS = PorkSkinDBHelper.KEY_ID
            + " =?";

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private static final SimpleDateFormat formatterTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.ENGLISH);

    private String[] allColumns = { PorkSkinDBHelper.KEY_ID,
            PorkSkinDBHelper.KEY_WEEK_NAME, PorkSkinDBHelper.KEY_WEEK_PHASE,
            PorkSkinDBHelper.KEY_WEEK_START, PorkSkinDBHelper.KEY_CREATED_AT };

    public DietWeekDao(Context context){
        super(context);
    }

    public DietWeek getDietWeek(long week_id){
        String query = "SELECT * FROM " + PorkSkinDBHelper.TABLE_WEEKS + " WHERE "
                + PorkSkinDBHelper.KEY_ID + "=" + week_id;
        Log.d("DietWeekDao",query);

        Cursor cursor = database.rawQuery(query,null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        DietWeek dw = new DietWeek();
        dw.setId(cursor.getInt(cursor.getColumnIndex(PorkSkinDBHelper.KEY_ID)));
        dw.setName(cursor.getString(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_NAME)));
        dw.setPhase(cursor.getString(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_PHASE)));
        Date start = null;
        Log.d("DietWeekDao","WK: " + dw.getName() + " StartDay: " + cursor.getString(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_START)));
        //Log.d("DietWeekDao","WK: " + dw.getName() + " StartDayDate: " + new Date(cursor.getString(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_START))));
        //dw.setStartDay(new Date(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_START)));
        //formatter.parse(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_START));
        try {
            start = formatter.parse(cursor.getString(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_START)));
        } catch (ParseException e){
            Log.d("DietWeekDao", "Unable to parse StartDay" + cursor.getString(cursor.getColumnIndex(PorkSkinDBHelper.KEY_WEEK_START)));
        }
        if (start == null) {
            start = new Date();
            dw.setStartDay(start);
        } else {
            dw.setStartDay(start);
        }

        return dw;
    }

    public long save(DietWeek dietWeek){
        ContentValues values = new ContentValues();
        values.put(PorkSkinDBHelper.KEY_WEEK_NAME, dietWeek.getName());
        values.put(PorkSkinDBHelper.KEY_WEEK_PHASE, dietWeek.getPhase());
        values.put(PorkSkinDBHelper.KEY_WEEK_START, formatter.format(dietWeek.getStartDay()));
        values.put(PorkSkinDBHelper.KEY_CREATED_AT, formatter.format(dietWeek.getCreatedAt()));

        return database.insert(PorkSkinDBHelper.TABLE_WEEKS, null, values);
    }

    public long update(DietWeek dietWeek) {
        ContentValues values = new ContentValues();
        values.put(PorkSkinDBHelper.KEY_WEEK_START, formatter.format(dietWeek.getStartDay()));
        values.put(PorkSkinDBHelper.KEY_CREATED_AT, formatter.format(dietWeek.getCreatedAt()));

        long result = database.update(PorkSkinDBHelper.TABLE_WEEKS, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(dietWeek.getId()) });
        Log.d("Update Result:", "=" + result);
        return result;

    }

    public int count(){
        Cursor cursor = database.rawQuery("select count(*) from " + PorkSkinDBHelper.TABLE_WEEKS, null);
        cursor.moveToFirst();
        int res = cursor.getInt(0);
        cursor.close();
        return res;
    }

    public int deleteWeek(DietWeek dietWeek) {
        return database.delete(PorkSkinDBHelper.TABLE_WEEKS,
                WHERE_ID_EQUALS, new String[] { dietWeek.getId() + "" });
    }

    public void resetDB(){
       database.delete(PorkSkinDBHelper.TABLE_MEALS, null, null);
        database.delete(PorkSkinDBHelper.TABLE_WEEKS, null, null);
    }

    public ArrayList<DietWeek> getWeeks() {
        ArrayList<DietWeek> weeks = new ArrayList<DietWeek>();

        Cursor cursor = database.query(PorkSkinDBHelper.TABLE_WEEKS,
                        allColumns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            DietWeek dietWeek = new DietWeek();
            dietWeek.setId(cursor.getInt(0));
            dietWeek.setName(cursor.getString(1));
            dietWeek.setPhase(cursor.getString(2));
            Log.d("getWeeks","StartDay:" + cursor.getString(3));
            try {
                dietWeek.setStartDay(formatter.parse(cursor.getString(3)));
            } catch (ParseException e) {
                dietWeek.setStartDay(new Date());
            }
            if (cursor.getString(4) != null) {
                try {
                    dietWeek.setCreatedAt(formatter.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    dietWeek.setCreatedAt(new Date());
                }
            }
            Log.d("dietWeekIterator",dietWeek.toString());
            weeks.add(dietWeek);
        }
        cursor.close();
        return weeks;
    }

    public  void CreateMealsForWeek(DietWeek dietWeek, String breakfast, String lunch, String diner){

        String[] comidas= {"Desayuno", "Comida", "Cena"};
        //String[] comidasTipo= {"Desayuno", "Comida", "Cena"};
        String[] comidas_meals= {"Huevos con Queso", "Hamburguesa", "Pollo"};

        String[] tbreak = breakfast.split(":");
        Log.d("CreateMeals", "breakfast:" +tbreak[0] + ":" + tbreak[1]);
        String[] tlunch = lunch.split(":");
        Log.d("CreateMeals", "lunch:" +tlunch[0] + ":" + tlunch[1]);
        String[] tdiner = diner.split(":");
        Log.d("CreateMeals", "diner:" +tdiner[0] + ":" + tdiner[1]);
        Log.d("CreateMeals", "dietWeek StartDay: " + formatter.format(dietWeek.getStartDay()));
        // i = days, k = meals
        for (int i=1; i < 8 ; i++){
            Log.d("CreateMeals", "Creating meals for day: " + Integer.valueOf(i).toString());
            int k=0;
            for( String  comida : comidas ) {
                Calendar cal = Calendar.getInstance();
                if (i == 1 ){
                    cal.setTime(dietWeek.getStartDay());
                    if (k == 0){
                        cal.set(Calendar.HOUR,Integer.parseInt(tbreak[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(tbreak[1]));
                    } else if (k == 1) {
                        cal.set(Calendar.HOUR,Integer.parseInt(tlunch[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(tlunch[1]));
                    } else if (k == 2 ) {
                        cal.set(Calendar.HOUR,Integer.parseInt(tdiner[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(tdiner[1]));
                    }
                    Log.d("CreateMeals", "Creating meal: " + comida + " at: " + formatterTime.format(cal.getTime()));
                    DietMeal meal = new DietMeal(comida, comidas_meals[k], comida, cal.getTime(), dietWeek);
                    ContentValues values = new ContentValues();
                    values.put(PorkSkinDBHelper.KEY_MEAL_NAME, meal.getName());
                    values.put(PorkSkinDBHelper.KEY_MEAL_MEAL, meal.getMeal());
                    values.put(PorkSkinDBHelper.KEY_MEAL_TYPE, meal.getMealType());
                    values.put(PorkSkinDBHelper.KEY_MEAL_DATE, formatterTime.format(cal.getTime()));
                    values.put(PorkSkinDBHelper.KEY_MEAL_WEEK_ID, dietWeek.getId());

                    database.insert(PorkSkinDBHelper.TABLE_MEALS,null,values);
                }  else if ( i > 1 ) {
                    cal.setTime(dietWeek.getStartDay());
                    cal.add(Calendar.DAY_OF_YEAR,+i-1);
                    if (k == 0){
                        cal.set(Calendar.HOUR,Integer.parseInt(tbreak[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(tbreak[1]));
                    } else if (k == 1) {
                        cal.set(Calendar.HOUR,Integer.parseInt(tlunch[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(tlunch[1]));
                    } else if (k == 2 ) {
                        cal.set(Calendar.HOUR,Integer.parseInt(tdiner[0]));
                        cal.set(Calendar.MINUTE,Integer.parseInt(tdiner[1]));
                    }
                    Log.d("CreateMeals", "Creating meal: " + comida + " at: " + formatterTime.format(cal.getTime()));
                    DietMeal meal = new DietMeal(comida, comidas_meals[k], comida, cal.getTime(), dietWeek);
                    ContentValues values = new ContentValues();
                    values.put(PorkSkinDBHelper.KEY_MEAL_NAME, meal.getName());
                    values.put(PorkSkinDBHelper.KEY_MEAL_MEAL, meal.getMeal());
                    values.put(PorkSkinDBHelper.KEY_MEAL_TYPE, meal.getMealType());
                    values.put(PorkSkinDBHelper.KEY_MEAL_DATE, formatterTime.format(cal.getTime()));
                    values.put(PorkSkinDBHelper.KEY_MEAL_WEEK_ID, dietWeek.getId());

                    database.insert(PorkSkinDBHelper.TABLE_MEALS,null,values);
                }
                k+=1;
            }
        }
    }

    public  void loadWeeks(int numWeeks, String desayuno, String comida, String cena){
        Calendar calendar = Calendar.getInstance();
        Log.d("loadWeeks","received numWeeks: " + Integer.valueOf(numWeeks).toString());

        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, +7);
        Date nextWeek = calendar.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date());
        calendar2.add(Calendar.DAY_OF_YEAR, +14);
        Date afterNextWeek = calendar2.getTime();

        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(new Date());
        calendar3.add(Calendar.DAY_OF_YEAR, +21);
        Date afterNextNextWeek = calendar3.getTime();

// TODO: Fix this, set appropiate dates
        for (int i=1; i < numWeeks + 1; i++) {

            if (i == 1) {
                DietWeek dietWeek = new DietWeek("Semana " + Integer.valueOf(i).toString(), "Induccion", new Date());
                ContentValues values = new ContentValues();
                values.put(PorkSkinDBHelper.KEY_WEEK_NAME, dietWeek.getName());
                values.put(PorkSkinDBHelper.KEY_WEEK_PHASE, dietWeek.getPhase());
                values.put(PorkSkinDBHelper.KEY_WEEK_START, formatter.format(dietWeek.getStartDay()));
                long week_id = database.insert(PorkSkinDBHelper.TABLE_WEEKS, null, values);
                Log.d("loadWeeks","WeekID: " + Long.valueOf(week_id).toString());
                DietWeek DW = this.getDietWeek(week_id);
                Log.d("loadWeeks","DW: " + formatter.format(DW.getStartDay()));
                CreateMealsForWeek(DW, desayuno, comida, cena);

                Log.d("weekLoad","Loaded week1 ");
            } else if ( i == 2) {
                DietWeek dietWeek = new DietWeek("Semana " +  Integer.valueOf(i).toString(), "Induccion 2", nextWeek);
                ContentValues values = new ContentValues();
                values.put(PorkSkinDBHelper.KEY_WEEK_NAME, dietWeek.getName());
                values.put(PorkSkinDBHelper.KEY_WEEK_PHASE, dietWeek.getPhase());
                values.put(PorkSkinDBHelper.KEY_WEEK_START, formatter.format(nextWeek));
                long week_id = database.insert(PorkSkinDBHelper.TABLE_WEEKS, null, values);
                Log.d("loadWeeks","WeekID: " + Long.valueOf(week_id).toString());
                DietWeek DW = this.getDietWeek(week_id);
                Log.d("loadWeeks","DW: " + formatter.format(DW.getStartDay()));
                CreateMealsForWeek(DW, desayuno, comida, cena);

                Log.d("weekLoad","Loaded week2");
            } else if ( i == 3) {
                DietWeek dietWeek = new DietWeek("Semana " +  Integer.valueOf(i).toString(), "Bajar Peso", afterNextWeek);
                ContentValues values = new ContentValues();
                values.put(PorkSkinDBHelper.KEY_WEEK_NAME, dietWeek.getName());
                values.put(PorkSkinDBHelper.KEY_WEEK_PHASE, dietWeek.getPhase());
                values.put(PorkSkinDBHelper.KEY_WEEK_START, formatter.format(afterNextWeek));
                long week_id = database.insert(PorkSkinDBHelper.TABLE_WEEKS, null, values);
                Log.d("loadWeeks","WeekID: " + Long.valueOf(week_id).toString());
                DietWeek DW = this.getDietWeek(week_id);
                Log.d("loadWeeks","DW: " + formatter.format(DW.getStartDay()));
                CreateMealsForWeek(DW,  desayuno, comida, cena);

                Log.d("weekLoad","Loaded week3");
            } else if ( i == 4) {
                DietWeek dietWeek = new DietWeek("Semana " +  Integer.valueOf(i).toString(), "Mantenimiento", afterNextNextWeek);
                ContentValues values = new ContentValues();
                values.put(PorkSkinDBHelper.KEY_WEEK_NAME, dietWeek.getName());
                values.put(PorkSkinDBHelper.KEY_WEEK_PHASE, dietWeek.getPhase());
                values.put(PorkSkinDBHelper.KEY_WEEK_START, formatter.format(afterNextNextWeek));
                long week_id = database.insert(PorkSkinDBHelper.TABLE_WEEKS, null, values);
                Log.d("loadWeeks","WeekID: " + Long.valueOf(week_id).toString());
                DietWeek DW = this.getDietWeek(week_id);
                Log.d("loadWeeks","DW: " + formatter.format(DW.getStartDay()));
                CreateMealsForWeek(DW,  desayuno, comida, cena);

                Log.d("weekLoad","Loaded week4");
            } else {
                Log.d("weekLoad","Nothing else to do");
            }

        }

    }

}
