package admire.dev.studio.uvois.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecordingViewModelProvider(val recordingRepository: RecordingRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecordingViewModel(
            recordingRepository
        ) as T
    }
}