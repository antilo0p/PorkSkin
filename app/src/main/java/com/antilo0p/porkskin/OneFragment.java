package com.antilo0p.porkskin;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.antilo0p.porkskin.models.DietWeek;
import com.antilo0p.porkskin.util.PorkSkinXmlParser;

import java.util.ArrayList;

/**
 * Created by rigre on 16/05/2016.
 */
public class OneFragment extends Fragment {
    private View view;
    private TextView status;
    ArrayList<DietWeek> weeks;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weeks = PorkSkinXmlParser.parseWeeks();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Resources res = getContext().getResources();
        view = inflater.inflate(R.layout.fragment_one, container, false);
        status = (TextView) view.findViewById(R.id.strStatus);

        String strStatus;
        if (weeks != null) {
            Log.d("OneFrag","weeks:" + Integer.valueOf(weeks.size()).toString());
            strStatus = String.format(res.getString(R.string.frag_strStatus), Integer.valueOf(weeks.size()).toString());
        } else {
            strStatus = String.format(res.getString(R.string.frag_strStatus), "0" );
        }

        if(strStatus != null) {
            status.setText(strStatus);
        }

        return view;
    }

}