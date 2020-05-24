package com.panya.bas.scopestorage

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        take.setOnClickListener {
            saveImageToStorage()
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveImageToStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val bitmap = getBitmapFromView(wrapper)
            val bucketName = "MyApp"
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "sample")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + bucketName + File.separator)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val imageUri = application.contentResolver.insert(collection, values)!!

            application.contentResolver.openOutputStream(imageUri).use { out ->
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }

            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            application.contentResolver.update(imageUri, values, null, null)

        } else {
            // use File()
        }
    }
}
