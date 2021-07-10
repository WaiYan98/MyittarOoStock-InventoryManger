package com.example.myittaroostockinventorymanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.tool_bar)
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(navView);
        });

        navView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Log.d("tag", "onCreate: " + "home");
            } else if (id == R.id.nav_transaction) {
                Log.d("tag", "onCreate: " + "transaction");
            } else if (id == R.id.nav_dashboard) {
                Log.d("tag", "onCreate: " + "dashboard");
            } else if (id == R.id.nav_about) {
                Log.d("tag", "onCreate: " + "about");
            }

            return false;
        });

    }
}