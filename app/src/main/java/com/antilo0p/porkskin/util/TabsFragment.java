package com.antilo0p.porkskin.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antilo0p.porkskin.MealListFragment;
import com.antilo0p.porkskin.OneFragment;
import com.antilo0p.porkskin.R;
import com.antilo0p.porkskin.ThirdFragment;
import com.antilo0p.porkskin.TwoFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rigre on 20/05/2016.
 */
public class TabsFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;


    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private static final SimpleDateFormat formatterTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.ENGLISH);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                if (ViewCompat.isLaidOut(tabLayout)) {
                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    tabLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            tabLayout.setupWithViewPager(viewPager);
                            tabLayout.removeOnLayoutChangeListener(this);
                        }
                    });
                }            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 :
                    Bundle params = new Bundle();
                    params.putString("type","TODAY");
                    params.putBoolean("dietStarted",true);
                    params.putString("date",formatterTime.format(new Date()));
                    MealListFragment mh = new MealListFragment();
                    mh.setArguments(params);
                    return mh;
                case 1 :
                    Bundle params1 = new Bundle();
                    params1.putString("type","TOMORROW");
                    params1.putBoolean("dietStarted",true);
                    params1.putString("date",formatterTime.format(new Date()));
                    MealListFragment mm = new MealListFragment();
                    mm.setArguments(params1);
                    return mm;
                case 2 :
                    Bundle params2 = new Bundle();
                    params2.putString("type","WEEK");
                    params2.putBoolean("dietStarted",true);
                    params2.putString("date",formatterTime.format(new Date()));
                    MealListFragment mw = new MealListFragment();
                    mw.setArguments(params2);
                    return mw;
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Hoy";
                case 1 :
                    return "Mañana";
                case 2 :
                    return "Semana";
            }
            return null;
        }
    }

}