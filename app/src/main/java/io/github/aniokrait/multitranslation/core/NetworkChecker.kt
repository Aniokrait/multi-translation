package io.github.aniokrait.multitranslation.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Check user's device network info.
 */
class NetworkChecker {
    companion object {
        /**
         * Check if device is connected to WiFi network.
         * @param context Application context
         * @return True if connected to Wi-Fi.
         */
        fun isWifiConnected(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities != null &&
                networkCapabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI,
                )
        }
    }
}
