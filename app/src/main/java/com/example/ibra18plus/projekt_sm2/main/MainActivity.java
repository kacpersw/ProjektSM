package com.example.ibra18plus.projekt_sm2.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ibra18plus.projekt_sm2.R;
import com.example.ibra18plus.projekt_sm2.reservations.ListWithReservationRoomsFragment;
import com.example.ibra18plus.projekt_sm2.rooms.AddRoomFragment;
import com.example.ibra18plus.projekt_sm2.rooms.DeleteRoomFragment;
import com.example.ibra18plus.projekt_sm2.users.LoginActivity;
import com.example.ibra18plus.projekt_sm2.users.SharedPrefManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static DrawerLayout drawer;
    private static final String ID_USER = "id_user";
    private static int id_user;
    public static Integer user_id_better_one;
    private Boolean IS_ADMIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isLogged();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        if (!IS_ADMIN) {
            MenuItem menuItem = menu.findItem(R.id.add_room).setVisible(false);
        }


        setHeaders(navigationView);


    }

    public void setHeaders(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        TextView username = header.findViewById(R.id.header_nameTV);
        TextView email = header.findViewById(R.id.header_emailTV);
        username.setText(SharedPrefManager.getInstance(this).getUsername());
        email.setText(SharedPrefManager.getInstance(this).getUserEmail());
    }


    public void isLogged() {
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else if (SharedPrefManager.getInstance(this).getAdmin() != null) {
            IS_ADMIN = SharedPrefManager.getInstance(this).getAdmin();
        } else {
            IS_ADMIN = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;
        int id = item.getItemId();

        if (id == R.id.add_room) {
            fragmentClass = AddRoomFragment.class;
        } else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return false;
        } else if (id == R.id.list_rooms) {
            fragmentClass = DeleteRoomFragment.class;
        } else if (id == R.id.exit) {
            finish();
            return true;
        } else if (id == R.id.reservationList) {
            fragmentClass = ListWithReservationRoomsFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
            Bundle bundle = new Bundle();
            bundle.putInt(ID_USER, id_user);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.cotent_frame, fragment).
                    commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (
                Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.cotent_frame, fragment).
                commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
