package com.example.barkodershopapp.ui.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.CursorWindow
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ToolbarWidgetWrapper
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.ActivityMainBinding
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.fragments.HistoryListFragment
import com.example.barkodershopapp.ui.fragments.ListProductsFragment
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val REQ_CODE_SPEECH_INPUT = 100
    private var isEditMode: Boolean = false
    val productViewModel : ProductViewModel by viewModels()
    val listViewModel: ListViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null
//    private lateinit var drawerLayout: DrawerLayout
    private var currentFragment:    Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        drawerLayout = binding.drawerLayout

        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBarrr)
        setSupportActionBar(toolBar)


//        val navigationView = binding.drawerView
//        navigationView.setNavigationItemSelectedListener(this)

//        val toogle = ActionBarDrawerToggle(
//            this,
//            drawerLayout,
//            toolBar,
//            R.string.open_nav,
//            R.string.close_nav
//        )
//        drawerLayout.addDrawerListener(toogle)
//        toogle.syncState()

        setDefaultLanguage()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.background = null
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        bottomNavigationView.setupWithNavController(navController!!)
        bottomNavigationView.selectedItemId = R.id.homeScreenFragment
        val currentFragment = navHostFragment.childFragmentManager.primaryNavigationFragment

        toolBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.searchIcon -> promptSpeechInput()
                R.id.addIcon -> {
                    if(bottomNavigationView.selectedItemId == R.id.historyListFragment2) {
                        navController!!.navigate(R.id.dialogListCreate)
                        listViewModel.delete()
                    }
                    else if (bottomNavigationView.selectedItemId == R.id.selectProductFragment) {
                        navController!!.navigate(
                            R.id.addProductFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build()
                        )
                    }
                }
                R.id.addIcon2 -> {
                        var listId = currentFragment?.arguments?.getLong("currentListId")
                        var checkedDate = currentFragment?.arguments?.getString("checkedDate").toString()
                        var listName = currentFragment?.arguments?.getString("listName")
                        var edit = currentFragment?.arguments?.getBoolean("edit")
                        var editMode = currentFragment?.arguments?.getBoolean("editMode")
                        Log.d("listName", editMode.toString())
                        if(editMode == true) {
                            var bundle = Bundle()
                            bundle.putBoolean("editSelect", true)
                            bundle.putLong("listId",listId!!)
                            bundle.putString("checkedDate", checkedDate)
                            bundle.putString("listName", listName)
                            navController!!.navigate(
                                R.id.selectProductFragment,
                                bundle,
                                NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                            )
                        } else {
                            var bundle = Bundle()
                            bundle.putBoolean("createSelect", true)
                            bundle.putString("listName", listName)
                            bundle.putString("listNameCreate", listName)
                            navController!!.navigate(
                                R.id.selectProductFragment,
                                bundle,
                                NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                            )
                        }
                }
            }
            true
        }

    }




    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt))
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PREF_SELECTED_LANGUAGE = "selected_language"
        private const val PREF_SELECTED_CURRENCY = "selected_currency"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setDefaultLanguage() {
        val locale = Locale("en-us")
        Locale.setDefault(locale)

        val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.getOrNull(0)
            if (spokenText != null) {
                var bundle = Bundle()
                bundle.putString("voiceString", spokenText)
                bundle.putBoolean("voiceBoolean", true)
                navController!!.navigate(R.id.addProductFragment, bundle)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navController = null
        listViewModel.delete()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.drawerHome -> navController!!.navigate(R.id.homeScreenFragment)
            R.id.drawerAbout -> navController!!.navigate(R.id.addProductFragment)
            R.id.drawerSettings -> navController!!.navigate(R.id.settingsFragment)
        }
//        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentFragment(): Fragment? {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentList = fragmentManager.fragments

        for (fragment in fragmentList) {
            if (fragment.isVisible) {
                return fragment
            }
        }

        return null
    }






}
