package com.awp.intermediatestoryapp.view

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
import com.awp.intermediatestoryapp.model.ViewModelFactory
import com.awp.intermediatestoryapp.view.story.customizeFile
import com.awp.intermediatestoryapp.view.story.fileTo
import com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel.addStoryViewModel
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
    private lateinit var addStoryViewModel: addStoryViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var dataFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.upload_page)

        instanceModel()

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionIssues()


        buttonActivity()
    }

    private fun buttonActivity() {
        binding.btnCamera.setOnClickListener {
            runningCamera()
        }
        binding.btnGallery.setOnClickListener {
            runningGallery()
        }
        binding.btnUpload.setOnClickListener {
            doUploadPicture()
        }
    }

    private fun permissionIssues() {
        if (!askingPermission()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    fun runningCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        customizeFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.awp.intermediatestoryapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)

        }
    }


    private fun runningGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val theChoosenOne = Intent.createChooser(intent, "Choose a Picture")
        doLaunchGallery.launch(theChoosenOne)
    }
    private val doLaunchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = fileTo(selectedImg, this)

            dataFile = myFile

            binding.imageIcon.setImageURI(selectedImg)
        }
    }

    private fun instanceModel() {
        addStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionPreference.getInstance(dataStore))
        )[com.awp.intermediatestoryapp.viewmodel.AddStoryViewModel.addStoryViewModel::class.java]
    }


    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            dataFile = myFile

            val result = BitmapFactory.decodeFile(dataFile?.path)
            binding.imageIcon.setImageBitmap(result)
        }
    }


    fun askingUpload(token:String, file: MultipartBody.Part, desrcription: RequestBody){
        addStoryViewModel.pictureUploaded(token,file,desrcription)
        addStoryViewModel.uploadResponse.observe(this){

            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            finish()
            moveActivity()
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun askingPermission() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun sizeReduce(file: File): File {
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

    fun doUploadPicture(){
        addStoryViewModel.userData().observe(this){
            if (dataFile != null) {
                val fileReduced = sizeReduce(dataFile as File)

                val gettingImageRequest = fileReduced.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    fileReduced.name,
                    gettingImageRequest
                )
                askingUpload(
                    it.token,
                    imageMultipart,
                    binding.descStory.text.toString().toRequestBody("text/plain".toMediaType())
                )
            }else{
                Toast.makeText(this, "Please input your picture first", Toast.LENGTH_SHORT).show()
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

            ifGranted()
        }
    }

    private fun ifGranted() {
        if (!askingPermission()) {
            Toast.makeText(
                this,
                "User does not allow permission",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }





}