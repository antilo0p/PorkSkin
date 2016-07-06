package com.antilo0p.porkskin;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antilo0p.porkskin.R;
import com.antilo0p.porkskin.models.DietIngredient;
import com.antilo0p.porkskin.util.DietIngredientListAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by rigre on 16/05/2016.
 */
public class AllowedMantenimientoFragment extends Fragment {

    Activity activity;
    private View view;
    private Context context;

    private RecyclerView mRecyclerView;
    // private DietMealAdapter dietMealAdapter;
    private DietIngredientListAdapter dietIngListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GetIngredientsTask task;

    ArrayList<DietIngredient> dietIngredients;

    public AllowedMantenimientoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_allowed, container, false);
        activity = this.getActivity();
        if (view != null) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.ingredientList);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            task = new GetIngredientsTask(activity);
            task.execute();
            Log.d("AllowedFrag", "Done OnCreateView");
        }
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) task.cancel(true);
        task = null;

    }

    // TODO: Create XML Loader for Ingredients

    public class GetIngredientsTask extends AsyncTask<String, Void, ArrayList<DietIngredient>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetIngredientsTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        protected ArrayList<DietIngredient> doInBackground(String... args) {
            ArrayList<DietIngredient> ingredientList = new ArrayList<DietIngredient>();
            Resources res = getResources();
            String[] ings = res.getStringArray(R.array.allowed_mantenimiento);
            for (String di : ings) {
                String[] di_split = di.toString().split(",");
                DietIngredient ingredient = new DietIngredient();
                ingredient.setName(di_split[0]);
                ingredient.setCantidad(di_split[1]);
                ingredient.setFase("Bajar Peso");
                ingredientList.add(ingredient);
            }
            Log.d("AllowedFrag", "doInBackground.ings: " + ingredientList.toString());
            return ingredientList;
        }


        @Override
        protected void onPostExecute(ArrayList<DietIngredient> iList) {
            if (isAdded()) {
                if (activityWeakRef.get() != null
                        && !activityWeakRef.get().isFinishing()) {
                    dietIngredients = iList;
                    if (iList != null) if (iList.size() != 0) {
                        dietIngListAdapter = new DietIngredientListAdapter(getActivity(),
                                iList);
                        mRecyclerView.setAdapter(dietIngListAdapter);

                    } else {
                        View v = view.findViewById(R.id.overview_coordinator_layout);
                        if (v != null) {
                            Snackbar.make(v, "No se encontraron Ingredientes", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        Log.d("FRAGDBStatusPost", "NO INGREDIENTES FOUND");

                    }
                }
            }

        }
    }
}