package com.example.tutorapp

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import me.dm7.barcodescanner.zxing.ZXingScannerView
import android.widget.Toast
import android.os.Bundle
import android.util.Log
import android.content.DialogInterface
import android.os.Build
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.content.Intent
import com.google.zxing.Result
import com.shashank.sony.fancytoastlib.FancyToast


class BarcodeCaptureActivity: AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var docID: String
    private val REQUEST_CAMERA = 1
    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate", "onCreate")

        mScannerView = ZXingScannerView(this)
        setContentView(mScannerView)
        val currentapiVersion = android.os.Build.VERSION.SDK_INT
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(applicationContext, "Permission already granted", Toast.LENGTH_SHORT).show()
            } else {
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            REQUEST_CAMERA -> if (grantResults.isNotEmpty()) {

                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted) {
                    FancyToast.makeText(applicationContext, "Permission Granted, Now you can access camera", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show()
                } else {
                    FancyToast.makeText(applicationContext, "Permission Denied, You cannot access and camera", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(arrayOf(CAMERA),
                                                REQUEST_CAMERA)
                                    }
                                })
                            return
                        }
                    }
                }
            }
            else ->
                Log.d("AAAA", "004")
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {

        Log.d("AAAA", "005")
        android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    public override fun onResume() {
        super.onResume()
        docID = intent.getStringExtra("docID")

        val currentapiVersion = android.os.Build.VERSION.SDK_INT
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {

            Log.d("AAAA", "006")
            if (checkPermission()) {
                if (mScannerView == null) {
                    mScannerView = ZXingScannerView(this)
                    setContentView(mScannerView)
                }
                mScannerView!!.setResultHandler(this)
                mScannerView!!.startCamera()
            } else {

                Log.d("AAAA", "007")
                requestPermission()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        mScannerView!!.stopCamera()
    }

    override fun handleResult(rawResult: Result) {

        val result = rawResult.text

        Log.d("QRCodeScanner", rawResult.text)
        Log.d("QRCodeScanner", rawResult.barcodeFormat.toString())

        val intent = Intent()
        intent.putExtra("result", result)
        intent.putExtra("docID", docID)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }
}