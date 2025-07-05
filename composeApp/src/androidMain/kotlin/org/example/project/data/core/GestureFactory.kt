package org.example.project.data.core

import android.content.Context
import com.google.mediapipe.tasks.vision.core.RunningMode

class GestureRecognizerHelperFactory(
    private val context: Context
) {
    fun create(runningMode: RunningMode, listener: GestureRecognizerHelper.GestureRecognizerListener?): GestureRecognizerHelper {
        return GestureRecognizerHelper(
            context = context,
            runningMode = runningMode,
            gestureRecognizerListener = listener
        )
    }
}