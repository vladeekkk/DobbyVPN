package com.dobby.feature.logging.domain

interface LogsRepository {

    fun writeLog(log: String)

    fun readLogs(): List<String>

    fun clearLogs()
}
