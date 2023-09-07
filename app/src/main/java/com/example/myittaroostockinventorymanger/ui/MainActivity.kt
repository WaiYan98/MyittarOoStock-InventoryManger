package com.example.myittaroostockinventorymanger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.animation.Animation;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.ui.batch.AddNewAndUpdateBatchActivity;
import com.example.myittaroostockinventorymanger.ui.batch.BatchFragment;
import com.example.myittaroostockinventorymanger.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {



    //    @BindView(R.id.e_fab)
//    ExpandableFab eFab;
//    @BindView(R.id.fab_in)
//    FabOption fabIN;
//    @BindView(R.id.fab_out)
//    FabOption fabOut;
//    @BindView(R.id.fab_scan)
//    FabOption fabScan;
    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private boolean isOpen = false;
    private ActionMode actionMode;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        BatchFragment hf = new BatchFragment();
        createFragment(hf);

        binding.toolBar.setNavigationOnClickListener(v -> {
            binding.drawerLayout.openDrawer(binding.navView);
        });


//       navDrawerOnItemSelected();
//
//        eFab.setOnClickListener(v -> {
//            changeFabIcon();
//            isOpen = !isOpen;
//        });
//
//        fabIN.setOnClickListener(v -> {
//            changeFabIcon();
//            isOpen = false;
//            goToAddNewItemActivity();
//            Toast.makeText(this, "Item In", Toast.LENGTH_SHORT).show();
//        });
//
//        fabOut.setOnClickListener(v -> {
//            changeFabIcon();
//            isOpen = false;
//            Toast.makeText(this, "Item Out", Toast.LENGTH_SHORT).show();
//        });
//
//        fabScan.setOnClickListener(v -> {
//            changeFabIcon();
//            isOpen = false;
//            Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
//        });


    }

    // Drawer menu item selected change fragment

//    private void navDrawerOnItemSelected() {
//
//        binding.navView.setNavigationItemSelectedListener(item -> {
//
//            int id = item.getItemId();
////            fabHideAndShow(id);
//
//            switch (id) {
//                case R.id.nav_transaction:
//                    TransactionFragment tf = new TransactionFragment();
//                    createFragment(tf);
//                    break;
//                case R.id.stock_name:
//                    StockNameFragment sf = new StockNameFragment();
//                    createFragment(sf);
//                    break;
//                case R.id.batch:
//                    BatchFragment hf = new BatchFragment();
//                    createFragment(hf);
//                    break;
//                case R.id.nav_dashboard:
//                    DashboardFragment df = new DashboardFragment();
//                    createFragment(df);
//                    break;
//                case R.id.nav_about:
//                    AboutFragment af = new AboutFragment();
//                    createFragment(af);
//                    break;
//            }
//            drawerLayout.closeDrawer(navView);
//            return false;
//        });
//    }

    //fragment create in already defined view

    public void createFragment(Fragment fg) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_view, fg)
                .commit();
    }

    //for changing extendable fab background color and icon After clicked
//    private void changeFabIcon() {
//        if (!isOpen) {
//            eFab.setEfabIcon(ContextCompat.getDrawable(this, R.drawable.ic_add));
//            eFab.setEfabColor(ContextCompat.getColor(this, R.color.black));
//        } else {
//            eFab.setEfabIcon(ContextCompat.getDrawable(this, R.drawable.ic_white_menu));
//            eFab.setEfabColor(ContextCompat.getColor(this, R.color.color_blue));
//        }
//    }
//
//    //for fab hide and show in some fragments
//    private void fabHideAndShow(@IdRes int id) {
//        if (id == R.id.batch || id == R.id.nav_transaction) {
//            eFab.show();
//        } else {
//            eFab.hide();
//        }
//    }

    private void goToAddNewItemActivity() {
        Intent intent = new Intent(this, AddNewAndUpdateBatchActivity.class);
        startActivity(intent);
    }


//    private void fabOptionHide() {
//        fabOut.hide();
//        fabScan.hide();
//    }

}