// shared/src/commonMain/kotlin/org/example/project/domain/usecase/gesture/GestureUseCase.kt
package org.example.project.domain.usecase.gesture

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf // Needed for flowOf(...)
import org.example.project.data.core.Resource
import org.example.project.domain.model.gesture.GestureRecognitionResult
import org.example.project.domain.repository.GestureRepository


class GestureUseCase(private val repository: GestureRepository) {

    // The invoke function now takes the specific GestureAction type
    // and returns a consistent Flow<Resource<GestureRecognitionResult>>
    suspend operator fun invoke(action: GestureAction): Flow<Resource<GestureRecognitionResult>> {
        return when (action) {
            is GestureAction.StartLiveStreamRecognition -> {
                repository.startLiveStreamRecognition()
            }
            is GestureAction.RecognizeImage -> { // Use 'is' for data classes/objects
                val result = repository.recognizeImage(action.imageData)
                if (result != null) {
                    flowOf(Resource.Success(result))
                } else {
                    flowOf(Resource.Error(Exception("Image recognition failed.")))
                }
            }
            is GestureAction.RecognizeVideo -> { // Use 'is' for data classes/objects
                repository.recognizeVideoFile(action.videoData, action.inferenceIntervalMs)
            }
            is GestureAction.SetRecognitionParameters -> { // Use 'is' for data classes/objects
                repository.setRecognitionParameters(
                    action.minHandDetectionConfidence,
                    action.minHandTrackingConfidence,
                    action.minHandPresenceConfidence,
                    action.delegate
                )
                // Since this returns Unit, wrap a success signal in a Flow
                flowOf(Resource.Success(GestureRecognitionResult(emptyList(), 0, 0, 0)))
            }
            GestureAction.StopRecognition -> { // For data objects, direct match
                repository.stopRecognition()
                // Wrap a success signal in a Flow
                flowOf(Resource.Success(GestureRecognitionResult(emptyList(), 0, 0, 0)))
            }

            is GestureAction.AnalyzeLiveStreamFrame -> {
                repository.processLiveStreamFrame(action.imageProxy)
            }
        }
    }
}