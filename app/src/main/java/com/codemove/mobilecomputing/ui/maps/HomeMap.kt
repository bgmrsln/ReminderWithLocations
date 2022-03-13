package com.codemove.mobilecomputing.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.codemove.mobilecomputing.util.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import java.util.*
import kotlinx.coroutines.launch

@Composable
fun HomeMap(
    navController: NavController
) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val currentLatlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("virtual_location_data")
        ?.value

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)
        .padding(bottom = 36.dp)
    ) {
        AndroidView({mapView}) { mapView ->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                var location = LatLng(65.06, 25.47)
                if(currentLatlng!= null){
                    location= LatLng(currentLatlng.latitude, currentLatlng.longitude)
                }

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 10f)
                )

                val markerOptions = MarkerOptions()
                    .title("Welcome to Oulu")
                    .position(location)
                map.addMarker(markerOptions)

                setMapLongClick(map = map, navController = navController)
            }
        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController
) {
    map.setOnMapLongClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )

        map.addMarker(
            MarkerOptions().position(latlng).title("Virtual location").snippet(snippet)
        ).apply {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("virtual_location_data", latlng)
        }
    }
}