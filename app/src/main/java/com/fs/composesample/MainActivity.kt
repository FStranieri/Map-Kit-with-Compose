package com.fs.composesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.CameraPosition
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private var testMarker: Marker? = null
    private val testMarkerOptions: MarkerOptions = MarkerOptions()
        .position(LatLng(45.4781878, 9.1684945))
        .title("Hello Compose!")
        .snippet("MapView with Compose!")

    private val huaweiMapOptions = HuaweiMapOptions().apply {
        camera(CameraPosition.builder()
            .target(LatLng(45.4781878, 9.1684945))
            .zoom(10f)
            .build())
    }

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mapViewBundle = savedInstanceState?.get(MAPVIEW_BUNDLE_KEY) as? Bundle

        mapView = MapView(this, huaweiMapOptions)
        mapView.onCreate(mapViewBundle)

        setContent {
            BuildMap(this)
        }
    }

    @Composable
    private fun BuildMap(onMapReadyCallback: OnMapReadyCallback){
        AndroidView(
            modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
            factory = {
                mapView
            },
            update = {
                mapView.getMapAsync(onMapReadyCallback)
            }
        )
    }

    @Composable
    private fun BuildMap(onMapReadyCallback: OnMapReadyCallback){
        // Adds view to Compose
        AndroidView(
            modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
            factory = { context ->
                layoutInflater.inflate(R.layout.map_fragment, null, false)
            },
            update = { view ->
                (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).apply {
                    getMapAsync(onMapReadyCallback)
                }
            }
        )
    }

    override fun onMapReady(map: HuaweiMap?) {
        testMarker?.remove()
        testMarker = map?.addMarker(testMarkerOptions)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mapView?.onSaveInstanceState(mapViewBundle)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    companion object {
        const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }
}