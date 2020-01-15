package admire.dev.studio.uvois.player

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import java.io.File
import android.content.Intent

class RecordingRepository{
    companion object {
        @Volatile
        private var instance: RecordingRepository? = null

        fun getInstance() =
            instance
                ?: synchronized(this) {
                instance
                    ?: RecordingRepository().also { instance = it }
            }


        fun playRecording(context: Context, title: String){
            if(File(Environment.getExternalStorageDirectory().absolutePath + "/uvois/$title").exists()) {
                val path = Uri.parse(Environment.getExternalStorageDirectory().absolutePath + "/uvois/$title")


                val manager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                if (manager.isMusicActive) {
                    Toast.makeText(context, "Аудиозапись уже запущена, дождитесь окончания.", Toast.LENGTH_SHORT).show()
                } else {
                    MediaPlayer().apply {
                        setAudioStreamType(AudioManager.STREAM_MUSIC)
                        setDataSource(context, path)
                        prepare()
                        start()
                    }
                }
            } else {
                Toast.makeText(context, "Аудиозапись удалена.", Toast.LENGTH_SHORT).show()
            }
        }

        fun daleteRecording(context: Context, title: String){
            val path = Environment.getExternalStorageDirectory().absolutePath+"/uvois/$title"

            val file = File(path).delete()
            if(file) {
                Toast.makeText(context, "Аудиозапись успешно удалена.", Toast.LENGTH_SHORT).show()
            }
        }

        fun shareRecording(context: Context, title: String){
            /*val imagePath = File(Environment.getExternalStorageDirectory().absolutePath, "uvois")
            val newFile = File(imagePath, title)
            val contentUri = FileProvider.getUriForFile(context, "com.example.gabriel.uvois.provider", newFile)*/

            val path = Environment.getExternalStorageDirectory().absolutePath+"/uvois/$title"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "audio/*"
            sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
            /*sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            sharingIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri))
            sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)*/
            context.startActivity(Intent.createChooser(sharingIntent, "Отправить аудиозапись"))
        }
    }

    private val recorderDirectory = File(Environment.getExternalStorageDirectory().absolutePath+"/uvois/")

    fun getRecordings(): ArrayList<String> {
        val file : ArrayList<String> = ArrayList()

        val files: Array<out File>? = recorderDirectory.listFiles()
        files?.let {
            for (i in files) {
                println(i.name)
                file.add(i.name)
            }
        }

        return file
    }

}