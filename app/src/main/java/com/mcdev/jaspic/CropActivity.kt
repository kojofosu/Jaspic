package com.mcdev.jaspic

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jackandphantom.blurimage.BlurImage
import com.mcdev.jaspic.databinding.ActivityCropBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CropActivity : AppCompatActivity() {
    private val TAG = CropActivity::class.qualifiedName

    private lateinit var binding: ActivityCropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getIntent
        if (intent != null) {
            val action = intent.action
            var data = intent.data

            if (action.equals(Intent.ACTION_SEND) && intent.type != null) {
                /*get Uri*/
                data = intent!!.getParcelableExtra(Intent.EXTRA_STREAM)
            }

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data)
                binding.cropIV.setImageURI(data)
                BlurImage.with(this).load(bitmap).intensity(5F).Async(true).into(binding.blurIV)
            } catch (e: IOException) {
                Log.d(TAG, "onCreate: $e")
            }

        }

        //save image
        binding.saveBtn.setOnClickListener {
            binding.imageConstraintLayout.isDrawingCacheEnabled = true
            val bitmap : Bitmap = binding.imageConstraintLayout.drawingCache
            saveImageToExternalStorage(bitmap)
            Toast.makeText(applicationContext, getString(R.string.image_saved), Toast.LENGTH_LONG).show()
            this.finish()
        }

        //discard changes
        binding.discardIV.setOnClickListener { this.finish() }
    }

    private fun refreshGallery(file : File) {
        MediaStore.Images.Media.insertImage(applicationContext.contentResolver, file.path, null, null) //did the magic. insert image in gallery
        intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        sendBroadcast(intent)
    }

    private fun saveImageToExternalStorage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory()
        val myDir = File(root, "Jaspic")
        if (!myDir.exists()){
            myDir.mkdirs()
        }

        val filename = "Jaspic${System.currentTimeMillis()}.jpg"
        val file = File(myDir, filename)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            refreshGallery(file)    //insert image to gallery and refresh
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}