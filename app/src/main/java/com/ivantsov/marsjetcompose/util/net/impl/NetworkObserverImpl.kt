package com.ivantsov.marsjetcompose.util.net.impl

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.ivantsov.marsjetcompose.util.net.NetworkObserver
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

//adb shell svc wifi enable|disable (for debugging)
class NetworkObserverImpl
@RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE) @Inject constructor(
    application: Application
) : NetworkObserver {
    private val connectivityManager =
        application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // general availability of Internet over any type
    override var isOnline: NetworkObserver.NetworkState = NetworkObserver.NetworkState.DISABLE
        private set
        get() {
            updateFields()
            return field
        }
    override var isOverWifi: NetworkObserver.NetworkState = NetworkObserver.NetworkState.DISABLE
        private set
        get() {
            updateFields()
            return field
        }

    override var isOverCellular: NetworkObserver.NetworkState = NetworkObserver.NetworkState.DISABLE
        private set
        get() {
            updateFields()
            return field
        }
    override var isOverEthernet: NetworkObserver.NetworkState = NetworkObserver.NetworkState.DISABLE
        private set
        get() {
            updateFields()
            return field
        }

    private fun updateFields() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val networkAvailability =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (networkAvailability != null && networkAvailability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkAvailability.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
            ) {
                //has network
                isOnline = NetworkObserver.NetworkState.ENABLE

                // wifi
                isOverWifi = networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    .convertToNetworkState()

                // cellular
                isOverCellular =
                    networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .convertToNetworkState()

                // ethernet
                isOverEthernet =
                    networkAvailability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        .convertToNetworkState()
            } else {
                isOnline = NetworkObserver.NetworkState.DISABLE
                isOverWifi = NetworkObserver.NetworkState.DISABLE
                isOverCellular = NetworkObserver.NetworkState.DISABLE
                isOverEthernet = NetworkObserver.NetworkState.DISABLE
            }
        } else {

            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isConnected) {
                isOnline = NetworkObserver.NetworkState.ENABLE

                val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                isOverWifi = (wifi != null && wifi.isConnected).convertToNetworkState()

                val cellular = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                isOverCellular = (cellular != null && cellular.isConnected).convertToNetworkState()

                val ethernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)
                isOverEthernet = (ethernet != null && ethernet.isConnected).convertToNetworkState()

            } else {
                isOnline = NetworkObserver.NetworkState.DISABLE
                isOverWifi = NetworkObserver.NetworkState.DISABLE
                isOverCellular = NetworkObserver.NetworkState.DISABLE
                isOverEthernet = NetworkObserver.NetworkState.DISABLE
            }
        }
    }

    override fun watchNetwork(): Flow<NetworkObserver.NetworkState> =
        watchWifi().combine(watchCellular()) { wifi, cellular -> if (wifi == NetworkObserver.NetworkState.ENABLE || cellular == NetworkObserver.NetworkState.ENABLE) NetworkObserver.NetworkState.ENABLE else NetworkObserver.NetworkState.DISABLE }
            .combine(watchEthernet()) { wifiAndCellular, ethernet -> if (wifiAndCellular == NetworkObserver.NetworkState.ENABLE || ethernet == NetworkObserver.NetworkState.ENABLE) NetworkObserver.NetworkState.ENABLE else NetworkObserver.NetworkState.DISABLE }

    override fun watchNetworkAsLiveData(): LiveData<NetworkObserver.NetworkState> =
        watchNetwork().asLiveData()

    override fun watchWifi(): Flow<NetworkObserver.NetworkState> =
        callbackFlowForType(NetworkCapabilities.TRANSPORT_WIFI)

    override fun watchWifiAsLiveData() = watchWifi().asLiveData()

    override fun watchCellular(): Flow<NetworkObserver.NetworkState> =
        callbackFlowForType(NetworkCapabilities.TRANSPORT_CELLULAR)

    override fun watchCellularAsLiveData(): LiveData<NetworkObserver.NetworkState> =
        watchCellular().asLiveData()

    override fun watchEthernet(): Flow<NetworkObserver.NetworkState> =
        callbackFlowForType(NetworkCapabilities.TRANSPORT_ETHERNET)

    override fun watchEthernetAsLiveData() = watchEthernet().asLiveData()

    /**
     * The callback register/unregister methods provided by an external API must be thread-safe,
     * because awaitClose block can be called at any time due to asynchronous nature of cancellation,
     * even concurrently with the call of the callback.
     */
    private fun callbackFlowForType(type: Int): Flow<NetworkObserver.NetworkState> = callbackFlow {
        val channel = this

        channel.trySend(NetworkObserver.NetworkState.DISABLE).onClosed {
            throw it ?: ClosedSendChannelException("Channel was closed normally")
        }.isSuccess.also {
            if (it.not()) Log.e(TAG, "callbackFlowForType | channel.trySend result: $it")
        }

        val networkRequest = NetworkRequest.Builder().addTransportType(type).build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                channel.trySend(NetworkObserver.NetworkState.DISABLE).onClosed {
                    throw it ?: ClosedSendChannelException("Channel was closed normally")
                }.isSuccess.also {
                    if (it.not()) Log.e(
                        TAG, "onLost | channel.trySend result: $it"
                    )
                }
            }

            override fun onUnavailable() {
                channel.trySend(NetworkObserver.NetworkState.DISABLE).onClosed {
                    throw it ?: ClosedSendChannelException("Channel was closed normally")
                }.isSuccess.also {
                    if (it.not()) Log.e(
                        TAG, "onUnavailable | channel.trySend result: $it"
                    )
                }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                // do nothing
            }

            override fun onAvailable(network: Network) {
                channel.trySend(NetworkObserver.NetworkState.ENABLE).onClosed {
                    throw it ?: ClosedSendChannelException("Channel was closed normally")
                }.isSuccess.also {
                    if (it.not()) Log.e(
                        TAG, "onAvailable | channel.trySend result: $it"
                    )
                }
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, callback)

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }

    companion object {
        private const val TAG = "NetworkObserver"
        private fun Boolean.convertToNetworkState(): NetworkObserver.NetworkState =
            if (this) NetworkObserver.NetworkState.ENABLE else NetworkObserver.NetworkState.DISABLE
    }
}
