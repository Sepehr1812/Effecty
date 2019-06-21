package com.example.effecty

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class FirstPage : AppCompatActivity() {

    private var btn: Button? = null
    private var imageView: ImageView? = null
    private val gallery = 2
    private val camera = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        btn = findViewById<View>(R.id.button_take_photo) as Button
        imageView = findViewById<View>(R.id.imageView) as ImageView
        btn!!.setOnClickListener { showPictureDialog() }
    }

    /** Actions for Give me a photo button */
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select from Gallery", "Capture a Photo")
        pictureDialog.setItems(pictureDialogItems) { _, which ->
            when (which) {
                0 -> selectImageInAlbum()
                1 -> takePhoto()
            }
        }
        pictureDialog.show()
    }

    /** Actions for select image from gallery button */
    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) startActivityForResult(intent, gallery)
    }

    /** Actions for take image button */
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Toast.makeText(this, "A problem occurred.", Toast.LENGTH_SHORT).show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider", it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, camera)
                }
            }
        }
    }


    /** To take a photo from the user */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED)
            return

        if (requestCode == gallery) {
            if (data != null) {
                val contentURI = data.data
                try {
                    //To show the image gotten
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageView!!.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == camera) {
            /* An error occurred here and I (Sepehr) don't know why!
               Error: java.lang.RuntimeException: Failure delivering result ResultInfo{who=null, request=1, result=-1, data=null}
                to activity {com.example.effecty/com.example.effecty.MainActivity}: kotlin.KotlinNullPointerException
             */
//            val thumbnail = data!!.extras!!.get("data") as Bitmap
//            imageView!!.setImageBitmap(thumbnail)

            //To show the image captured
            val imgFile = File(currentPhotoPath)

            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                imageView!!.setImageBitmap(myBitmap)

                Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private var currentPhotoPath: String = ""

    /** To create initial image file */
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }
}