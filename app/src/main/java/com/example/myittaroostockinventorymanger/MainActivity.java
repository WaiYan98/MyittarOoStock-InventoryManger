package com.example.myittaroostockinventorymanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.myittaroostockinventorymanger.fragments.AboutFragment;
import com.example.myittaroostockinventorymanger.fragments.DashboardFragment;
import com.example.myittaroostockinventorymanger.fragments.HomeFragment;
import com.example.myittaroostockinventorymanger.fragments.TransactionFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    @BindView(R.id.fab_menu)
    FloatingActionButton fabMenu;
    @BindView(R.id.fab_in)
    FloatingActionButton fabIN;
    @BindView(R.id.fab_out)
    FloatingActionButton fabOut;
    @BindView(R.id.fab_scan)
    FloatingActionButton fabScan;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //for expandable fab animation
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_animation);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_animation);

        HomeFragment hf = new HomeFragment();
        createFragment(hf);

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(navView);
        });


        navDrawerOnItemSelected();

        fabMenu.setOnClickListener(v -> {
            onFabMenuClicked();
        });
    }

    //menu item selected change fragment

    private void navDrawerOnItemSelected() {

        navView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            navView.setCheckedItem(item);

            switch (id) {
                case R.id.nav_home:
                    HomeFragment hf = new HomeFragment();
                    createFragment(hf);
                    fabMenu.show();
                    break;
                case R.id.nav_transaction:
                    TransactionFragment tf = new TransactionFragment();
                    createFragment(tf);
                    fabMenu.show();
                    break;
                case R.id.nav_dashboard:
                    DashboardFragment df = new DashboardFragment();
                    createFragment(df);
                    fabMenu.hide();
                    break;
                case R.id.nav_about:
                    AboutFragment af = new AboutFragment();
                    createFragment(af);
                    fabMenu.hide();
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

    private void onFabMenuClicked() {

        setVisibility();
        setAnimation();
        changeFabMenuIcon();
        isOpen = !isOpen;
    }

    //animation for floating action button
    private void setAnimation() {
        if (!isOpen) {
            fabIN.setAnimation(fromBottom);
            fabOut.setAnimation(fromBottom);
            fabScan.setAnimation(fromBottom);
            fabMenu.setAnimation(rotateOpen);
        } else {
            fabIN.setAnimation(toBottom);
            fabOut.setAnimation(toBottom);
            fabScan.setAnimation(toBottom);
            fabMenu.setAnimation(rotateClose);
        }
    }

    //visibility for multi floating action button
    private void setVisibility() {
        if (!isOpen) {
            fabIN.setVisibility(View.VISIBLE);
            fabOut.setVisibility(View.VISIBLE);
            fabScan.setVisibility(View.VISIBLE);
        } else {
            fabIN.setVisibility(View.INVISIBLE);
            fabOut.setVisibility(View.INVISIBLE);
            fabScan.setVisibility(View.INVISIBLE);
        }
    }

    private void changeFabMenuIcon() {
        if (!isOpen) {
            fabMenu.setImageResource(R.drawable.ic_add);
        } else {
            fabMenu.setImageResource(R.drawable.ic_menu);
        }
    }
}