package com.ivantsov.marsjetcompose.util.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow

/**
 * A NetworkObserver interface that provides methods to observe network connectivity and its state.
 * This interface defines properties to determine the type of network connectivity, and methods to
 * observe the state of different types of networks such as WiFi, Cellular, and Ethernet.
 * The interface also defines two methods to get live data for observing network state and network
 * connectivity changes.
 */
interface NetworkObserver {
    /**
     * A property that returns the current state of network connectivity.
     * The state can be either ENABLE or DISABLE represented by NetworkState objects.
     */
    val isOnline: NetworkState

    /**
     *A property that returns the current state of WiFi connectivity.
     *The state can be either ENABLE or DISABLE represented by NetworkState objects.
     */
    val isOverWifi: NetworkState

    /**
     *A property that returns the current state of Cellular connectivity.
     *The state can be either ENABLE or DISABLE represented by NetworkState objects.
     */
    val isOverCellular: NetworkState

    /**
     *A property that returns the current state of Ethernet connectivity.
     *The state can be either ENABLE or DISABLE represented by NetworkState objects.
     */
    val isOverEthernet: NetworkState

    /**
     *A method that returns a flow for observing changes in network state.
     *This method returns a Flow of NetworkState objects that emit the current state of network
     *connectivity when there is a change in the connectivity state.
     */

    fun watchNetwork(): Flow<NetworkState>

    /**
     *A method that returns LiveData for observing changes in network state.
     *This method returns a LiveData object that emits the current state of network connectivity
     *when there is a change in the connectivity state.
     */
    fun watchNetworkAsLiveData(): LiveData<NetworkState>

    /**
     *A method that returns a flow for observing changes in WiFi state.
     *This method returns a Flow of NetworkState objects that emit the current state of WiFi
     *connectivity when there is a change in the connectivity state.
     */
    fun watchWifi(): Flow<NetworkState>

    /**
     *A method that returns LiveData for observing changes in WiFi state.
     *This method returns a LiveData object that emits the current state of WiFi connectivity
     *when there is a change in the connectivity state.
     */
    fun watchWifiAsLiveData(): LiveData<NetworkState>

    /**
     *A method that returns a flow for observing changes in Cellular state.
     *This method returns a Flow of NetworkState objects that emit the current state of Cellular
     *connectivity when there is a change in the connectivity state.
     */
    fun watchCellular(): Flow<NetworkState>

    /**
     *A method that returns LiveData for observing changes in Cellular state.
     *This method returns a LiveData object that emits the current state of Cellular connectivity
     *when there is a change in the connectivity state.
     */
    fun watchCellularAsLiveData(): LiveData<NetworkState> = watchCellular().asLiveData()

    /**
     *A method that returns a flow for observing changes in Ethernet state.
     *This method returns a Flow of NetworkState objects that emit the current state of Ethernet
     *connectivity when there is a change in the connectivity state.
     */
    fun watchEthernet(): Flow<NetworkState>

    /**
     *A method that returns LiveData for observing changes in Ethernet state.
     *This method returns a LiveData object that emits the current state of Ethernet connectivity
     *when there is a change in the connectivity state.
     */
    fun watchEthernetAsLiveData(): LiveData<NetworkState>

    /**
     *A sealed class representing the different states of network connectivity.
     *The state can be either ENABLE or DISABLE, which are represented by the ENABLE and DISABLE objects of the NetworkState sealed class.
     **/
    sealed class NetworkState {
        /**
         * An object representing a state where network connectivity is enabled.
         */
        object ENABLE : NetworkState()

        /**
         * An object representing a state where network connectivity is disabled.
         */
        object DISABLE : NetworkState()
    }
}