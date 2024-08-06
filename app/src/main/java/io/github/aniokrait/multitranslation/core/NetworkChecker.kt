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
            val networkCapabilities = getNetworkCapabilities(context = context)
            return networkCapabilities != null &&
                    networkCapabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI,
                    )
        }

        /**
         * Check if device is connected to the internet.
         * @param context Application context
         * @return True if connected to the internet.
         */
        fun isNetworkConnected(context: Context): Boolean {
            return getNetworkCapabilities(context = context) != null
        }

        private fun getNetworkCapabilities(context: Context): NetworkCapabilities? {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            return connectivityManager.getNetworkCapabilities(network)
        }
    }
}
