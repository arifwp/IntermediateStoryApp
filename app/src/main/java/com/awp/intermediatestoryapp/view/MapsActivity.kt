package com.awp.intermediatestoryapp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.awp.intermediatestoryapp.R
import com.awp.intermediatestoryapp.databinding.ActivityMapsBinding
import com.awp.intermediatestoryapp.preference.SessionPreference
import com.awp.intermediatestoryapp.model.ViewModelFactory
import com.awp.intermediatestoryapp.viewmodel.HomeViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mobileMap: GoogleMap
    private lateinit var homeViewModel : HomeViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(SessionPreference.getInstance(dataStore))
        )[HomeViewModel::class.java]

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mobileMap = googleMap
        mobileMap.uiSettings.isZoomControlsEnabled = true
        mobileMap.uiSettings.isIndoorLevelPickerEnabled = true
        mobileMap.uiSettings.isCompassEnabled = true
        mobileMap.uiSettings.isMapToolbarEnabled = true

        viewModelUser()
        homeList()
    }

    private fun homeList() {
        homeViewModel.list.observe(this){
            if (it!= null) {
                it.listStory?.forEach { data ->
                    if (data != null) {
                        if (data.lat != null || data.lon != null){
                            mobileMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(data.lat as Double, data.lon as Double ))
                                    .title("user : ${data.name}")
                            )
                        }
                    }
                }
            }
        }
    }

    private fun viewModelUser() {
        var token =""
        homeViewModel.getUser().observe(
            this
        ){
            token = it.token
            dataStoriesResponse(token)
        }
    }

    fun dataStoriesResponse(token:String){
        homeViewModel.getStoryLocation(token)
    }
}