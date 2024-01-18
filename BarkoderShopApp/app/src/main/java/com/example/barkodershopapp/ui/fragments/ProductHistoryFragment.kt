package com.example.barkodershopapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentProductHistoryBinding
import com.example.barkodershopapp.data.db.pricedb.PriceHistory
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.PriceHistoryAdapter
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ProductHistoryFragment : Fragment() {
    private var _binding: FragmentProductHistoryBinding? = null
    private val binding get() = _binding!!
    private var priceAdapter: PriceHistoryAdapter? = null
    private val args by navArgs<ProductHistoryFragmentArgs>()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductHistoryBinding.inflate(inflater, container, false)

        setupRecView()
        setupTextViews()
        navInvisible()
        onClickCancel()
        onBackButton()

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)



        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

    private fun onClickCancel(){
        var scanned = this@ProductHistoryFragment.arguments?.getBoolean("scanned")
        if(scanned == true) {
            binding.btnCancel.setOnClickListener {
                findNavController().navigate(
                    R.id.homeScreenFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.scanFragment, true).build()
                )
            }
        } else {
            binding.btnCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }

    private fun setupTextViews(){

        var scanned = this@ProductHistoryFragment.arguments?.getBoolean("scanned")
        var productId = this@ProductHistoryFragment.arguments?.getLong("productId")
        var productName = this@ProductHistoryFragment.arguments?.getString("productName")
        var productBarcode = this@ProductHistoryFragment.arguments?.getString("productBarcode")
        var productUnit = this@ProductHistoryFragment.arguments?.getString("productUnit")
        var productQuantity = this@ProductHistoryFragment.arguments?.getString("productQuantity")
        var productPrice = this@ProductHistoryFragment.arguments?.getString("productPrice")
        var productImage = this@ProductHistoryFragment.arguments?.getByteArray("productImage")
        if(scanned == true) {
            requireActivity().title = productName
            binding.textActivityName.setText(productName)
            binding.textNameInfo.setText(productName)
            binding.textBarcodeInfo.setText(productBarcode)
            binding.textPriceInfo.setText(productPrice)
            binding.textQuantityInfo.setText(productQuantity)
            binding.textUnitInfo.setText(productUnit)
            val byteArray = productImage?.let { TypeConverterss.toBitmap(it) }
            binding.imageProductInfo.load(byteArray) {
                crossfade(true)
            }

        } else {
            binding.textActivityName.setText(args.currentProduct.nameProduct)
            binding.textNameInfo.setText(args.currentProduct.nameProduct)
            binding.textBarcodeInfo.setText(args.currentProduct.barcodeProduct)
            binding.textPriceInfo.setText(args.currentProduct.priceProduct.toString())
            binding.textQuantityInfo.setText(args.currentProduct.quantityProduct.toString())
            binding.textUnitInfo.setText(args.currentProduct.unitProduct)

            val byteArray = args.currentProduct.imageProduct?.let { TypeConverterss.toBitmap(it) }
            binding.imageProductInfo.load(byteArray) {
                crossfade(true)
            }
        }

    }



    private fun setupRecView(){
        var scanned = this@ProductHistoryFragment.arguments?.getBoolean("scanned")
        var priceHistory = this@ProductHistoryFragment.arguments?.getSerializable("priceHistory") as? ArrayList<PriceHistory>
        if(scanned == true) {
            priceAdapter = PriceHistoryAdapter(priceHistory!!)
            priceAdapter!!.setPricesList(priceHistory!!)
            if(priceHistory.size == 0) {
                binding.recViewPrice.visibility = View.GONE
                binding.priceCardViewText.visibility = View.VISIBLE
            } else {
                binding.recViewPrice.visibility = View.VISIBLE
                binding.priceCardViewText.visibility = View.GONE
            }
        } else {
            priceAdapter = PriceHistoryAdapter(args.currentProduct.priceHistory)
            priceAdapter!!.setPricesList(args.currentProduct.priceHistory)
            if(args.currentProduct.priceHistory.size == 0) {
                binding.recViewPrice.visibility = View.GONE
                binding.priceCardViewText.visibility = View.VISIBLE
            } else {
                binding.recViewPrice.visibility = View.VISIBLE
                binding.priceCardViewText.visibility = View.GONE
            }
        }

        binding.recViewPrice.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = priceAdapter
        }


    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                var scanned = this@ProductHistoryFragment.arguments?.getBoolean("scanned")
//                if(scanned == true) {
//
//                        findNavController().navigate(
//                            R.id.homeScreenFragment,
//                            null,
//                            NavOptions.Builder().setPopUpTo(R.id.scanFragment, true).build()
//                        )
//
//                } else {
//
//                    findNavController().navigate(
//                        R.id.selectProductFragment,
//                        null,
//                        NavOptions.Builder().setPopUpTo(R.id.productHistoryFragment, true).build()
//                    )
//
//                }
                findNavController().popBackStack()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
        priceAdapter = null

    }
}



