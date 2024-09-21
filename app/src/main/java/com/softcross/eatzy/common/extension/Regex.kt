package com.softcross.eatzy.common.extension

import java.util.regex.Pattern

fun String.emailRegex(): Boolean {
    val pattern = Pattern.compile(
        "^[A-Za-z](?=\\S+\$)(.*)([@]{1})(?=\\S+\$)(.{1,})(\\.)(.{1,})"
    )
    return if (this.isNotEmpty()) {
        pattern.matcher(this).matches()
    } else {
        false
    }
}

fun String.passwordRegex(): Boolean {
    val pattern = Pattern.compile(
        "^(?=.*[0-9])(?=\\S+\$)(?=.*[a-z])(?=.*[A-Z]).{8,}\$"
    )
    return if (this.isNotEmpty()) {
        return pattern.matcher(this).matches()
    } else {
        false
    }
}

fun String.phoneRegex(): Boolean {
    val pattern =
        Pattern.compile("(^[0\\s]?[\\s]?)([(]?)([5])([0-9]{2})([)]?)([\\s]?)([0-9]{3})([\\s]?)([0-9]{2})([\\s]?)([0-9]{2})\$")
    return if (this.isNotEmpty()) {
        pattern.matcher(this).matches()
    } else {
        false
    }
}

fun String.nameSurnameRegexWithSpace(): Boolean {
    val pattern = Pattern.compile("^[a-zA-Z]+(?:\\s[a-zA-Z]+)+$")
    return if (this.isNotEmpty()) {
        pattern.matcher(this).matches()
    } else {
        false
    }
}

fun String.userNameRegex(): Boolean {
    return if (this.isNotEmpty()) {
        this.length >= 6
    } else {
        false
    }
}


fun String.creditCardNumberRegex(): Boolean {
    val pattern = Pattern.compile(
        ("^[0-9]{16}$")
    )
    return if (this.isNotEmpty()) {
        pattern.matcher(this).matches()
    } else {
        false
    }
}

fun String.creditCardDateRegex(): Boolean {
    val pattern = Pattern.compile("^(0[1-9]|1[0-2])\\/?(2[4-9]|[3-9][0-9])\$")
    return if (this.isNotEmpty()) {
        pattern.matcher(this).matches()
    } else {
        false
    }
}

fun String.cvcRegex(): Boolean {
    val pattern = Pattern.compile("^[0-9]{3,4}$")
    return if (this.isNotEmpty()) {
        pattern.matcher(this).matches()
    } else {
        false
    }
}