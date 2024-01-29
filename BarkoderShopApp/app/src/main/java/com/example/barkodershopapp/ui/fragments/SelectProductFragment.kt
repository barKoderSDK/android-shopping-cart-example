package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentSelectProductBinding
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.SelectProductAdapter
import com.example.barkodershopapp.ui.listeners.onClickAddProduct
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SelectProductFragment : Fragment() {
    private var _binding: FragmentSelectProductBinding? = null
    private val binding get() = _binding!!
    val productViewModel: ProductViewModel by viewModels()
    private var selectAdapter: SelectProductAdapter? = null
    val listViewMOdel: ListViewModel by viewModels()
    var productS = ArrayList<ProductDataEntity>()
    private var btnScan : FloatingActionButton? = null
    private var callback: OnBackPressedCallback? = null
    private var product : ProductDataEntity? = null
    private var addedProducst : ArrayList<ProductDataEntity>? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectProductBinding.inflate(inflater, container, false)

        val toolbar = (activity as? AppCompatActivity)?.findViewById<Toolbar>(R.id.toolBarrr)
        toolbar?.visibility = View.VISIBLE
        setHasOptionsMenu(true)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addedProducst = ArrayList<ProductDataEntity>()
        setupRecView()
        onCLickScan()
        observeList()
        requireActivity().title = requireContext().getString(R.string.myProducts)
        btnDoneProducts()






        var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
        var createSelect = this@SelectProductFragment.arguments?.getBoolean("createSelect")



        if(editSelect == true) {
            selectAdapter!!.isEditMode = true
            val activity = requireActivity() as AppCompatActivity
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        } else if (createSelect == true) {
            val activity = requireActivity() as AppCompatActivity
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.setDisplayShowHomeEnabled(true)
        }
        else {
            selectAdapter!!.isEditMode = false
            val activity = requireActivity() as AppCompatActivity
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity.supportActionBar?.setDisplayShowHomeEnabled(false)
        }


        if(editSelect == true || createSelect == true) {
            selectAdapter!!.showAddButton = true
                var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
                bottomNav.visibility = View.GONE
                var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
                bottomFab.visibility = View.GONE
//                binding.guideline27.setGuidelinePercent(1F)
                onBackButton()
//            binding.btnDoneProducts.visibility = View.VISIBLE


        } else {
            selectAdapter!!.showAddButton = false
            var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
            bottomNav.visibility = View.VISIBLE
            var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
            bottomFab.visibility = View.VISIBLE

            binding.btnDoneProducts.visibility = View.GONE
        }
    }

    private fun btnDoneProducts() {
        binding.btnDoneProducts.setOnClickListener {
            var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
            var listId = this@SelectProductFragment.arguments?.getLong("listId")
            var checkedDate = this@SelectProductFragment.arguments?.getString("checkedDate")
            var bundle = Bundle()
            if(editSelect == true){
                bundle.putBoolean("editMode", true)
                bundle.putLong("currentListId", listId!!)
                bundle.putString("checkedDate", checkedDate)
            } else {
                bundle.putBoolean("editMode", false)
            }

            findNavController().navigate(
                R.id.listProductsFragment,
                bundle,
                NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build()
            )

        }
    }

    private fun observeList() {
        productViewModel.allNotes.observe(viewLifecycleOwner, Observer { products ->
            binding.progressBar3.visibility = View.GONE
            selectAdapter!!.setProductsList2(products as ArrayList<ProductDataEntity>)
//            searchView(products)

        })
    }

    private fun setupRecView() {
        var listId = this@SelectProductFragment.arguments?.getLong("listId")
        var checkedDate = this@SelectProductFragment.arguments?.getString("checkedDate")
        var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
        var listNameCreate = this@SelectProductFragment.arguments?.getString("listNameCreate")
        if(editSelect == true){
            selectAdapter = SelectProductAdapter(productS, listViewMOdel, listId!!, checkedDate!!,"", listener)
        } else {
            selectAdapter = SelectProductAdapter(productS, listViewMOdel, 0L, "","", listener)
        }



        selectAdapter!!.setProductsList2(productS)

        binding.recViewSelect.apply {
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(context)
            adapter = selectAdapter

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recViewSelect) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)
                    val archiveButton = archiveButton(position)

                    buttons = listOf(deleteButton, archiveButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(binding.recViewSelect)
        }
    }


    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        val originalIcon = ContextCompat.getDrawable(requireContext(), R.drawable.delete_item)
        val icon = originalIcon?.let { drawable ->
            val desiredSize = 100
            val scaledDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(drawable.toBitmap(), desiredSize, desiredSize, true))
            scaledDrawable.setBounds(0, 0, scaledDrawable.intrinsicWidth, scaledDrawable.intrinsicHeight)
            scaledDrawable
        }
        return SwipeHelper.UnderlayButton(
            requireContext(),
            icon!!,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.deleteProduct))
                        .setMessage(getString(R.string.deleteProductDialog))
                        .setPositiveButton(getString(R.string.delete)) { _, _ ->
                            selectAdapter!!.notifyItemRemoved(position)
                            productViewModel.deleteItem(selectAdapter!!.getSelectInt(position))
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                }
            }
        )
    }

    private var listener = object : onClickAddProduct {
        override fun onAdedProduct(list: ProductDataEntity) {
            product = list
            product!!.adedProduct = true
            addedProducst!!.add(list)
        }

    }

    private fun onCLickScan() {
        btnScan = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        btnScan!!.setOnClickListener {
            findNavController().navigate(
                R.id.scanFragment,
                null,
            )
        }
    }


    private fun archiveButton(position: Int): SwipeHelper.UnderlayButton {
        val originalIcon = ContextCompat.getDrawable(requireContext(), R.drawable.edit_list)
        val icon = originalIcon?.let { drawable ->
            val desiredSize = 100
            val scaledDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(drawable.toBitmap(), desiredSize, desiredSize, true))
            scaledDrawable.setBounds(0, 0, scaledDrawable.intrinsicWidth, scaledDrawable.intrinsicHeight)
            scaledDrawable
        }
        return SwipeHelper.UnderlayButton(
            requireContext(),
            icon!!,
            R.color.editButton,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    val currentProduct = selectAdapter!!.getSelectInt(position)
                    val action = SelectProductFragmentDirections.actionSelectProductFragmentToProductInfoFragment(
                        currentProduct
                    )
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        )
    }


