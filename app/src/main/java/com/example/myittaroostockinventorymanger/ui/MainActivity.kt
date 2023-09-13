package com.example.myittaroostockinventorymanger.ui

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.animation.Animation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.repository.Repository
import com.example.myittaroostockinventorymanger.databinding.ActivityMainBinding
import com.example.myittaroostockinventorymanger.ui.item_name.ItemNameViewModel

class MainActivity : AppCompatActivity() {
    //    @BindView(R.id.e_fab)
    //    ExpandableFab eFab;
    //    @BindView(R.id.fab_in)
    //    FabOption fabIN;
    //    @BindView(R.id.fab_out)
    //    FabOption fabOut;
    //    @BindView(R.id.fab_scan)
    //    FabOption fabScan;
    private val rotateOpen: Animation? = null
    private val rotateClose: Animation? = null
    private val fromBottom: Animation? = null
    private val toBottom: Animation? = null
    private val isOpen = false
    private val actionMode: ActionMode? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration

    val viewModel: ItemNameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        setSupportActionBar(binding.toolBarMain)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.findNavController()

        binding.navView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.toolBarMain.setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        val repository = Repository()
//        repository.searchItems("%or%")
//            .observe(this) {
//                Log.d("tag", "onCreate: $it")
//            }

//        val queryText = "%or%"
//        viewModel.searchItemsFromDb(queryText)
//
//        viewModel.getAllItems()
//            .observe(this) {
//                Log.d("tag", "onCreate: $it")
//            }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) ||
                super.onSupportNavigateUp()
    }

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
//    public void createFragment(Fragment fg) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_view, fg)
//                .commit();
//    }
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
//private fun goToAddNewItemActivity() {
//    val intent = Intent(this, AddNewAndUpdateBatchActivity::class.java)
//    startActivity(intent)
//} //    private void fabOptionHide() {
////        fabOut.hide();
////        fabScan.hide();
////    }
//}