package com.example.template

import kotlin.test.Test

import kotlin.test.assertTrue

class NetworkMonitorTest {
    @Test
    fun `AlwaysOnlineNetworkMonitor always returns online`() {
        val monitor = AlwaysOnlineNetworkMonitor()
        assertTrue(monitor.isOnline.value)
    }
}
