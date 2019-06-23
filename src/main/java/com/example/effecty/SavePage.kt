package com.example.effecty

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class SavePage : AppCompatActivity() {

    private var image: Bitmap? = null
    private var saveButton: Button? = null
    private var finalImageView: ImageView? = null
    private var currentPhotoPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        image = SecondPage.image
        finalImageView = findViewById(R.id.final_image_view)
        finalImageView!!.setImageBitmap(image)
        saveButton = findViewById(R.id.save_button_save_page)
        saveButton!!.setOnClickListener { save(image!!) }
    }

    @SuppressLint("SimpleDateFormat")
    private fun save(bitmap: Bitmap) {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir("../Final Pictures")!!
        val file = File.createTempFile(
            "FINAL_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply { currentPhotoPath = absolutePath }

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

            Toast.makeText(this, "Final Image Saved! Directory: $currentPhotoPath.", Toast.LENGTH_LONG).show()
            val firstIntent = Intent(this, FirstPage::class.java)
            startActivity(firstIntent)
        } catch (e: IOException) {
            Toast.makeText(this, "A problem occurred.", Toast.LENGTH_SHORT).show()
        }
    }
}