package com.antilo0p.porkskin.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.antilo0p.porkskin.R;
import com.antilo0p.porkskin.models.DietMeal;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietMealAdapter extends RecyclerView.Adapter<DietMealAdapter.MealCardHolder> {
    private Context context;
    List<DietMeal> meals;

    private static final SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.ENGLISH);

    private static final SimpleDateFormat formatterTime = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm", Locale.ENGLISH);

    public DietMealAdapter(Context context, List<DietMeal> meals){
        this.context = context;
        this.meals = meals;
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }


    // TODO: Validate displayed data, NOT OK currently
    @Override
    public void onBindViewHolder(MealCardHolder mealCardHolder, int i) {
        DietMeal dm = meals.get(i);
        Resources res = context.getResources();
        String strDescription;
        String strTitle = String.format(res.getString(R.string.meal_week_name_title), dm.getName());
        strDescription = String.format(res.getString(R.string.meal_description), dm.getMealDaytime(), dm.getMeal() );

        mealCardHolder.vTitle.setText(strTitle);
        mealCardHolder.vDescription.setText(strDescription);
        if (dm.getDietWeek() != null) {
            mealCardHolder.vType.setText(dm.getDietWeek().getPhase());
        } else{
            mealCardHolder.vType.setText(dm.getDietWeek().getName());
        }

        switch (dm.getMeal()){
            case "Huevos con Queso":
                mealCardHolder.vThumb.setImageResource(R.drawable.eggs);
                break;
            case "Hamburguesa":
                mealCardHolder.vThumb.setImageResource(R.drawable.bgribeye);
                break;
            case "Pollo":
                mealCardHolder.vThumb.setImageResource(R.drawable.chiken);
                break;
            default:
                break;

        }
    }

    @Override
    public MealCardHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.mealcard, viewGroup, false);

        return new MealCardHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static  class MealCardHolder extends RecyclerView.ViewHolder {
        protected CardView cv;
        protected TextView vTitle;
        protected TextView vType;
        protected TextView vDescription;
        protected ImageView vThumb;
        protected String vTipo;
        protected String vWeek;

        public MealCardHolder(View v){
            super(v);
            cv = (CardView)v.findViewById(R.id.card_view);
            vTitle = (TextView) v.findViewById(R.id.ingredienteName);
            vDescription = (TextView) v.findViewById(R.id.mealContent);
            vType = (TextView) v.findViewById(R.id.mealOverImgTxt);
            vThumb = (ImageView) v.findViewById(R.id.thumbnail);
        }
    }


}
