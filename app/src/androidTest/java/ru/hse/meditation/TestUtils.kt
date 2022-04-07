package ru.hse.meditation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun <T> LiveData<T>.awaitValue(): T? =
    suspendCancellableCoroutine { continuation ->
        runBlocking(Dispatchers.Main) {
            observeForever(object : Observer<T> {

                override fun onChanged(t: T) {
                    removeObserver(this)
                    continuation.resume(t) {}
                }
            })
        }
    }