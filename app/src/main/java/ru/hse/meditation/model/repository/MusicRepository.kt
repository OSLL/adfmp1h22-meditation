package ru.hse.meditation.model.repository

import android.app.Application
import android.widget.ProgressBar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File
import kotlin.math.roundToInt

class MusicRepository(application: Application) {
    private val assets = application.assets
    private val musicDir = application.filesDir.resolve("music").apply {
        mkdir()
    }

    fun getMusicFileFor(courseId: String, musicFileName: String): File {
        val courseMusicDir = musicDir.resolve(courseId).apply { mkdir() }
        return courseMusicDir.resolve(musicFileName).also {
            if (!it.exists()) {
                assets.open(musicFileName).use { input ->
                    it.outputStream().use { output ->
                        input.copyTo(output, 1024)
                    }
                }
            }
        }
    }

    suspend fun loadMusicForCourse(courseId: String, progressBar: ProgressBar) {
        val courseMusicDir = musicDir.resolve(courseId).apply { mkdir() }
        val root = Firebase.storage.reference
        val courseDirFiles = root.child("courses/$courseId").listAll().await().items
        val totalSize = courseDirFiles.sumOf { it.metadata.await().sizeBytes }
        var currentSize = 0L
        courseDirFiles.forEach {
            if (it.name == "course.json") {
                return@forEach
            }
            it.getFile(courseMusicDir.resolve(it.name).apply { createNewFile() })
                .addOnProgressListener {
                    progressBar.progress =
                        (100 * (currentSize + it.bytesTransferred.toDouble()) / totalSize).roundToInt()
                }.addOnSuccessListener {
                    currentSize += it.totalByteCount
                }.await()
        }
    }
}
