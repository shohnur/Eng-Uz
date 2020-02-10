package uz.ideal.dictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_word.view.*
import net.cachapa.expandablelayout.ExpandableLayout
import uz.ideal.dictionary.R
import uz.ideal.dictionary.models.WordData

class WordAdapter : RecyclerView.Adapter<WordAdapter.ViewHolder>() {

    var data = arrayListOf<WordData>()
        set(value) {
            field.clear()
            field = value
            notifyDataSetChanged()
        }

    var id = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindData(data[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(model: WordData) {
            itemView.word.text = model.word
            itemView.translation.text = model.translation
            itemView.description.text = model.description
            itemView.setOnClickListener {
                id = model.id
                itemView.findViewById<ExpandableLayout>(R.id.exp).toggle()
            }
            if (id != model.id) {
                itemView.findViewById<ExpandableLayout>(R.id.exp).collapse()
            }
        }
    }
}