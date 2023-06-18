package com.example.mystoryapp.view.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.databinding.ActivityCameraMainBinding
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.storylist.StoryListActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*

class CameraMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraMainBinding
    private lateinit var sharedPreferenceManager: SharedPreferencesManager
    private lateinit var viewModel: CameraMainViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loc: Location

    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getLocation()
        sharedPreferenceManager = SharedPreferencesManager(this)
        viewModel = ViewModelProvider(this)[CameraMainViewModel::class.java]

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.btnCamerax.setOnClickListener { startCameraX() }
        binding.buttonAdd.setOnClickListener {
            showLoading(true)

            uploadStory()
        }

        binding.btnGalery.setOnClickListener { startGallery() }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
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
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@CameraMainActivity)
                getFile = myFile
                binding.ivImagePreview.setImageURI(uri)
            }
        }
    }


    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("picture", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("picture")
                } as? File

                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

                myFile?.let { file ->
                    rotateFile(file, isBackCamera)
                    getFile = file
                    binding.ivImagePreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }


    //Upload Story Function
    private fun uploadStory() {

        if (getFile != null) {

            val file = reduceFileImage(getFile as File)
            val desc = binding.edAddDescription.text.toString()
            val result = desc.ifEmpty {
                "No Desc"
            }
            val description = result.toRequestBody("text/plain".toMediaType())
            val lat = loc.latitude.toString().toRequestBody("text/plain".toMediaType())
            val lon = loc.longitude.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )



            Log.d("TAG", "lat : $lat, lng : $lon")
            viewModel.uploadStory(
                sharedPreferenceManager.getToken().toString(),
                description,
                imageMultipart,
                lat,
                lon
            )

            viewModel.addStoryResponse.observe(this) {
                if (!it.error) {
                    showLoading(false)
                    startActivity(
                        Intent(
                            this@CameraMainActivity,
                            StoryListActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    finish()
                } else {
                    showLoading(false)
                    Toast.makeText(this, "Upload gagal", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            showLoading(false)
            Toast.makeText(
                this@CameraMainActivity,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.pbProgressBar.visibility = View.VISIBLE
        } else {
            binding.pbProgressBar.visibility = View.GONE
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {

                    getLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {

                    getLocation()
                }

                else -> {

                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun getLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient.lastLocation.addOnSuccessListener {
                loc = it

            }.addOnFailureListener {
                Log.d("TAG", "${it.message}")
            }


        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


}