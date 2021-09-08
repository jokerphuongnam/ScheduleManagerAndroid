package com.pnam.schedulemanager.model.database.local

import com.pnam.schedulemanager.model.database.domain.Media
import javax.inject.Singleton

@Singleton
interface DownloadFile {
    suspend fun downloadFile(media: Media)
}