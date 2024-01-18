package com.example.barkodershopapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentHomeScreenBinding
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.adapters.*
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment(){
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    val productViewModel: ProductViewModel by viewModels()
    private var productAdapter : HomeProductAdapter? = null
    private var listAdapter : HomeListAdapter? = null
    var listList = arrayListOf<HistoryDataEntity>()
    var list = arrayListOf<ProductDataEntity>()
    val listViewMOdel: ListViewModel by viewModels()
    val historyViewModel : HistoryViewModel by viewModels()
    private var callback: OnBackPressedCallback? = null
    private lateinit var drawerLayout : DrawerLayout
    private var btnScan : FloatingActionButton? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        recViewList()
        observeListList()
        recViewProduct()
        observeListProduct()
        onBackButton()

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowHomeEnabled(false)

        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onCLickScan()
        requireActivity().title = requireContext().getString(R.string.homeScreen)
        binding.btnCreateProduct.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_addProductFragment,null,
                NavOptions.Builder().setPopUpTo(R.id.homeScreenFragment, true).build())
        }
        binding.btnCreateList.setOnClickListener {
            var bundle = Bundle()
            bundle.putBoolean("createSelect", true)
            findNavController().navigate(R.id.action_homeScreenFragment_to_listProductsFragment,bundle,
                NavOptions.Builder().setPopUpTo(R.id.homeScreenFragment, true).build())
        }

    }

    private fun recViewList(){
        listAdapter = HomeListAdapter(listList, listViewMOdel)
        listAdapter!!.setProductsList2(listList)

       var linear =  object : LinearLayoutManager(context) { override fun canScrollVertically() = false }

        binding.recViewCurrentlyLists.apply {
            layoutManager = linear

            adapter = listAdapter

        }
        binding.recViewCurrentlyLists.isNestedScrollingEnabled = false
    }

    private fun observeListList() {
        historyViewModel.allNotes.observe(viewLifecycleOwner, Observer { products ->
//            binding.progressBar3.visibility = View.GONE
            products.reverse()
            listAdapter!!.setProductsList2(products as ArrayList<HistoryDataEntity>)
//            searchView(products)
        })
    }

    private fun recViewProduct(){
        productAdapter = HomeProductAdapter(list, listViewMOdel)
        productAdapter!!.setProductsList2(list)

        var linear =  object : LinearLayoutManager(context) { override fun canScrollVertically() = false }

        binding.recViewCurentyProducts.apply {
            layoutManager = linear

            adapter = productAdapter

        }
        binding.recViewCurentyProducts.isNestedScrollingEnabled = false
    }

    private fun observeListProduct() {
        productViewModel.allNotes.observe(viewLifecycleOwner, Observer { products2 ->
//            binding.progressBar3.visibility = View.GONE
            products2.reverse()
            productAdapter!!.setProductsList2(products2 as ArrayList<ProductDataEntity>)
//            searchView(products)
        })
    }

    private fun onCLickScan() {

        btnScan = requireActivity().findViewById(R.id.fabNav)
        btnScan!!.setOnClickListener {
            findNavController().navigate(
                R.id.scanFragment,
                null,

            )
        }
    }
    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                requireActivity().finish()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)
    }

    override fun onPause() {
        super.onPause()

        observeListList()
        observeListProduct()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listAdapter = null
        productAdapter = null
        btnScan = null
        callback = null

    }




}

