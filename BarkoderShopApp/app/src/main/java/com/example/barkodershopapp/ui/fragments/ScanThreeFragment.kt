package com.example.barkodershopapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.barkoder.Barkoder
import com.barkoder.BarkoderConfig
import com.barkoder.interfaces.BarkoderResultCallback
import com.barkoder.shoppingApp.net.R
import com.barkoder.shoppingApp.net.databinding.FragmentScanThreeBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanThreeFragment : Fragment(), BarkoderResultCallback {
    private var _binding: FragmentScanThreeBinding? = null
    private val binding get() = _binding!!
    private val CAMERA_PERMISSION_REQUEST_CODE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScanThreeBinding.inflate(inflater, container, false)
        binding.bkdView3.config = BarkoderConfig(
            context,
            "PEmBIohr9EZXgCkySoetbwP4gvOfMcGzgxKPL2X6uqOVL7EGJ4cmhf5j5WSDVkDVoOEcowlOGyJMbrM3DMQmyFpfmE8J8Fc-I7lITXErfWK52VDxXCvYPCPgLX2sIAc9Du6ble1kIKmg8JAXwcrGuJeKGgpyo_FOcXuraHs9NUmkXc-5TqlOTC93SznoT6wcec4NEAV9IhhRCse0L8NcFxSwRmvB3TIkLHjmvIsQt2LlzgSwU7gy9Hpyj9KoPYqdmeNwTdG89ShKpQ7fbXvFokUcjjSQwdfL09AD_-TPb-V5BU_Jd0oQmYxREIC0FZyk"
        )
        {
            Log.i("LicenseInfo", it.message)
        }
        var toolBar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBarrr)
        toolBar.visibility = View.GONE

        if (checkCameraPermission()) {
            startScanning()
            navInvisible()
        } else {
            requestCameraPermission()
            findNavController().popBackStack()
        }


        setActiveBarcodeTypes()
        setBarkoderSettings()
        navInvisible()
        binding.btnClose3.setOnClickListener {
            findNavController().popBackStack()
        }
        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    private fun navInvisible() {
        var bottomNav = requireActivity().findViewById<BottomAppBar>(R.id.bottomNavigationApp)
        bottomNav.visibility = View.GONE
        var bottomFab = requireActivity().findViewById<FloatingActionButton>(R.id.fabNav)
        bottomFab.visibility = View.GONE
    }

    private fun setActiveBarcodeTypes() {
        // There is option to set multiple active barcodes at once as array
        binding.bkdView3.config.decoderConfig.SetEnabledDecoders(
            arrayOf(
                Barkoder.DecoderType.QR,
                Barkoder.DecoderType.Ean13
            )
        )
        // or configure them one by one
        binding.bkdView3.config.decoderConfig.UpcA.enabled = true
    }

    private fun setBarkoderSettings() {
        // These are optional settings, otherwise default values will be used
        binding.bkdView3.config.let { config ->
            config.isImageResultEnabled = true
            config.isLocationInImageResultEnabled = true
            config.isRegionOfInterestVisible = true
            config.isPinchToZoomEnabled = true
            config.setRegionOfInterest(5f, 25f, 90f, 50f)
        }
    }

    private fun updateUI(result: Barkoder.Result? = null, resultImage: Bitmap? = null) {
        var barcodeNum3 = result?.textualData

        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "barcodeNum3",
            barcodeNum3
        )
        findNavController().popBackStack()
    }

    override fun scanningFinished(
        results: Array<out Barkoder.Result>?,
        p1: Array<out Bitmap>?,
        resultImage: Bitmap?
    ) {
        if (results!!.isNotEmpty())
            updateUI(results[0], resultImage)
        else
            updateUI()
    }

    private fun startScanning() {

        binding.bkdView3.startScanning(this)

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
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning()
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
        var toolBar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBarrr)
        toolBar.visibility = View.VISIBLE
    }
}