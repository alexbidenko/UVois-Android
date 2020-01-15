package admire.dev.studio.uvois.util

import admire.dev.studio.uvois.player.RecordingRepository
import admire.dev.studio.uvois.player.RecordingViewModelProvider
import admire.dev.studio.uvois.recorder.RecorderRepository
import admire.dev.studio.uvois.recorder.RecorderViewModelProvider

object InjectorUtils {
    fun provideRecorderViewModelFactory(): RecorderViewModelProvider {
        val recorderRepository = RecorderRepository.getInstance()
        return RecorderViewModelProvider(
            recorderRepository
        )
    }

    fun provideRecordingViewModelFactory(): RecordingViewModelProvider {
        val recordingRepository = RecordingRepository.getInstance()
        return RecordingViewModelProvider(
            recordingRepository
        )
    }
}