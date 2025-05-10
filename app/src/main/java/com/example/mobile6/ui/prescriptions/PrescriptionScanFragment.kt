package com.example.mobile6.ui.prescriptions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mobile6.R
import com.example.mobile6.data.remote.dto.response.PrescriptionResponse
import com.example.mobile6.databinding.FragmentPrescriptionScanBinding
import com.example.mobile6.ui.MainActivity
import com.example.mobile6.ui.base.BaseFragment
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.IOException

@AndroidEntryPoint
class PrescriptionScanFragment : BaseFragment<FragmentPrescriptionScanBinding>() {
    private val viewModel: PrescriptionScanViewModel by viewModels()

    private var cameraSource: CameraSource? = null
    private var barcodeDetector: BarcodeDetector? = null
    private var isDetectorOperational = false

    override val bindingInflater: (LayoutInflater, ViewGroup?) -> FragmentPrescriptionScanBinding
        get() = { inflater, container ->
            FragmentPrescriptionScanBinding.inflate(inflater, container, false)
        }

    override fun initViews() {
        setupBackButton()
        setupCompleteButton()
        setupPrescriptionIdField()
        setupBarcodeDetector()
    }

    private fun setupBackButton() {
        binding.ivBack.setOnClickListener {
            back()
        }
    }

    private fun setupCompleteButton() {
        binding.btnComplete.setOnClickListener {
            val prescriptionId = binding.etPrescriptionId.text.toString()
            if (prescriptionId.isNotEmpty()) {
                viewModel.fetchPrescription(prescriptionId.toLong())
            } else {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng nhập mã đơn thuốc",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupPrescriptionIdField() {
        binding.etPrescriptionId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                binding.btnComplete.isEnabled = !s.isNullOrEmpty()
            }
        })
    }

    private fun setupBarcodeDetector() {
        barcodeDetector = BarcodeDetector.Builder(context)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        if (!barcodeDetector!!.isOperational) {
            Toast.makeText(
                requireContext(),
                "Không thể khởi tạo trình quét mã QR",
                Toast.LENGTH_SHORT
            ).show()
            isDetectorOperational = false
            return
        }

        isDetectorOperational = true
        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        startCameraSource()
        setupBarcodeProcessor()
    }

    private fun setupBarcodeProcessor() {
        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                // Release resources
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    val barcode = barcodes.valueAt(0)
                    if (barcode.format == Barcode.QR_CODE) {
                        val prescriptionId = barcode.displayValue
                        requireActivity().runOnUiThread {
                            binding.etPrescriptionId.setText(prescriptionId)
                            // Automatically fetch prescription after scanning
                            viewModel.fetchPrescription(prescriptionId.toString().toLong())
                        }
                    }
                }
            }
        })
    }

    private fun startCameraSource() {
        if (isDetectorOperational) {
            try {
                binding.cameraPreviewContainer.removeAllViews()
                val surfaceView = SurfaceView(requireContext())
                binding.cameraPreviewContainer.addView(surfaceView)

                surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                    @RequiresPermission(Manifest.permission.CAMERA)
                    override fun surfaceCreated(holder: SurfaceHolder) {
                        try {
                            if (ActivityCompat.checkSelfPermission(
                                    requireContext(),
                                    Manifest.permission.CAMERA
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                requestCameraPermission()
                                return
                            }
                            cameraSource?.start(holder)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }

                    override fun surfaceChanged(
                        holder: SurfaceHolder,
                        format: Int,
                        width: Int,
                        height: Int
                    ) {
                    }

                    override fun surfaceDestroyed(holder: SurfaceHolder) {
                        cameraSource?.stop()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraSource()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Cần quyền truy cập camera để quét mã QR",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()
        cameraSource?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        barcodeDetector?.release()
    }

    override fun initObservers() {
        // Observe UI messages
        viewModel.uiMessage.onEach { message ->
            (requireActivity() as MainActivity).showToast(message)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .launchIn(viewLifecycleOwner.lifecycleScope)


        viewModel.prescription.observe(viewLifecycleOwner) { result ->
            navigateToPrescriptionDetails(result)
        }
    }

    private fun navigateToPrescriptionDetails(prescription: PrescriptionResponse) {
        val bundle = Bundle().apply {
            putLong("prescriptionId", prescription.prescriptionId!!)
        }
        navigateTo(R.id.action_prescriptionScanFragment_to_prescriptionDetailScanFragment, bundle)
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}