package com.example.myittaroostockinventorymanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.myittaroostockinventorymanger.fragments.AboutFragment;
import com.example.myittaroostockinventorymanger.fragments.DashboardFragment;
import com.example.myittaroostockinventorymanger.fragments.HomeFragment;
import com.example.myittaroostockinventorymanger.fragments.TransactionFragment;
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

        HomeFragment hf = new HomeFragment();
        createFragment(hf);

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(navView);
        });


        navDrawerOnItemSelected();
    }

    //menu item selected change fragment

    private void navDrawerOnItemSelected() {

        navView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            switch (id) {
                case R.id.nav_home:
                    HomeFragment hf = new HomeFragment();
                    createFragment(hf);
                    break;
                case R.id.nav_transaction:
                    TransactionFragment tf = new TransactionFragment();
                    createFragment(tf);
                    break;
                case R.id.nav_dashboard:
                    DashboardFragment df = new DashboardFragment();
                    createFragment(df);
                    break;
                case R.id.nav_about:
                    AboutFragment af = new AboutFragment();
                    createFragment(af);
                    break;
            }
            drawerLayout.closeDrawer(navView);
            return false;
        });
    }

    //fragment create in already defined view

    public void createFragment(Fragment fg) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_view, fg)
                .commit();
    }
}