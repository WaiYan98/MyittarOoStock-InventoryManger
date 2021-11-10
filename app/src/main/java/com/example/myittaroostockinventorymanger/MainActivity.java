package com.example.myittaroostockinventorymanger;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.Toast;

import com.example.myittaroostockinventorymanger.fragments.AboutFragment;
import com.example.myittaroostockinventorymanger.fragments.BatchFragment;
import com.example.myittaroostockinventorymanger.fragments.DashboardFragment;
import com.example.myittaroostockinventorymanger.stock_name_fragment.StockNameFragment;
import com.example.myittaroostockinventorymanger.fragments.TransactionFragment;
import com.example.myittaroostockinventorymanger.local.Dao;
import com.example.myittaroostockinventorymanger.local.StockDataBase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.nambimobile.widgets.efab.ExpandableFab;
import com.nambimobile.widgets.efab.FabOption;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.tool_bar)
    MaterialToolbar toolbar;
    @BindView(R.id.e_fab)
    ExpandableFab eFab;
    @BindView(R.id.fab_in)
    FabOption fabIN;
    @BindView(R.id.fab_out)
    FabOption fabOut;
    @BindView(R.id.fab_scan)
    FabOption fabScan;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean isOpen = false;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        BatchFragment hf = new BatchFragment();
        createFragment(hf);

        toolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(navView);
        });

        navDrawerOnItemSelected();

        eFab.setOnClickListener(v -> {
            changeFabIcon();
            isOpen = !isOpen;
        });

        fabIN.setOnClickListener(v -> {
            changeFabIcon();
            isOpen = false;
            goToAddNewItemActivity();
            Toast.makeText(this, "Item In", Toast.LENGTH_SHORT).show();
        });

        fabOut.setOnClickListener(v -> {
            changeFabIcon();
            isOpen = false;
            Toast.makeText(this, "Item Out", Toast.LENGTH_SHORT).show();
        });

        fabScan.setOnClickListener(v -> {
            changeFabIcon();
            isOpen = false;
            Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
        });



    }

    // Drawer menu item selected change fragment

    private void navDrawerOnItemSelected() {

        navView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            fabHideAndShow(id);

            switch (id) {
                case R.id.nav_transaction:
                    TransactionFragment tf = new TransactionFragment();
                    createFragment(tf);
                    break;
                case R.id.stock_name:
                    StockNameFragment sf = new StockNameFragment();
                    createFragment(sf);
                    break;
                case R.id.batch:
                    BatchFragment hf = new BatchFragment();
                    createFragment(hf);
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
                .replace(R.id.fragment_view, fg)
                .commit();
    }

    //for changing extendable fab background color and icon After clicked
    private void changeFabIcon() {
        if (!isOpen) {
            eFab.setEfabIcon(ContextCompat.getDrawable(this, R.drawable.ic_add));
            eFab.setEfabColor(ContextCompat.getColor(this, R.color.black));
        } else {
            eFab.setEfabIcon(ContextCompat.getDrawable(this, R.drawable.ic_white_menu));
            eFab.setEfabColor(ContextCompat.getColor(this, R.color.color_blue));
        }
    }

    //for fab hide and show in some fragments
    private void fabHideAndShow(@IdRes int id) {
        if (id == R.id.batch || id == R.id.nav_transaction) {
            eFab.show();
        } else {
            eFab.hide();
        }
    }

    private void goToAddNewItemActivity() {
        Intent intent = new Intent(this, AddNewItemActivity.class);
        startActivity(intent);
    }


//    private void fabOptionHide() {
//        fabOut.hide();
//        fabScan.hide();
//    }

}