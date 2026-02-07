package com.example.template

external class Date {
    override fun toString(): String
}

actual fun getCurrentTimestamp(): String {
    return Date().toString()
}
