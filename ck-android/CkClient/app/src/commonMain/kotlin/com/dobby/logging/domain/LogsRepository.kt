package com.dobby.logging.domain

interface LogsRepository {

    fun writeLog(log: String)

    fun readLogs(): List<String>

    fun clearLogs()
}
