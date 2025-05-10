package com.example.mobile6.ui.prescriptions

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobile6.domain.repository.PrescriptionRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrescriptionQrViewModel @Inject constructor(
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {

    private val _prescriptionId = MutableLiveData<Long>()
    val prescriptionId: LiveData<Long> = _prescriptionId

    private val _qrCode = MutableLiveData<Bitmap>()
    val qrCode: LiveData<Bitmap> = _qrCode

    fun setPrescriptionId(id: Long) {
        _prescriptionId.value = id
        _qrCode.value = generateQrCodeBitmap(String.format("%09d", id))
    }

    fun generateQrCodeBitmap(content: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val hints = HashMap<EncodeHintType, Any>()
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        hints[EncodeHintType.MARGIN] = 2

        val bitMatrix = qrCodeWriter.encode(
            content,
            BarcodeFormat.QR_CODE,
            512,
            512,
            hints
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a custom color for the QR code (light blue)
        val qrColor = Color.parseColor("#7B8CFF")

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) qrColor else Color.WHITE)
            }
        }

        return bitmap
    }
}