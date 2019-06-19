package com.android.mahindra.ui.common

/**
 * @author Kaiser Perwez
 */
 
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast


class CurrentLocationUtils(
    private val activity: Activity,
    private val permissionDeniedText: String = "This app needs location permission to work.",
    val removeLocationUpdates: Boolean = true
) :
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    lateinit var listener: CurrentLocationChangedListener

    private val LOCATION_SETTINGS_REQUEST: Int = 1

    fun initActivityListener(activity: Activity) {
        this.listener = activity as CurrentLocationChangedListener
    }

    fun initFragmentListener(fragment: androidx.fragment.app.Fragment) {
        this.listener = fragment as CurrentLocationChangedListener
    }


    fun getCurrentLocation(delay: Long) {
        // Check if permissions are enabled and if not request
        Handler().postDelayed({
            buildGoogleApiClient()
        }, delay)
    }

    fun removeLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
        }
    }

    /*GoogleApiClient.ConnectionCallbacks*/
    override fun onConnected(p0: Bundle?) {
        val mLocationRequest = createLocationRequest()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)

        // **************************
        builder.setAlwaysShow(true) // this is the key ingredient
        // **************************

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    LocationServices.FusedLocationApi?.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
                } catch (ex: Exception) {
                    activity.toast("Error: ${ex.message}")
                }
            } else {
                requestPermission(100)
            }
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the all_ic_user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, LOCATION_SETTINGS_REQUEST)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun createLocationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
//        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return mLocationRequest
    }

    override fun onConnectionSuspended(p0: Int) {
        activity.toast("Error: Google API connection suspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        activity.toast("Error: Google API connection failed")
    }

    /*LocationListener*/
    override fun onLocationChanged(location: Location?) {
        location?.let {
            listener.locationHasChanged(location)

            if (removeLocationUpdates)
                removeLocationUpdates()
        }
    }

    fun requestPermission(delay: Long) {
        Dexter.withActivity(activity)
            .withPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : PermissionListener {
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    getCurrentLocation(delay)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    showAppCloseDialog()
                }
            }).withErrorListener { activity.toast("Error occurred while asking for location permission!") }
            .onSameThread()
            .check()
    }

    private fun showAppCloseDialog() {
        activity.alert(permissionDeniedText, "Need Permissions") {
            positiveButton("Allow") {
                it.dismiss()
                requestPermission(1000)
            }
            negativeButton("Ignore") {
                it.dismiss()
                listener.locationPermissionDenied()
            }
        }.show()
    }

    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(activity)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        mGoogleApiClient?.connect()
    }

    // navigating all_ic_user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.setData(uri)
        startActivityForResult(activity, intent, 101, null)
    }
}

interface CurrentLocationChangedListener {
    fun locationHasChanged(location: Location)
    fun locationPermissionDenied()
}
