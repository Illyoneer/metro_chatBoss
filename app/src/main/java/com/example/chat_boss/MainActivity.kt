package com.example.chat_boss

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.chat_boss.databinding.ActivityMainBinding
import com.example.chat_boss.service.ChatService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var chatService: ChatService

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChatbot.setOnClickListener {
            chatService.runChatActivity(this)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(providePermissionListener())
            .check()
    }

    private fun providePermissionListener(): PermissionListener {
        return object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        response?.requestedPermission?.name.toString()
                    ) == PackageManager.PERMISSION_GRANTED
                )
                    fusedLocationClient.lastLocation.addOnSuccessListener {
                        it?.let {
                            val latitude = it.latitude
                            val longitude = it.longitude

                            chatService.setGeoPosition(latitude, longitude)
                        }
                    }
            }

            override fun onPermissionDenied(denied: PermissionDeniedResponse?) {}

            override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {}
        }
    }
}