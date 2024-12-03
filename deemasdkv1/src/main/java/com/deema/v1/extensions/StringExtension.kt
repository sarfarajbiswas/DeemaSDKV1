package com.deema.v1.extensions

import android.util.Patterns


fun String.withPrefix(prefix: String): String {
    return "$prefix$this"
}

fun String.numberToWord(): String {
   return when(this){
       "1" -> "One"
       "2" -> "Two"
       "3" -> "Three"
       "4" -> "Four"
       "5" -> "Five"
       "6" -> "Six"
       "7" -> "Seven"
       "8" -> "Eight"
       "9" -> "Nine"
       else -> "Invalid number"
   }
}


fun String.isEmailValid(): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return emailRegex.toRegex().matches(this)
}

fun String.isPhoneNumberValid(): Boolean {
    val phoneRegex = "^\\d{10}$"
    return phoneRegex.toRegex().matches(this)
}

fun String.isPasswordValid(): Boolean {
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
    return passwordRegex.toRegex().matches(this)
}

fun String?.isStringNotEmpty(): Boolean {
    return !this.isNullOrEmpty()
}

fun String.isValidUrl(): Boolean {
    return this.startsWith("http://") || this.startsWith("https://") && Patterns.WEB_URL.matcher(this).matches()
}

