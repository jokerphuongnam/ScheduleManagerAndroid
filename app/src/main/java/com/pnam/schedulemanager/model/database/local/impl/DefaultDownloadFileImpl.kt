package com.pnam.schedulemanager.model.database.local.impl

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import com.pnam.schedulemanager.model.database.domain.Media
import com.pnam.schedulemanager.model.database.local.DownloadFile
import com.pnam.schedulemanager.utils.RetrofitUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultDownloadFileImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DownloadFile {
    override suspend fun downloadFile(media: Media) {
        val request = DownloadManager.Request(Uri.parse(RetrofitUtils.getMediaUrl(media.mediaUrl)))
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, media.mediaName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setMimeType(media.mimeType)
        (context.getSystemService(DOWNLOAD_SERVICE) as? DownloadManager)?.enqueue(request)
    }
}