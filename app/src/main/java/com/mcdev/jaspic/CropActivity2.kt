package com.mcdev.jaspic

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jackandphantom.blurimage.BlurImage
import kotlinx.android.synthetic.main.activity_crop.*
import java.io.IOException

class CropActivity2 : AppCompatActivity() {
    private val TAG = CropActivity2::class.qualifiedName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        //getIntent
        if (intent != null) {
            var action = intent.action
            val data = intent.data

            try {
                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, data)
                cropIV.setImageURI(data)
                BlurImage.with(this).load(bitmap).intensity(5F).Async(true).into(blurIV)
            } catch (e: IOException) {
                Log.d(TAG, "onCreate: $e")
            }

        }

        //save image
        saveBtn.setOnClickListener {
            imageConstraintLayout.isDrawingCacheEnabled = true
            val bitmap : Bitmap = imageConstraintLayout.drawingCache
            MediaStore.Images.Media.insertImage(applicationContext.contentResolver, bitmap, null, null)
            Toast.makeText(applicationContext, "Image saved!", Toast.LENGTH_LONG).show()
            this.finish()
        }

        //discard changes
        discardIV.setOnClickListener { this.finish() }
    }
}