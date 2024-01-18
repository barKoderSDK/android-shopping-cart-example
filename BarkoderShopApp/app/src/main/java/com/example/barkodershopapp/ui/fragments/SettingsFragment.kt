package com.example.barkodershopapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSettingsBinding
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val productViewModel : ProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentSettingsBinding.inflate(inflater, container, false)
        requireActivity().title = "Settings"
        navInvisible()

        val view = binding.root
        return view

    }

    companion object {
        private const val PREF_SELECTED_LANGUAGE = "selected_language"
        private const val PREF_SELECTED_CURRENCY = "selected_currency"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val selectedCurrency = sharedPreferences.getString(PREF_SELECTED_CURRENCY, "EU")
        val selectedLanguage = sharedPreferences.getString(PREF_SELECTED_LANGUAGE, "en-us")

        when(selectedCurrency){
            "EU" -> binding.radioCurrencyEU.isChecked = true
            "Denari" -> binding.radioCurrencyDenar.isChecked = true
        }

        binding.radioGroup2.setOnCheckedChangeListener { _, checkedId ->
            val newCurrency = when(checkedId) {
                R.id.radioCurrencyEU -> "EU"
                R.id.radioCurrencyDenar -> "Denari"
                else -> "EU"
            }
            val editor2 = sharedPreferences.edit()
            editor2.putString(PREF_SELECTED_CURRENCY, newCurrency)
            editor2.apply()
            if(newCurrency == "Denari"){
                denariCurrency()
            }
            else if(newCurrency == "EU"){
                euroCurrency()
            }
        }

        when (selectedLanguage) {
            "en-us" -> binding.radioEnglish.isChecked = true
            "mk" -> binding.radioMacedonia.isChecked = true
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newLanguage = when (checkedId) {
                R.id.radioEnglish -> "en-us"
                R.id.radioMacedonia -> "mk"
                else -> "en-us"
            }


            val editor = sharedPreferences.edit()
            editor.putString(PREF_SELECTED_LANGUAGE, newLanguage)
            editor.apply()

            setLocale(Locale(newLanguage))
        }
    }
    private fun denariCurrency() {
        productViewModel.allNotes.observe(viewLifecycleOwner, { products ->
            val updatedProducts = ArrayList<ProductDataEntity>()

            for (product in products) {
                val updatedPrice = product.priceProduct * 61
                val updatedTotalPrice = product.totalPrice * 61
                product.totalPrice = updatedTotalPrice
                product.priceProduct = updatedPrice
                updatedProducts.add(product)
            }

            val updatedLiveData = MutableLiveData<MutableList<ProductDataEntity>>()
            updatedLiveData.value = updatedProducts

            productViewModel.allNotes = updatedLiveData
        })
    }
    private fun euroCurrency(){
        productViewModel.allNotes.observe(viewLifecycleOwner, { products ->
            val updatedProducts = ArrayList<ProductDataEntity>()

            for (product in products) {
                val updatedTotalPrice = product.totalPrice / 61
                val updatedPrice = product.priceProduct / 61
                product.totalPrice = updatedTotalPrice
                product.priceProduct = updatedPrice
                updatedProducts.add(product)
            }

            val updatedLiveData = MutableLiveData<MutableList<ProductDataEntity>>()
            updatedLiveData.value = updatedProducts

            productViewModel.allNotes = updatedLiveData
        })
    }

    private fun setLocale(locale: Locale) {
        val resources = requireActivity().resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



