package com.example.barkodershopapp.ui.fragments

import android.app.AlertDialog
import android.database.CursorWindow
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentHistoryListBinding
import com.example.barkodershopapp.ui.listeners.swipecallback.SwipeToDelete
import com.example.barkodershopapp.data.db.historydatabase.HistoryDataEntity
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.adapters.HistoryAdapter
import com.example.barkodershopapp.ui.listeners.swipeicons.SwipeHelper
import com.example.barkodershopapp.ui.viewmodels.HistoryViewModel
import com.example.barkodershopapp.ui.viewmodels.ListViewModel
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.lang.reflect.Field

@AndroidEntryPoint
class HistoryListFragment : Fragment() {
    private var _binding: FragmentHistoryListBinding? = null
    private val binding get() = _binding!!
    private var historyAdapter : HistoryAdapter? = null
    private val historyViewmodel: HistoryViewModel by viewModels()
    private val listViewModel : ListViewModel by viewModels()
    private val productViewModel : ProductViewModel by viewModels()
    private val listH = arrayListOf<HistoryDataEntity>()
    private var callback: OnBackPressedCallback? = null
    private var btnScan : FloatingActionButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHistoryListBinding.inflate(inflater, container, false)
        requireActivity().title = requireContext().getString(R.string.myLists)
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.setAccessible(true)
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setupRecView()
        observeList()
        onClickNewList()
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



//        swipeDelete()
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

    private fun onClickNewList(){
        binding.fabNewList.setOnClickListener {
            findNavController().navigate(
                R.id.listProductsFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.historyListFragment2, true).build()
            )
            listViewModel.delete()

        }
    }

    private fun setupRecView() {
        historyAdapter = HistoryAdapter(listH, requireContext())

        binding.recViewHistory2.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter

            val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recViewHistory2) {
                override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                    var buttons = listOf<UnderlayButton>()
                    val deleteButton = deleteButton(position)
                    val archiveButton = archiveButton(position)

                    buttons = listOf(deleteButton,archiveButton)
                    return buttons
                }
            })

            itemTouchHelper.attachToRecyclerView(binding.recViewHistory2)
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
                        .setTitle(getString(R.string.deleteList))
                        .setMessage(getString(R.string.deleteListDialog))
                        .setPositiveButton(getString(R.string.delete)) { _, _ ->
                            historyAdapter!!.notifyItemRemoved(position)
                            historyViewmodel.deleteItem(historyAdapter!!.getHistoryInt(position))
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                }
            }
        )
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
                    val currentList = historyAdapter!!.getHistoryInt(position)
                    val bundle = Bundle()
                    bundle.putBoolean("editMode", true)
                    bundle.putLong("currentListId", currentList.id)
                    bundle.putString("checkedDate", currentList.checkedDate)
                    bundle.putString("listName", currentList.listName)
                    for (i in currentList.listProducts) {
                        listViewModel.insert(i)
                    }

                    findNavController().navigate(
                        R.id.listProductsFragment,
                        bundle,
                        NavOptions.Builder().setPopUpTo(R.id.currentListFragment2, true).build()
                    )
                }
            }
        )
    }

    private fun observeList() {
        historyViewmodel.allNotes.observe(viewLifecycleOwner, Observer { products4 ->
            historyAdapter!!.setNotesList(products4)
            binding.progressBar4.visibility = View.GONE
        })
    }


    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().navigate(
                    R.id.homeScreenFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.historyListFragment2, true).build()
                )

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        historyAdapter = null
        btnScan = null
        callback = null

    }

}
