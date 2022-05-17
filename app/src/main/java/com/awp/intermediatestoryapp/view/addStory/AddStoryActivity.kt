package com.awp.intermediatestoryapp.view.addStory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.databinding.ActivityAddStoryBinding
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.preference.ViewModelFactory
import com.awp.intermediatestoryapp.view.home.HomeActivity
import com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel.addStoryVM
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryVM: addStoryVM
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var getFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = getString(R.string.uploadStories)
        addStoryVM = ViewModelProvider(
            this,
            ViewModelFactory(SessionPreference.getInstance(dataStore))
        )[com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel.addStoryVM::class.java]

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.takepicture.setOnClickListener {
            takePicture()
        }
        binding.buttontofile.setOnClickListener {
            startGallery()
        }
        binding.upload.setOnClickListener {
            upload()
        }
    }

    fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.bangkit.intermediate",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)

        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.imagePreview.setImageURI(selectedImg)
        }
    }


    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            getFile = myFile

            val result = BitmapFactory.decodeFile(getFile?.path)
            binding.imagePreview.setImageBitmap(result)
        }
    }

    fun getUploadResponse(token:String, file: MultipartBody.Part, desrcription: RequestBody){
        addStoryVM.uploadStory(token,file,desrcription)
        addStoryVM.uploadResponse.observe(this){

            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//            val i =Intent(this,HomeActivity::class.java)
//            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(i)
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    fun upload(){
        addStoryVM.getUser().observe(this){
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)

                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                getUploadResponse(
                    it.token,
                    imageMultipart,
                    binding.editTextTextMultiLine.text.toString().toRequestBody("text/plain".toMediaType())
                )
            }else{
                Toast.makeText(this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }





}