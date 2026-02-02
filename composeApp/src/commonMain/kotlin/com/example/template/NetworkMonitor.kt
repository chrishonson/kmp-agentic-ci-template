
package com.example.template

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface NetworkMonitor {
    val isOnline: StateFlow<Boolean>
}

class AlwaysOnlineNetworkMonitor : NetworkMonitor {
    override val isOnline: StateFlow<Boolean> = MutableStateFlow(true)
}
