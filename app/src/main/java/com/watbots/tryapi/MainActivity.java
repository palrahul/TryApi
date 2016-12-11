package com.watbots.tryapi;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.watbots.tryapi.model.Item;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ItemFragment.OnListFragmentInteractionListener,
        SearchView.OnQueryTextListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent launchIntent = this.getIntent();
        handleIntent(launchIntent);

        ItemFragment listFragment = (ItemFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(listFragment == null) {
            listFragment = ItemFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, listFragment).commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (intent != null && Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("MainActivity", "query: " + query);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("MainActivity", "onQueryTextSubmit: " + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Log.d("MainActivity", "onQueryTextChange: " + newQuery);
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_listing) {
            ItemFragment listFragment = (ItemFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(listFragment == null) {
                listFragment = ItemFragment.newInstance(false);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, listFragment)
                        .commit();
            } else {
                listFragment = ItemFragment.newInstance(false);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, listFragment)
                        .commit();
                //transaction.addToBackStack(null); TODO check if we need to add to stack
            }
        } else if (id == R.id.nav_saved) {

            ItemFragment listFragment = (ItemFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(listFragment == null) {
                listFragment = ItemFragment.newInstance(true);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, listFragment)
                        .commit();
            } else {
                listFragment = ItemFragment.newInstance(true);
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, listFragment)
                    .commit();
                //transaction.addToBackStack(null); TODO check if we need to add to stack
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Item item) {
        addItem(item);
    }

    private void addItem(final Item item) {
        Realm.getDefaultInstance()
                .executeTransactionAsync(realm -> realm.copyToRealm(item),
                        () -> {
                            Log.d("MainActivity", "addItem Success");
                            Toast.makeText(this, item.name + ": Added to Saved", Toast.LENGTH_SHORT)
                                .show();
                        },
                    error -> {
                        // Transaction failed and was automatically canceled.
                        Log.e("MainActivity", "addItem Failed !");
                });
    }

    private void deleteItem(final Item item) {
        Realm.getDefaultInstance()
                .executeTransactionAsync(realm -> realm.where(Item.class).equalTo("id", item.id)
                .findAll()
                .deleteAllFromRealm(),
                () -> {
                    // Transaction was a success.
                    Log.d("MainActivity", "deleteItem Success");
                }, error -> {
                    // Transaction failed and was automatically canceled.
                    Log.e("MainActivity", "deleteItem Failed!");
                });
    }

}
