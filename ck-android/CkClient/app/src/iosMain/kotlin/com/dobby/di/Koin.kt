package com.dobby.di

import com.dobby.main.presentation.MainViewModel
import org.koin.mp.KoinPlatform

fun getMainViewModel(): MainViewModel = KoinPlatform.getKoin().get()
