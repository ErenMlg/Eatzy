package com.softcross.eatzy.common.extension

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

fun changeAppLanguage(context: Context, languageCode: String) {
    Log.d("Settings", "Changing language to $languageCode")

    val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    prefs.edit().putString("app_language", languageCode).apply()

    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    // Uygulama yapılandırmasını güncelle
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    // Uygulama yeniden başlatılıyor
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
