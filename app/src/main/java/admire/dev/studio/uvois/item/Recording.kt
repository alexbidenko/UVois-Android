package admire.dev.studio.uvois.item

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.dev.studio.uvois.R
import admire.dev.studio.uvois.player.RecordingRepository
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recording_layout.view.*

class Recording(val title: String, val context: Context): Item(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.recording_title_textview.text = title.substringBeforeLast(".")
        viewHolder.itemView.recording_image.setOnClickListener {
            RecordingRepository.playRecording(context, title )
        }
        viewHolder.itemView.delete_image.setOnClickListener {
            RecordingRepository.daleteRecording(context, title )
            viewHolder.itemView.visibility = View.GONE
            (viewHolder.itemView.parent as RecyclerView).Recycler()
        }
        viewHolder.itemView.share_image.setOnClickListener {
            RecordingRepository.shareRecording(context, title )
        }
    }

    override fun getLayout(): Int {
        return R.layout.recording_layout
    }

}