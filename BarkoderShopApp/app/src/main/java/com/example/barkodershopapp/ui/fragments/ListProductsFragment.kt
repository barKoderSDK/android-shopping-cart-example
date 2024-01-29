package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentListProductsBinding
import com.example.barkodershopapp.ui.listeners.OnClickListenerButtons
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.adapters.ProductAdapter
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ListProductsFragment : Fragment() {
    private var _binding: FragmentListProductsBinding? = null
    private val binding get() = _binding!!

    private var productAdapter: ProductAdapter? = null
    private val historyViewModel: HistoryViewModel by viewModels()
    private val listViewModel: ListViewModel by viewModels()
    private val productViewModel : ProductViewModel by viewModels()
    private val productL = arrayListOf<ListDataEntity>()
    var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")
    private var updatedProduct : ListDataEntity? = null
    private var totalPrice : Int? = null
    private var listName : String? = null
    private var listNameViewModel : String? = null


    private lateinit var callback: OnBackPressedCallback



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListProductsBinding.inflate(inflater, container, false)
        var selectBack = this@ListProductsFragment.arguments?.getBoolean("selectBack")

        setHasOptionsMenu(true)

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)




        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")

        setupRecView()
        observeList()
        onBackButton()
        checkAdedProduct()
        editMode()
        onClickButtonAdd()
        onClickButtonUpdate()
        navInvisible()
        onButtonSelect()
        listName = this@ListProductsFragment.arguments?.getString("listName")
        if(listName != null) {
            val sharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
            val editor = sharedPreferences!!.edit()

            editor.putString("listN", listName)
            editor.apply()
        }

        val sharedPreferences = requireContext().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val storedValue = sharedPreferences.getString("listN", "defaultValue")
            requireActivity().title = storedValue

    }


    private fun checkAdedProduct() {
        var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")
        if(editMode == true) {
            listViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                var array = products as ArrayList<ListDataEntity>
                for (i in array.indices) {
                    val item = array[i]
                    productViewModel.allNotes.observe(viewLifecycleOwner, { products2 ->
                        var array2 = products2 as ArrayList<ProductDataEntity>
                        for (i in array2.indices) {
                            val item2 = array2[i]
                            if (item.listProducts.barcodeProduct == item2.barcodeProduct) {
                                item2.adedProduct = true
                            }
                        }
                    })
                }
            })
        }
    }

    private fun onButtonSelect(){
//        binding.buttonImage.setOnClickListener {
//            var listId = this@ListProductsFragment.arguments?.getLong("currentListId")
//            var checkedDate = this@ListProductsFragment.arguments?.getString("checkedDate").toString()
//            var listName = this@ListProductsFragment.arguments?.getString("listName").toString()
//            var edit = this@ListProductsFragment.arguments?.getBoolean("edit")
//            var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")
//            if(editMode == true) {
//                var bundle = Bundle()
//                bundle.putBoolean("editSelect", true)
//                bundle.putLong("listId",listId!!)
//                bundle.putString("checkedDate", checkedDate)
//                bundle.putString("listName", listName)
//                findNavController().navigate(
//                    R.id.selectProductFragment,
//                    bundle,
//                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
//                )
//            } else {
//                var bundle = Bundle()
//                bundle.putBoolean("createSelect", true)
//                bundle.putString("listNameCreate", binding.editTextListName.text.toString())
//                findNavController().navigate(
//                    R.id.selectProductFragment,
//                    bundle,
//                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
//                )
//            }
//            }
        }

    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }

    private fun setupRecView() {
        productAdapter = ProductAdapter(requireContext(),productL, clickListenerButtons)
        binding.recViewProductList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
            binding.progressBar2.visibility = View.GONE

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recViewProductList) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)

                    buttons = listOf(deleteButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(binding.recViewProductList)
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

                            productAdapter!!.notifyItemRemoved(position)
                            listViewModel.deleteItem(productAdapter!!.getProductInt(position))


                            var product1 = productAdapter!!.getProductInt(position)
                            productViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                                var array = products as ArrayList<ProductDataEntity>
                                for(i in array.indices){
                                    val item = array[i]
                                    if(product1.listProducts.barcodeProduct == item.barcodeProduct
                                        && product1.listProducts.nameProduct == item.nameProduct
                                    ){
                                        item.adedProduct = false
                                    }
                                    productViewModel.updateItem(item)
                                }
                            })




                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                }
            }
        )
    }

    private fun onClickButtonUpdate() {

        var listId = this@ListProductsFragment.arguments?.getLong("currentListId")
        var checkedDate = this@ListProductsFragment.arguments?.getString("checkedDate").toString()
        binding.btnUpdateList.setOnClickListener {

                productViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                    var array = products as ArrayList<ProductDataEntity>
                    for(i in array.indices){
                        val item = array[i]
                        item.adedProduct = false
                        productViewModel.updateItem(item)
                    }
                })



                listViewModel.allNotes.observe(viewLifecycleOwner, { products4 ->
                    var currentList = HistoryDataEntity(
                        requireActivity().title.toString(), getCurrentDate(),
                        sumTotalCostList(products4).toString(),
                        checkedDate!!,
                        false, products4 as ArrayList<ListDataEntity>, listId!!
                    )
                    historyViewModel.updateItem(currentList)
                    listViewModel.delete()
                })
                findNavController().navigate(
                    R.id.historyListFragment2,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )
                Toast.makeText(context, R.string.toast_list_sucessful_updated, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickButtonAdd() {
        binding.btnSaveList.setOnClickListener {

                productViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                    var array = products as ArrayList<ProductDataEntity>
                    for(i in array.indices){
                        val item = array[i]
                        item.adedProduct = false
                        productViewModel.updateItem(item)
                    }
                })

                listViewModel.allNotes.observe(viewLifecycleOwner, { products3 ->
                    historyViewModel.insert(
                        HistoryDataEntity(
                            requireActivity().title.toString(),
                            getCurrentDate(),
                            sumTotalCostList(products3).toString(),
                            "Not checked yet!",
                            false,
                            products3 as ArrayList<ListDataEntity>
                        )
                    )
                })
                listViewModel.delete()

                findNavController().navigate(
                    R.id.historyListFragment2,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )

                Toast.makeText(context, R.string.toast_list_secesfful_created, Toast.LENGTH_SHORT).show()

        }

    }

    private fun observeList() {
        listViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                productAdapter!!.setNotesList(products as ArrayList<ListDataEntity>)
                binding.textTotalCostList.text = sumTotalCostList(products).toString() + " $"
                totalPrice = sumTotalCostList(products)
                binding.textSizeProducstsList.text = products.size.toString()
                binding.progressBar2.visibility = View.GONE


        })
    }

    private fun editMode() {
        var editMode = this@ListProductsFragment.arguments?.getBoolean("editMode")
        var listName = this@ListProductsFragment.arguments?.getString("listName")
        var backUpdate = this@ListProductsFragment.arguments?.getBoolean("backUpdate")

        if (editMode == true) {
            binding.btnUpdateList.visibility = View.INVISIBLE
        } else {
            binding.btnUpdateList.visibility = View.INVISIBLE
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }

    private fun sumTotalCostList(products: MutableList<ListDataEntity>): Int {
        var sum = 0
        for (product in products) {
            sum += product.listProducts.totalPrice
        }
        return sum
    }




    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {
                listViewModel.delete()
                productViewModel.allNotes.observe(viewLifecycleOwner, { products ->
                    var array = products as ArrayList<ProductDataEntity>
                    for(i in array.indices){
                        val item = array[i]
                        item.adedProduct = false
                        productViewModel.updateItem(item)
                    }
                })

                findNavController().navigate(
                    R.id.historyListFragment2,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.listProductsFragment, true).build()
                )


            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private val clickListenerButtons = object : OnClickListenerButtons {
        override fun onClickPlus(list: ListDataEntity) {
            list.listProducts.count++
            list.listProducts.totalPrice =
                list.listProducts.totalPrice + list.listProducts.priceProduct
            totalPrice = totalPrice?.plus(list.listProducts.priceProduct)
            binding.textTotalCostList.text = "$${totalPrice.toString()}"
            updatedProduct = list
//            listViewModel.updateItem(list)
        }

        override fun onClickMinus(list: ListDataEntity) {
            if (list.listProducts.count >= 2) {
                list.listProducts.count--
                list.listProducts.totalPrice =
                    list.listProducts.totalPrice - list.listProducts.priceProduct
                totalPrice = totalPrice?.minus(list.listProducts.priceProduct)
                binding.textTotalCostList.text = "${totalPrice.toString()} $"
                updatedProduct = list
            }
//            listViewModel.updateItem(list)
        }
    }


    override fun onPause() {
        super.onPause()
        if(updatedProduct != null) {
            listViewModel.updateItem(updatedProduct!!)
        }
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productAdapter = null
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val messagesMenuItem = menu.findItem(R.id.searchIcon)
        val addMenuItem = menu.findItem(R.id.addIcon)
        messagesMenuItem?.isVisible = false
        addMenuItem?.isVisible = false
    }

}