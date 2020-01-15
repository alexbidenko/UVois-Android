package admire.dev.studio.uvois

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import admire.dev.studio.uvois.player.RecordingsActivity
import admire.dev.studio.uvois.recorder.RecorderViewModel
import admire.dev.studio.uvois.util.InjectorUtils
import admire.dev.studio.uvois.util.RecorderState
import com.dev.studio.uvois.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var viewModel: RecorderViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()

        fab_start_recording.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else {
                startRecording()
            }
        }

        fab_stop_recording.setOnClickListener{
            stopRecording()
        }

        fab_pause_recording.setOnClickListener {
            pauseRecording()
        }

        fab_resume_recording.setOnClickListener {
            resumeRecording()
        }

        fab_recordings.setOnClickListener {
            val intent = Intent(this, RecordingsActivity::class.java)
            startActivity(intent)
        }

        if(viewModel?.recorderState == RecorderState.Stopped){
            fab_stop_recording.isEnabled = false
        }
    }

    private fun initUI() {
        val factory = InjectorUtils.provideRecorderViewModelFactory()

        viewModel = ViewModelProviders.of(this, factory).get(RecorderViewModel::class.java)

        addObserver()
    }

    private fun addObserver() {
        viewModel?.getRecordingTime()?.observe(this, Observer {
            textview_recording_time.text = it
        })
    }


    @SuppressLint("RestrictedApi")
    private fun startRecording() {
        viewModel?.startRecording()

        fab_stop_recording.isEnabled = true
        fab_start_recording.visibility = View.INVISIBLE
        fab_pause_recording.visibility = View.VISIBLE
        fab_resume_recording.visibility = View.INVISIBLE
    }

    @SuppressLint("RestrictedApi")
    private fun stopRecording(){
        viewModel?.stopRecording(this)

        fab_stop_recording.isEnabled = false
        fab_start_recording.visibility = View.VISIBLE
        fab_pause_recording.visibility = View.INVISIBLE
        fab_resume_recording.visibility = View.INVISIBLE
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    private fun pauseRecording(){
        viewModel?.pauseRecording()

        fab_stop_recording.isEnabled = true
        fab_start_recording.visibility = View.INVISIBLE
        fab_pause_recording.visibility = View.INVISIBLE
        fab_resume_recording.visibility = View.VISIBLE
    }

    @TargetApi(Build.VERSION_CODES.N)
    @SuppressLint("RestrictedApi")
    private fun resumeRecording(){
        viewModel?.resumeRecording()
        fab_stop_recording.isEnabled = true
        fab_start_recording.visibility = View.INVISIBLE
        fab_pause_recording.visibility = View.VISIBLE
        fab_resume_recording.visibility = View.INVISIBLE
    }

}
