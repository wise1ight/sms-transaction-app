package com.karlasa.smstransaction.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.karlasa.smstransaction.R;
import com.karlasa.smstransaction.fragment.HistoryFragment;
import com.karlasa.smstransaction.fragment.HomeFragment;
import com.karlasa.smstransaction.fragment.RulesFragment;
import com.karlasa.smstransaction.util.UniqueFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private long backKeyPressedTime = 0;
    private long backKeyDelayTime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPermissionValid();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavigationUserProfile();

        replaceFragment(HomeFragment.newInstance("",""));
    }

    private void isPermissionValid() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS};

            //권한이 없는 경우
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(MainActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.permission_msg), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + backKeyDelayTime) {
                backKeyPressedTime = System.currentTimeMillis();
                Toast.makeText(this,
                        getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id)
        {
            case R.id.nav_home:
                replaceFragment(HomeFragment.newInstance("",""));
                break;
            case R.id.nav_rules:
                replaceFragment(RulesFragment.newInstance());
                break;
            case R.id.nav_history:
                replaceFragment(HistoryFragment.newInstance());
                break;
            /*
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
                */
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.commit();
    }

    private void setNavigationUserProfile() {
        View header = navigationView.getHeaderView(0);
        CircleImageView imageProfile = (CircleImageView)header.findViewById(R.id.navProfileImageView);
        TextView labelUserName = (TextView)header.findViewById(R.id.navUsernameLabel);
        TextView labelEmail = (TextView)header.findViewById(R.id.navEmailLabel);
        imageProfile.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.railplanet));
        labelUserName.setText("레일플래닛"); //Hard Coding..
        labelEmail.setText("cs@railplanet.kr"); //Hard Coding..
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
