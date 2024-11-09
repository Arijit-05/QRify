package com.royz.testproject

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class ScanQrActivity : AppCompatActivity() {
    private lateinit var scanResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        scanResultTextView = findViewById(R.id.scan_result)
        registerUiListener()
    }

    private fun registerUiListener() {
        scannerLauncher.launch(ScanOptions()
            .setPrompt("Scan Qr Code")
            .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        )
    }

    private val scannerLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result ->
        if (result.contents == null) {
            finish()
        } else {
            scanResultTextView.text = result.contents.toString()
        }
    }
}