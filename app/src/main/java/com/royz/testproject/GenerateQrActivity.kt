package com.royz.testproject

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GenerateQrActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var button: Button
    private lateinit var imageView: ImageView
    private lateinit var shareBtn: Button
    private var qrbitmap: Bitmap ?= null
    private val TAG = "GenerateQrActivity"
    private val QR_SIZE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_generate_qr)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText = findViewById(R.id.editText)
        button = findViewById(R.id.button)
        imageView = findViewById(R.id.imageView)
        shareBtn = findViewById(R.id.share_btn)

        button.setOnClickListener {
            generateQrCode()
        }

        shareBtn.setOnClickListener {
            shareQrCode()
        }
    }

    private fun generateQrCode() {
        val inputText = editText.text.toString()
        try {
            val encoder = BarcodeEncoder()
            qrbitmap = encoder.encodeBitmap(inputText, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE)
            imageView.setImageBitmap(qrbitmap)
            shareBtn.visibility = View.VISIBLE
        } catch (e: WriterException) {
            Log.e(TAG, "generateQrCode: ${e.message}")
        }
    }

    private fun shareQrCode() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, getUriFromBitmap())
            putExtra(Intent.EXTRA_TEXT, "Scan this code")
            type = "image/*"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun getUriFromBitmap(): Uri? {
        try {
            val file = File(
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "qr_code${System.currentTimeMillis()}.png"
            )
            val outStream = FileOutputStream(file)
            qrbitmap?.compress(Bitmap.CompressFormat.PNG, 90, outStream)
            return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
        } catch (e: IOException) {
            Log.d(TAG, "Message: $e")
        }
        return null
    }
}