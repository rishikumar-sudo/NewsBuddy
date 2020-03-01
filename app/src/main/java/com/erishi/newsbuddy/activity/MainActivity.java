package com.erishi.newsbuddy.activity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.erishi.newsbuddy.R;
import com.erishi.newsbuddy.adapter.HomeAdapter;
import com.erishi.newsbuddy.fragment.CategoryFragment;
import com.erishi.newsbuddy.fragment.FavoriteFragment;
import com.erishi.newsbuddy.fragment.HomeFragment;
import com.erishi.newsbuddy.models.Model;
import com.erishi.newsbuddy.utils.Constants;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;

    private  Bundle bundle;
    private Toolbar toolbarr;
    private FloatingActionButton fab;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    private int navItemIndex=0;
    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Model> list=new ArrayList<>();
    private HomeAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
       toolbarr=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbarr);
       drawer=findViewById(R.id.drawer_layout);
       ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbarr,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

       drawer.addDrawerListener(toggle);
       toggle.syncState();
       navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // load nav menu header data
        loadNavHeader();

       // initializing navigation menu
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0; getData();
            loadHomeFragment();
            swipeRefreshLayout.setRefreshing(true);

        }



    }


    private void loadNavHeader() {

        txtName.setText("Rishi");
        txtWebsite.setText("I Love Coding");
        imgNavHeaderBg.setImageResource(R.drawable.imgbg);
        imgProfile.setImageResource(R.drawable.ic_account_circle_black_24dp);
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

                // update the main content by replacing fragments
         Fragment fragment = getHomeFragment();
        bundle=new Bundle();
        bundle.putParcelableArrayList("key",list);
        fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commitAllowingStateLoss();


        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();



    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                CategoryFragment categoryFragment = new CategoryFragment();
                return categoryFragment;
            case 2:
                // movies fragment
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                return favoriteFragment;

            case 3:
                // settings fragment
                categoryFragment = new CategoryFragment();
                return categoryFragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        navItemIndex = 0;
                        Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_category:
                        navItemIndex = 1;
                        Toast.makeText(getApplicationContext(), "category", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_favorite:
                        navItemIndex = 2;
                        Toast.makeText(getApplicationContext(), "favorite", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_contact:
                        navItemIndex = 3;
                        Toast.makeText(getApplicationContext(), "contact", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_about_us:
                        navItemIndex = 4;
                        // launch new intent instead of loading fragment
                        Toast.makeText(getApplicationContext(), "about", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        navItemIndex = 5;
                        // launch new intent instead of loading fragment
                        Toast.makeText(getApplicationContext(), "privacy", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                        drawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                loadHomeFragment();

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (navItemIndex != 0) {
            navItemIndex = 0;

            loadHomeFragment();
            return;
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected

            getMenuInflater().inflate(R.menu.main, menu);




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.action_cateory:
                navItemIndex=1;
                Toast.makeText(getApplicationContext(), "Category!", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));

                Toast.makeText(getApplicationContext(), "Search user!", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_favorite:
                navItemIndex=2;
                Toast.makeText(getApplicationContext(), "Favourite user!", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), "Share user!", Toast.LENGTH_LONG).show();
                break;
            default:
                navItemIndex = 0;

        }

        loadHomeFragment();

        return super.onOptionsItemSelected(item);
    }
    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    private void loadData(String url) {

        //creating a string request to send request to the url
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());


                try {
                    list = getJsonArrayList(response.getJSONArray(Constants.ARTICLES));
                    Fragment fragment = new HomeFragment();
                    bundle=new Bundle();
                    bundle.putParcelableArrayList("key",list);
                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        getData();
                         swipeRefreshLayout.setRefreshing(false);

                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(jsonObjReq);
    }
    public void getData(){
        new Connection(getApplicationContext(), this).execute();



    }
    class Connection extends AsyncTask<Void, Void, Boolean> {

        private final Context context;
          MainActivity parent;

        Connection(Context context, MainActivity parent) {
            this.context = context;
            this.parent = parent;

        }

        boolean isInternet() {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager != null) {
                NetworkInfo[] info = manager.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }
            return false;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return isInternet();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            if (aBoolean)
            {
                loadData(Constants.HEAD_URL + "&" + Constants.APIKEY + "=" + "068c7c291c3e430fafc09992eaac6bd2");

            }

            else {
                // Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

                getData();
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
                adapter.onrefresh(list);

            }
        }

    }
    private static ArrayList<Model> getJsonArrayList(JSONArray jsonArray) {

        ArrayList<Model> list = new ArrayList<>();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject j = jsonArray.getJSONObject(i);
                    Model model = new Model();
                    JSONObject s = j.getJSONObject(Constants.SOURCE);
                    model.setAuthor(j.optString(Constants.AUTHOR));
                    model.setDesc(j.optString(Constants.DESCRIPTION));
                    model.setImage(j.optString(Constants.URLTOIMAGE));
                    model.setUrl(j.optString(Constants.URL));
                    model.setTitle(j.optString(Constants.TITLE));
                    model.setSname(s.optString(Constants.NAME));
                    model.setDate(j.optString(Constants.PUBLISHEDAT));
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return list;

    }
    public void onRefresh() {
         getData();

    }

}

