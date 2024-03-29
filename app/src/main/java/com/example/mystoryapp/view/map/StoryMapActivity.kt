package com.example.mystoryapp.view.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.databinding.ActivityStoryMapBinding
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.model.StoryListResponse
import com.example.mystoryapp.sharedpreferences.SharedPreferencesManager
import com.example.mystoryapp.view.storydetail.StoryDetailActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoryMapBinding
    private lateinit var sharedPreferenceManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        sharedPreferenceManager = SharedPreferencesManager(this)
        val token = sharedPreferenceManager.getToken()

        Log.d("TAG", "Token $token")

        if (token != null) {
            getAllStoryPosition(token)
        }

    }

    private fun getAllStoryPosition(token: String) {
        ApiConfig.getApiService().getAllStoriesForLocation("Bearer $token",1 ,100, 1)
            .enqueue(object : Callback<StoryListResponse> {
                override fun onResponse(
                    call: Call<StoryListResponse>,
                    response: Response<StoryListResponse>
                ) {
                    if (response.isSuccessful) {
                        val storyList = response.body()?.listStory
                        Log.d("TAG", "Map Result : $storyList")


                        storyList?.forEach {
                            val latLng = LatLng(it.lat as Double, it.lon as Double)
                            val markerOptions = MarkerOptions()
                                .position(latLng)
                                .title(it.name)

                            val marker = mMap.addMarker(markerOptions)
                            marker?.tag = it
                            boundsBuilder.include(latLng)
                        }
                        mMap.setOnMarkerClickListener {

                            val data = it.tag as? ListStoryItem
                            if (data != null) {
                                val intent = Intent(this@StoryMapActivity, StoryDetailActivity::class.java)
                                intent.putExtra(StoryDetailActivity.EXTRA_USERNAME, data.name)
                                intent.putExtra(StoryDetailActivity.EXTRA_IMG_URL, data.photoUrl)
                                intent.putExtra(StoryDetailActivity.EXTRA_DESC, data.description)
                                intent.putExtra(StoryDetailActivity.EXTRA_DATE, data.createdAt)
                                intent.putExtra(StoryDetailActivity.EXTRA_LAT, data.lat.toString())
                                intent.putExtra(StoryDetailActivity.EXTRA_LON, data.lon.toString())
                                startActivity(intent)

                            }
                            true
                        }


                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                300
                            )
                        )

                    } else {
                        Log.d("TAG", "Error 1 ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoryListResponse>, t: Throwable) {
                    Log.d("TAG", "Error 2 : ${t.message.toString()}")
                }

            })
    }


    private val boundsBuilder = LatLngBounds.Builder()


}