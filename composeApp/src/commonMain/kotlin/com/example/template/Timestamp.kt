@file:JvmName("TimestampCommonKt")
package com.example.template

import kotlin.jvm.JvmName

expect fun getCurrentTimestamp(): String

fun formatTimestamp(timestamp: Long): String {
    return "Timestamp: $timestamp"
}
