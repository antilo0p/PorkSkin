package com.antilo0p.porkskin.util;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by rigre on 07/05/2016.
 */
public class PorkskinDBDAO {

    protected SQLiteDatabase database;
    private PorkSkinDBHelper dbHelper;
    private Context mContext;

    public PorkskinDBDAO() {
    }

    public PorkskinDBDAO(Context context){
        this.mContext = context;
        dbHelper = PorkSkinDBHelper.getHelper(mContext);
        open();

    }

    public void open() throws SQLException {
        if(dbHelper == null)
            dbHelper = PorkSkinDBHelper.getHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    public  static void backupDB() throws IOException {
            String inFilename = "/data/data/com.antilo0p.porkskin/databases/PorkSkinDiet.db";
            File dbFile = new File(inFilename);
            FileInputStream fis = new FileInputStream(dbFile);

            String outFilename = Environment.getExternalStorageDirectory()+"/PorkSkinDiet.db";
            OutputStream output = new FileOutputStream(outFilename);

            byte[] buffer = new byte[1024];
            int lenght;
            while ((lenght = fis.read(buffer))>0){
                output.write(buffer,0, lenght);
            }
        output.flush();
        output.close();
        fis.close();

    }

}
