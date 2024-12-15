package com.dobby.util

import android.content.Context
import com.dobby.logging.domain.LogsRepository
import com.dobby.logs.LogsRepositoryImpl

object Logger {

    private lateinit var logsRepository: LogsRepository

    fun init(context: Context) {
        if (::logsRepository.isInitialized.not()) {
            logsRepository = LogsRepositoryImpl(fileDirProvider = { context.filesDir })
        }
    }

    fun log(message: String) {
        logsRepository.writeLog(message)
    }
}