//
//    private fun searchView(list: List<ProductDataEntity>) {
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText.isNullOrEmpty()) {
//                    selectAdapter!!.setProductsList(list as ArrayList<ProductDataEntity>)
//                } else {
//                    val filteredList = list.filter { product ->
//                        product.nameProduct!!.contains(newText, ignoreCase = true)
//                    } as ArrayList<ProductDataEntity>
//                    selectAdapter!!.setProductsList(filteredList)
//                }
//                return false
//            }
//        })
//    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
                var listId = this@SelectProductFragment.arguments?.getLong("listId")
                var checkedDate = this@SelectProductFragment.arguments?.getString("checkedDate")
                var bundle = Bundle()
                if(editSelect == true){
                    bundle.putBoolean("editMode", true)
                    bundle.putBoolean("selectBack", true)
                    bundle.putLong("currentListId", listId!!)
                    bundle.putString("checkedDate", checkedDate)
                } else {
                    bundle.putBoolean("selectBack", true)
                    bundle.putBoolean("editMode", false)
                }
                for(i in addedProducst!!) {
                    if(addedProducst!!.isNotEmpty()) {
                        productViewModel.updateItem(i)
                    }
                }
                findNavController().navigate(
                    R.id.listProductsFragment,
                    bundle,
                    NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build()
                )

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)
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
        selectAdapter = null
        btnScan = null
        callback = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                var editSelect = this@SelectProductFragment.arguments?.getBoolean("editSelect")
                var listId = this@SelectProductFragment.arguments?.getLong("listId")
                var checkedDate = this@SelectProductFragment.arguments?.getString("checkedDate")
                var bundle = Bundle()
                if(editSelect == true){
                    bundle.putBoolean("editMode", true)
                    bundle.putBoolean("selectBack", true)
                    bundle.putLong("currentListId", listId!!)
                    bundle.putString("checkedDate", checkedDate)
                } else {
                    bundle.putBoolean("selectBack", true)
                    bundle.putBoolean("editMode", false)
                }

                findNavController().navigate(
                    R.id.listProductsFragment,
                    bundle,
                    NavOptions.Builder().setPopUpTo(R.id.selectProductFragment, true).build()
                )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val addMenuItem2 = menu.findItem(R.id.addIcon2)
        addMenuItem2?.isVisible = false
    }



}


