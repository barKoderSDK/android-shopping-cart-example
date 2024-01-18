package com.example.barkodershopapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import coil.load
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentAddProductBinding
import com.example.barkodershopapp.data.db.pricedb.PriceHistory
import com.example.barkodershopapp.data.db.productdatabase.ProductDataEntity
import com.example.barkodershopapp.ui.viewmodels.ProductViewModel
import com.example.barkodershopapp.ui.typeconverters.TypeConverterss
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddProductFragment : Fragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val productViewModel: ProductViewModel by viewModels()
    private val cameraRequest = 1
    private var callback: OnBackPressedCallback? = null
    private val REQ_CODE_SPEECH_INPUT = 100
    private var btnScan : FloatingActionButton? = null
    private var imageProduct : Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        requireActivity().title = requireContext().getString(R.string.createProduct)

        nameTextVoice()
        getBarcodeString()

        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.setDisplayShowHomeEnabled(false)

        savedInstanceState?.let { savedState ->
            binding.editTextNameAddProduct.setText(savedState.getString("textName"))
            binding.editUnitAddProduct.setText(savedState.getString("textUnit"))
            binding.editQuantityAddProduct.setText(savedState.getString("textQuantity"))
            binding.editPriceAddProduct.setText(savedState.getString("textPrice"))
        }

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickAdd()
        onClickAddImage()
        onClickScan()
        onCLickScanFab()
        onBackButton()

        if(imageProduct != null) {
            onPhotoCaptured(imageProduct!!)
            binding.cameraImage.load(productViewModel.capturedPhotoBitmap)
        }

        binding.btnVoice.setOnClickListener {
            promptSpeechInput()
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
            Toast.makeText(context, getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show()
        }
    }


    private fun onCLickScanFab() {
        btnScan = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        btnScan!!.setOnClickListener {
            findNavController().navigate(
                R.id.scanFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.addProductFragment, true).build()
            )
        }
    }

    private fun getBarcodeString() {
        var barcodeNum = this@AddProductFragment.arguments?.getString("barcodeNum")
        if (barcodeNum == "null") {
            binding.editTextBarcodeAddProduct.setText("")
        } else {
            binding.editTextBarcodeAddProduct.setText(barcodeNum)
        }


        binding.cameraImage.setImageResource(R.drawable.photo_camera)
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("barcodeNum3")
            ?.observe(viewLifecycleOwner) { result ->
                if (result == "null") {
                    binding.editTextBarcodeAddProduct.setText("")
                } else {
                    binding.editTextBarcodeAddProduct.setText(result)
                }
            }


}

    private fun onClickScan() {
        binding.btnScanAddProduct.setOnClickListener {
//            findNavController().navigate(R.id.scanFragment)
            findNavController().navigate(
                R.id.scanThreeFragment,
                null
            )


        }
    }

    override fun onResume() {
        super.onResume()

    }
    private fun onClickAdd() {
        val currentAddProduct = ProductDataEntity(
            "productName",
            "productBarcode",
            "productNotes",
            0,
            "",
            0,
            false,
            null,
            1,
            0,
            arrayListOf(),
            0,
            false
        )
        binding.btnAddProductToList.setOnClickListener {
            var productName = binding.editTextNameAddProduct.text.toString()
            var productBarcode = binding.editTextBarcodeAddProduct.text.toString()
            var productPrice = binding.editPriceAddProduct.text.toString()
            var productImage = binding.cameraImage
            var productUnit = binding.editUnitAddProduct.text.toString()
            var productQuantity = binding.editQuantityAddProduct.text.toString()


            if (productName.isNotEmpty() && productPrice.isNotEmpty() && productUnit.isNotEmpty()
                && productQuantity.isNotEmpty()
            ) {
                lifecycleScope.launch {
                    currentAddProduct.nameProduct = productName
                    currentAddProduct.barcodeProduct = productBarcode
                    currentAddProduct.priceProduct = productPrice.toInt()
                    currentAddProduct.totalPrice = productPrice.toInt()
                    currentAddProduct.checkout = false
                    currentAddProduct.unitProduct = productUnit
                    currentAddProduct.quantityProduct = productQuantity.toInt()
                    currentAddProduct.defultCount = 0
                    currentAddProduct.count = 1
                    currentAddProduct.priceHistory = arrayListOf(PriceHistory(currentAddProduct.priceProduct.toString() + " $", getCurrentDate(), R.drawable.edit_fill0_wght400_grad0_opsz48))



                    val drawable = productImage.drawable
                    val imageData = when (drawable) {
                        is BitmapDrawable -> {
                            val bitmap = drawable.bitmap
                            TypeConverterss.fromBitmap(bitmap)
                        }
                        is VectorDrawable -> {
                            val bitmap = Bitmap.createBitmap(
                                drawable.intrinsicWidth,
                                drawable.intrinsicHeight,
                                Bitmap.Config.ARGB_8888
                            )
                            val canvas = Canvas(bitmap)
                            drawable.setBounds(0, 0, canvas.width, canvas.height)
                            drawable.draw(canvas)
                            TypeConverterss.fromBitmap(bitmap)
                        }
                        else -> {

                            val defaultImage = requireContext().resources.getDrawable(
                                R.drawable.photo_camera,
                                null
                            ) as BitmapDrawable
                            val defaultImageByteArray =
                                TypeConverterss.fromBitmap(defaultImage.bitmap)
                            defaultImageByteArray
                        }
                    }
                    currentAddProduct.imageProduct = imageData

                    productViewModel.insert(currentAddProduct)

                }
                findNavController().navigate(
                    R.id.selectProductFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.addProductFragment, true).build()
                )
                Toast.makeText(context, getString(R.string.productCreated), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, getString(R.string.fieldsEmpty), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun nameTextVoice() {
        var voiceText = this@AddProductFragment.arguments?.getString("voiceString")
        var voiceBoolean = this@AddProductFragment.arguments?.getBoolean("voiceBoolean")
        if(voiceBoolean == true){
            binding.editTextNameAddProduct.setText(voiceText)
        }
    }

    private fun onClickAddImage() {
        binding.addImage.setOnClickListener {
            if (checkCameraPermission()) {
                camera()
            } else {
                requestCameraPermission()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.getOrNull(0)
            if (spokenText != null) {
                binding.editTextNameAddProduct.setText(spokenText)
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                cameraRequest -> {
                    imageProduct = data?.extras?.get("data") as Bitmap
                    val bitmapScaled = Bitmap.createScaledBitmap(imageProduct!!, 150, 180, true)
                    binding.cameraImage.load(imageProduct)


                }
            }
        }
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, cameraRequest)
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            cameraRequest
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            cameraRequest -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun onPhotoCaptured(capturedBitmap: Bitmap) {
        productViewModel.capturedPhotoBitmap = capturedBitmap
    }

    private fun onBackButton(){
        callback = object : OnBackPressedCallback(true ) {
            override fun handleOnBackPressed() {

                findNavController().navigate(
                    R.id.homeScreenFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.addProductFragment, true).build()
                )

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the text field values to the bundle
        outState.putString("textName", binding.editTextNameAddProduct.text.toString())
        outState.putString("textUnit", binding.editUnitAddProduct.text.toString())
        outState.putString("textQuantity", binding.editQuantityAddProduct.text.toString())
        outState.putString("textPrice", binding.editPriceAddProduct.text.toString())



        // Save other text field values
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return formatter.format(currentDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        btnScan = null
        callback = null
    }


}
