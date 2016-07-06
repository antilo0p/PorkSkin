package com.antilo0p.porkskin.util;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.antilo0p.porkskin.PorkSkinApp;
import com.antilo0p.porkskin.R;
import com.antilo0p.porkskin.models.DietIngredient;

import java.util.List;

/**
 * Created by rigre on 07/05/2016.
 */
public class DietIngredientListAdapter extends RecyclerView.Adapter<DietIngredientListAdapter.IngredientListHolder> {
    private Context context;
    List<DietIngredient> ingredients;

    public DietIngredientListAdapter(Context context, List<DietIngredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    // TODO: Validate displayed data,  OK currently
    @Override
    public void onBindViewHolder(IngredientListHolder ingredientListHolder, int i) {
//        DietMeal dm = meals.get(i);
        final DietIngredient di = ingredients.get(i);
        Resources res = context.getResources();
        ingredientListHolder.vTitle.setText(di.getName());
        ingredientListHolder.vCarbs.setText(di.getCantidad());
        ingredientListHolder.vBtnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String query = "Recetas " + di.getName() + " bajas en carbohidratos";
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(SearchManager.QUERY, query);
                if (intent.resolveActivity(PorkSkinApp.getContext().getPackageManager()) != null) {
                    PorkSkinApp.getContext().startActivity(intent);
                }
            }
        });

    }

    @Override
    public IngredientListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.ingredient, viewGroup, false);

        return new IngredientListHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class IngredientListHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vCarbs;
        protected Button vBtnSearch;

        public IngredientListHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.titleIngredient);
            vBtnSearch = (Button) v.findViewById(R.id.ImgListImgBtnSearch);
            vCarbs = (TextView) v.findViewById(R.id.nmbrCarbs);
        }
    }


}
