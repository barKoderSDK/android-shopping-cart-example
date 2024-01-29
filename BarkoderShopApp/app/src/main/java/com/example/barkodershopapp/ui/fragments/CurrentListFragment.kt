package com.example.barkodershopapp.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.StatusBarManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentCurrentListBinding
import com.example.barkodershopapp.data.db.listdatabase.ListDataEntity
import com.example.barkodershopapp.ui.adapters.CurrentListAdapter
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.activities.MainActivity
import com.example.barkodershopapp.ui.listeners.OnCheckedListener
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.descriptors.StructureKind
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CurrentListFragment : Fragment(), BarkoderResultCallback {
    private var _binding: FragmentCurrentListBinding? = null
    private val binding get() = _binding!!
    private var currentAdapter: CurrentListAdapter? = null
    val listViewModel: ListViewModel by viewModels()
    val historyViewModel: HistoryViewModel by viewModels()
    val productViewModel : ProductViewModel by viewModels()
    private val args by navArgs<CurrentListFragmentArgs>()
    private lateinit var callback: OnBackPressedCallback
    private var barcodeNum2 : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentListBinding.inflate(inflater, container, false)
        binding.bkdView2.config = BarkoderConfig(
            context,
            "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqOVL7EGJ4cmhf5j5WSDVkDVoOEcowlOGyJMbrM3DMQmyFpfmE8J8Fc-I7lITXErfWK52VDxXCvYPCPgLX2sIAc9Du6ble1kIKmg8JAXwcrGuJeKGgpyo_FOcXuraHs9NUmkXc-5TqlOTC93SznoT6wcec4NEAV9IhhRCse0L8NcFxSwRmvB3TIkLHjmvIsQt2LlzgSwU7gy9Hpyj9KoPYqdmeNwTdG89ShKpQ7fbXvFokUcjjSQwdfL09AD_-TPb-V5BU_Jd0oQmYxREIC0FZyk"
        )
        {
            Log.i("LicenseInfo", it.message)
        }
        setHasOptionsMenu(true)
        navInvisible()
        toolBarName()
        scannedPro()

        val toolbar = (activity as? AppCompatActivity)?.findViewById<Toolbar>(R.id.toolBarrr)
        toolbar?.visibility = View.VISIBLE

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextViews()
        onClickScan()
//        onClickStart()
        onClickStop()
        onClickRestore()
        onBackButton()
        setBarkoderSettings()
        setActiveBarcodeTypes()
        setupRecView()

    }


    private fun toolBarName() {
        requireActivity().title = args.currentList.listName
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    private fun navInvisible(){
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }
    private fun getBarcodeString(){
//        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("barcodeNum2")
//            ?.observe(viewLifecycleOwner) { result ->
//                highlightProductByBarcode(result)
//            }
        if(barcodeNum2 != null) {
            highlightProductByBarcode(barcodeNum2!!)
        }

    }
    private fun setupTextViews(){
        binding.textView25.text = args.currentList.listProducts.size.toString()
        binding.textTotalCostP.text = "$ " + sumTotalCost(args.currentList.listProducts).toString()
    }

    private fun onClickScan(){
        binding.btnStartList.setOnClickListener {
            listViewModel.isListStarted = !listViewModel.isListStarted
            currentAdapter!!.showCheckboxes = !currentAdapter!!.showCheckboxes
            if (listViewModel.isListStarted && currentAdapter!!.showCheckboxes) {
                currentAdapter!!.notifyDataSetChanged()

            } else {
                currentAdapter!!.notifyDataSetChanged()

            }
            binding.btnStartList.visibility = View.INVISIBLE
            binding.btnStopList.visibility = View.VISIBLE
            binding.bkdView2.visibility = View.VISIBLE
            binding.textView23.visibility = View.VISIBLE
            binding.textView24.visibility = View.VISIBLE
            binding.textView25.visibility = View.VISIBLE
            binding.textView26.visibility = View.VISIBLE
            binding.textTotalCostP.visibility = View.VISIBLE
            binding.scannedProductsP.visibility = View.VISIBLE
            binding.textView27.visibility = View.INVISIBLE
            binding.bkdView2.startScanning(this)
        }
    }

    private fun onClickStop() {
        binding.btnStopList.setOnClickListener {
            listViewModel.isListStarted = !listViewModel.isListStarted
            currentAdapter!!.showCheckboxes = !currentAdapter!!.showCheckboxes
            if (!listViewModel.isListStarted && !currentAdapter!!.showCheckboxes) {
                currentAdapter!!.notifyDataSetChanged()

            } else {
                currentAdapter!!.notifyDataSetChanged()

            }
            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.textActivityName.text = args.currentList.listName
            binding.btnRestoreCheckout.visibility = View.GONE
            binding.btnStopList.visibility = View.GONE
            binding.btnStartList.visibility = View.VISIBLE
            binding.btnRestoreCheckout.visibility = View.GONE
            binding.bkdView2.visibility = View.GONE
            binding.textView23.visibility = View.INVISIBLE
            binding.textView24.visibility = View.INVISIBLE
            binding.textView25.visibility = View.INVISIBLE
            binding.textView26.visibility = View.INVISIBLE
            binding.textTotalCostP.visibility = View.INVISIBLE
            binding.scannedProductsP.visibility = View.INVISIBLE
            binding.textView27.visibility = View.VISIBLE
            binding.bkdView2.stopScanning()
            for (i in args.currentList.listProducts) {
                i.listProducts.defultCount = 0
                i.listProducts.checkout = false
            }
            scannedPro()
        }
    }

    private fun onClickStart(){
        binding.btnStartList.setOnClickListener {


            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))

            binding.btnRestoreCheckout.visibility = View.GONE
            binding.bkdView2.visibility = View.VISIBLE
            binding.btnStartList.visibility = View.GONE
            binding.btnStopList.visibility = View.VISIBLE
            binding.bkdView2.startScanning(this)
        }

    }
    private fun onClickRestore(){
        binding.btnRestoreCheckout.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.restoreList))
                .setMessage(getString(R.string.restoreListDialog))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    for (i in args.currentList.listProducts) {
                        i.listProducts.defultCount = 0
                        i.listProducts.checkout = false
                    }
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
    }

    private fun setupRecView() {
        currentAdapter = CurrentListAdapter(args.currentList.listProducts, productViewModel, onCheckedListener)
        currentAdapter!!.setNotesList(args.currentList.listProducts)
        binding.currentRecView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = currentAdapter
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }
    private fun sucessfullCheckout() {
        var allCheckedOut = true
        for (listData in args.currentList.listProducts) {
            if (listData.listProducts.checkout == false) {
                allCheckedOut = false
                break
            }
        }
        if (allCheckedOut) {
            binding.bkdView2.visibility = View.GONE
            if (listViewModel.isListStarted) {
                binding.sucessfullCard.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                binding.scanLayout.visibility = View.GONE
                args.currentList.checkedList = true
                args.currentList.checkedDate = getCurrentDate()
                historyViewModel.updateItem(args.currentList)


                for (i in args.currentList.listProducts) {
                    i.listProducts.defultCount = 0
                    i.listProducts.checkout = false
                    listViewModel.isListStarted = false
                }
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        findNavController().previousBackStackEntry
                        findNavController().popBackStack()
                        changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
                    }
                }, 2000)
            }
        }
    }
    private fun scannedPro() {
        var numScannedProduct = 0
        for (i in args.currentList.listProducts) {
            if (i.listProducts.checkout) {
                numScannedProduct++
            }
            binding.scannedProductsP.text = numScannedProduct.toString()
        }
    }

    private fun sumTotalCost(products: MutableList<ListDataEntity>): Int {
        var sum = 0

        for (product in products) {
            sum += product.listProducts.totalPrice
        }
        return sum
    }

    private fun highlightProductByBarcode(barcode: String) {
        for (i in args.currentList.listProducts.indices) {
            if (args.currentList.listProducts[i].listProducts.barcodeProduct == barcode &&
                args.currentList.listProducts[i].listProducts.defultCount < args.currentList.listProducts[i].listProducts.count
            ) {
                args.currentList.listProducts[i].listProducts.defultCount++
                currentAdapter!!.notifyItemChanged(i)
                if (args.currentList.listProducts[i].listProducts.count == args.currentList.listProducts[i].listProducts.defultCount) {
                    args.currentList.listProducts[i].listProducts.checkout = true
                    args.currentList.listProducts[i].listProducts.defultCount =
                        args.currentList.listProducts[i].listProducts.count
                    currentAdapter!!.notifyItemChanged(i)
                    break
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (listViewModel.isListStarted) {
            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.textActivityName.text = "Checkout Mode"
            currentAdapter!!.showCheckboxes = true
            binding.btnRestoreCheckout.visibility = View.GONE
        } else {
            changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.include.toolBarr.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))
            binding.textActivityName.text = args.currentList.listName
            currentAdapter!!.showCheckboxes = false
            binding.btnRestoreCheckout.visibility = View.GONE
        }

    }

    val onCheckedListener = object  : OnCheckedListener {
        override fun onCheckedChanged(list : ListDataEntity) {
            list.listProducts.checkout = !list.listProducts.checkout
            for(i in args.currentList.listProducts.indices){
                if(args.currentList.listProducts[i].listProducts.checkout){
                    args.currentList.listProducts[i].listProducts.defultCount =
                        args.currentList.listProducts[i].listProducts.count
                    currentAdapter!!.notifyItemChanged(i)
                } else {
                    args.currentList.listProducts[i].listProducts.defultCount = 0
                    currentAdapter!!.notifyItemChanged(i)
                }
                scannedPro()
            }

           listViewModel.updateItem(list)
            binding.bkdView2.visibility = View.GONE
            sucessfullCheckout()
        }
    }

    override fun onPause() {
        super.onPause()

        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.VISIBLE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.VISIBLE
    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

//                findNavController().navigate(
//                    R.id.historyListFragment2,
//                    null,
//                    NavOptions.Builder().setPopUpTo(R.id.currentListFragment2, true).build()
//                )
                findNavController().popBackStack()
                changeStatusBarColor(ContextCompat.getColor(requireContext(), R.color.toolBarColor))

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun changeStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        currentAdapter = null

    }

    private fun setActiveBarcodeTypes() {
        // There is option to set multiple active barcodes at once as array
        binding.bkdView2.config.decoderConfig.SetEnabledDecoders(
            arrayOf(
                Barkoder.DecoderType.QR,
                Barkoder.DecoderType.Ean13
            )
        )
        // or configure them one by one
        binding.bkdView2.config.decoderConfig.UpcA.enabled = true
    }

    private fun setBarkoderSettings() {
        // These are optional settings, otherwise default values will be used
        binding.bkdView2.config.let { config ->
            config.isImageResultEnabled = true
            config.isLocationInImageResultEnabled = true
            config.isRegionOfInterestVisible = true
            config.isPinchToZoomEnabled = true
            config.setRegionOfInterest(5f, 5f, 90f, 90f)
            config.thresholdBetweenDuplicatesScans = 2
            config.isCloseSessionOnResultEnabled = false
        }
    }

    private fun updateUI(result: Barkoder.Result? = null, resultImage: Bitmap? = null) {
        barcodeNum2 = result?.textualData
        getBarcodeString()
        scannedPro()
        sucessfullCheckout()

//        findNavController().previousBackStackEntry?.savedStateHandle?.set(
//            "barcodeNum2",
//            barcodeNum2
//        )
//        findNavController().popBackStack()
    }

    override fun scanningFinished(
        results: Array<out Barkoder.Result>?,
        p1: Array<out Bitmap>?,
        p2: Bitmap?
    ) {
        if (results!!.isNotEmpty())
            updateUI(results[0], p2!!)
        else
            updateUI()
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val messagesMenuItem = menu.findItem(R.id.searchIcon)
        val addMenuItem = menu.findItem(R.id.addIcon)
        val addMenuItem2 = menu.findItem(R.id.addIcon2)

        messagesMenuItem?.isVisible = false
        addMenuItem2?.isVisible = false
        addMenuItem?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}