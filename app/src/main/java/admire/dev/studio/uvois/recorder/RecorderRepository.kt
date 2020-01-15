package admire.dev.studio.uvois.recorder

import android.annotation.SuppressLint
import android.annotation.TargetApi
import androidx.lifecycle.MutableLiveData
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import com.dev.studio.uvois.R
import kotlinx.android.synthetic.main.activity_main_input_title.view.*
import java.io.IOException
import java.io.File
import java.util.*

class RecorderRepository{

    companion object {
        @Volatile
        private var instance: RecorderRepository? = null

        fun getInstance() =
            instance
                ?: synchronized(this) {
                instance
                    ?: RecorderRepository().also { instance = it }
            }
    }

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private val dir: File = File(Environment.getExternalStorageDirectory().absolutePath + "/uvois/")

    private var recordingTime: Long = 0
    private var timer = Timer()
    private val recordingTimeString = MutableLiveData<String>()

    @SuppressLint("RestrictedApi")
    fun startRecording() {
        try {
            // create a File object for the parent directory
            val recorderDirectory = File(Environment.getExternalStorageDirectory().absolutePath+"/uvois/")
            // have the object build the directory structure, if needed.
            recorderDirectory.mkdirs()

            if(dir.exists()) {
                dir.listFiles()?.let {
                    val count = it.size
                    find@ for (i in 0..count+1) {
                        if (!File(Environment.getExternalStorageDirectory().absolutePath + "/uvois/recording" + i + ".mp3").exists()) {
                            output = Environment.getExternalStorageDirectory().absolutePath + "/uvois/recording" + i + ".mp3"
                            break@find
                        }
                    }
                }
            }

            mediaRecorder = MediaRecorder()

            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mediaRecorder?.setOutputFile(output)

            println("Starting recording!")
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            timer = Timer()
            startTimer()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi")
    fun stopRecording(context: Context){
        mediaRecorder?.stop()
        mediaRecorder?.release()
        stopTimer()
        resetTimer()

        initRecorder(context)
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    fun pauseRecording(){
        stopTimer()
        mediaRecorder?.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    fun resumeRecording(){
        timer = Timer()
        startTimer()
        mediaRecorder?.resume()
    }

    private fun initRecorder(context: Context) {
        mediaRecorder = MediaRecorder()

        if(dir.exists()){
            val input_title_dialog = AlertDialog.Builder(context)

            val inflater = context.getSystemService( Context.LAYOUT_INFLATER_SERVICE ) as LayoutInflater

            input_title_dialog.setTitle("Введите название")
            val activity_main_input_title = inflater.inflate(R.layout.activity_main_input_title, null)
            input_title_dialog.setView(activity_main_input_title)
            val old_title = output
            input_title_dialog.setPositiveButton("Cохранить") { _, _ ->
                run {
                    val title = activity_main_input_title.input_title.text.toString()
                    if(title != "") {
                        val new_title = Environment.getExternalStorageDirectory().absolutePath + "/uvois/" +
                                title + ".mp3"

                        File(old_title).renameTo(File(new_title))
                    }
                }
            }
            input_title_dialog.create().show()

            val count = dir.listFiles().size
            find@ for (i in 0..count+1) {
                if (!File(Environment.getExternalStorageDirectory().absolutePath + "/uvois/recording" + i + ".mp3").exists()) {
                    output = Environment.getExternalStorageDirectory().absolutePath + "/uvois/recording" + i + ".mp3"
                    break@find
                }
            }
        }

        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
    }

    private fun startTimer(){
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                recordingTime += 1
                updateDisplay()
            }
        }, 1000, 1000)
    }

    private fun stopTimer(){
        timer.cancel()
    }


    private fun resetTimer() {
        timer.cancel()
        recordingTime = 0
        recordingTimeString.postValue("00:00")
    }

    private fun updateDisplay(){
        val minutes = recordingTime / (60)
        val seconds = recordingTime % 60
        val str = String.format("%d:%02d", minutes, seconds)
        recordingTimeString.postValue(str)
    }

    fun getRecordingTime() = recordingTimeString
}