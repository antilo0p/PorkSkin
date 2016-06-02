package com.antilo0p.porkskin.util;

import android.content.res.AssetManager;
import android.util.Log;

import com.antilo0p.porkskin.PorkSkinApp;
import com.antilo0p.porkskin.models.DietWeek;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by rigre on 31/05/2016.
 */
public class PorkSkinXmlParser {

    public static ArrayList<DietWeek> parseWeeks(){
        ArrayList<DietWeek> weeks = new ArrayList<>();
        AssetManager assetManager = PorkSkinApp.getContext().getAssets();

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            InputStream is = assetManager.open("porskin.xml");
            parser.setInput(is,null);
            int eventType = parser.getEventType();
            DietWeek week = null;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // at start of document: START_DOCUMENT
                    case XmlPullParser.START_DOCUMENT:
                        //study = new Study();
                        Log.d("xmlParser", "Starting xml parsing ");
                        break;

                    // at start of a tag: START_TAG
                    case XmlPullParser.START_TAG:
                        // get tag name
                        String tagName = parser.getName();
                        // if <semana>, get attribute: 'id'
                        if (tagName.equalsIgnoreCase("semana")) {
                            week = new DietWeek();
                            //study.mId = Integer.parseInt(parser.getAttributeValue(null, Study.ID));
                            week.setPhase(parser.getAttributeValue(null, "fase"));
                            week.setName("Semana "+ parser.getAttributeValue(null, "id"));
                        }
                        // if <dia>
                        else if (tagName.equalsIgnoreCase("dia")) {
                            Log.d("xmlParser","Dia:" + parser.getAttributeValue(null, "num"));
                        }
                        // if <desayuno>
                        else if (tagName.equalsIgnoreCase("desayuno")) {
                            if ( parser.getAttributeValue(null,"alt") != null ){
                                Log.d("xmlParser", "Opcion:" + parser.getAttributeValue(null,"opcion"));
                            }
                            Log.d("xmlParser","Desayuno: " + parser.getAttributeValue(null,"img") + " :" + parser.nextText());
                        }
                        // if <comida>
                        else if (tagName.equalsIgnoreCase("comida")) {
                            if ( parser.getAttributeValue(null,"alt") != null ){
                                Log.d("xmlParser", "Opcion:" + parser.getAttributeValue(null,"opcion"));
                            }
                            Log.d("xmlParser","Comida: " + parser.getAttributeValue(null,"img") + " :" + parser.nextText());
                        }
                        // if <cena>
                        else if (tagName.equalsIgnoreCase("cena")) {
                            if ( parser.getAttributeValue(null,"alt") != null ){
                                Log.d("xmlParser", "Opcion:" + parser.getAttributeValue(null,"opcion"));
                            }
                            Log.d("xmlParser","Cena: " + parser.getAttributeValue(null,"img") + " :" + parser.nextText());
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        String EtagName = parser.getName();
                        if( EtagName.equalsIgnoreCase("semana") && week != null){
                            Log.d("xmlParser","Semana creada:" + week.getPhase());
                            weeks.add(week);
                        }
                }
                // jump to next event
                eventType = parser.next();
            }
        }  catch (XmlPullParserException e) {
            weeks = null;
            Log.d("xmlParser","XML Parsing Error: "+ e.getMessage());
        } catch (IOException e) {
            weeks = null;
            Log.d("xmlParser","XML IO Error: "+ e.getMessage() );
        }

        return weeks;

    }
}
