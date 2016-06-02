package com.antilo0p.porkskin.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rigre on 07/05/2016.
 */
public class PorkSkinDBHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "PorkSkinDBHelper";

    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "PorkSkinDiet.db";

    // Table Names
    public static final String TABLE_WEEKS = "weeks";
    public static final String TABLE_MEALS = "meals";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    // WEEKS Table  - column names
    public static final String KEY_WEEK_NAME = "week_name";
    public static final String KEY_WEEK_PHASE = "week_phase";
    public static final String KEY_WEEK_START = "week_start";

    // MEALS Table - column names
    public static final String KEY_MEAL_NAME = "meal_name";
    public static final String KEY_MEAL_MEAL = "meal_meal";
    public static final String KEY_MEAL_TYPE = "meal_type";
    public static final String KEY_MEAL_DATE = "meal_date";
    public static final String KEY_MEAL_WEEK_ID = "meal_week_id";

    // Table Create Statements
    // Week table create statement
    private static final String CREATE_TABLE_WEEK  = "CREATE TABLE "
            + TABLE_WEEKS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_WEEK_NAME
            + " TEXT, " + KEY_WEEK_PHASE + " TEXT, " + KEY_WEEK_START + " DATE, "
            + KEY_CREATED_AT + " DATE" + ")";


    // Meal table create statement
    private static final String CREATE_TABLE_MEAL = "CREATE TABLE " + TABLE_MEALS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MEAL_NAME + " TEXT, "
            + KEY_MEAL_MEAL + " TEXT, "  + KEY_MEAL_TYPE + " TEXT,"
            + KEY_MEAL_DATE + " DATE, " + KEY_MEAL_WEEK_ID + " INTEGER, "
            + " FOREIGN KEY ("+ KEY_MEAL_WEEK_ID + ") REFERENCES "
            + TABLE_WEEKS + "(id) " + ")";

    private static PorkSkinDBHelper instance;

    public static synchronized PorkSkinDBHelper getHelper(Context context) {
        if (instance == null)
            instance = new PorkSkinDBHelper(context);
        return instance;
    }

    public PorkSkinDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_WEEK);
        db.execSQL(CREATE_TABLE_MEAL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEEKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALS);

        // create new tables
        onCreate(db);
    }
}

