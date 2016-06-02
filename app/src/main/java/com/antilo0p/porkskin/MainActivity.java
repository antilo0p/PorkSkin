package com.antilo0p.porkskin;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.antilo0p.porkskin.util.TabsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rigre on 16/05/2016.
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private  FloatingActionButton fab;

    ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView mRecyclerView;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.alternate_main);
        setContentView(R.layout.porskin_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabsFragment()).commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
// Initializing Drawer Layout and ActionBarToggle

         actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                fab.show();

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                fab.hide();

                super.onDrawerOpened(drawerView);
            }
        };
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    //Replacing the main content with ContentFragment Which is our Inbox View;

                    case R.id.dieta:
                        Toast.makeText(getApplicationContext(),"Dieta Selected",Toast.LENGTH_SHORT).show();
                        // OneFragment fragment = new  OneFragment();
                        // MealsListActivity fragment = new MealsListActivity();
                        FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                        xfragmentTransaction.replace(R.id.containerView,new TabsFragment()).commit();
                        //  android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        //     fragmentTransaction.replace(R.id.content,fragment);
                        //.commit();
                        return true;
                    case R.id.status:
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.containerView,new OneFragment()).commit();
                        Toast.makeText(getApplicationContext(),"Status Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.recipes:
                        Toast.makeText(getApplicationContext(),"Recetas Selected",Toast.LENGTH_SHORT).show();
                        Intent mintent = new Intent(getApplicationContext(), MealsListActivity.class);
                        startActivity(mintent);
                        return true;
                    case R.id.drinks:
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.overview_coordinator_layout), "Drinks Selected", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return true;
                    case R.id.allowed:
                        Toast.makeText(getApplicationContext(),"Permitidas Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.disallowed:
                        Toast.makeText(getApplicationContext(),"NO Permitidas Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.health:
                        Toast.makeText(getApplicationContext(),"Salud Selected",Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.configure:
                        Toast.makeText(getApplicationContext(),"Configurar Selected",Toast.LENGTH_SHORT).show();
                        Intent sintent = new Intent(getApplicationContext(), DietSetupActivity.class);
                        startActivity(sintent);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Something is Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        //Setting the actionbarToggle to drawer layout

        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        fab = (FloatingActionButton) findViewById(R.id.overview_floating_action_button);
        mRecyclerView = (RecyclerView) findViewById(R.id.cardList);
        if (mRecyclerView != null) {
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Registra tu peso", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
        if (toolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch ( id ) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(),"Configurar Selected",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, DietSetupActivity.class);
                startActivity(intent);

                return true;
        }


        return super.onOptionsItemSelected(item);
    }


}