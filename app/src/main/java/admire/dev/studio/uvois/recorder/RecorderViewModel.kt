package admire.dev.studio.uvois.recorder

import androidx.lifecycle.ViewModel
import android.content.Context
import admire.dev.studio.uvois.util.RecorderState

class RecorderViewModel(val recorderRepository: RecorderRepository): ViewModel() {

    var recorderState: RecorderState = RecorderState.Stopped

    fun startRecording() = recorderRepository.startRecording()

    fun stopRecording(context: Context) = recorderRepository.stopRecording(context)

    fun pauseRecording() = recorderRepository.pauseRecording()

    fun resumeRecording() = recorderRepository.resumeRecording()

    fun getRecordingTime() = recorderRepository.getRecordingTime()

}