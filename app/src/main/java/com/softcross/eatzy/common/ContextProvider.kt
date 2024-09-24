package com.softcross.eatzy.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContextProvider @Inject constructor(@ApplicationContext val context: Context)