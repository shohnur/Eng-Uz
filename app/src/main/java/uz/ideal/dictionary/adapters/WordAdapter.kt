package uz.ideal.dictionary.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_word.view.*
import net.cachapa.expandablelayout.ExpandableLayout
import uz.ideal.dictionary.R
import uz.ideal.dictionary.database.Database
import uz.ideal.dictionary.dialogs.PopupMenu
import uz.ideal.dictionary.models.WordData
import java.util.concurrent.atomic.AtomicBoolean

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
                if (model.isSeen == 0) {
                    Database.getBase().addToSeen(model.id)
                }
                id = model.id
                itemView.findViewById<ExpandableLayout>(R.id.exp).toggle()
            }
            if (id != model.id) {
                itemView.findViewById<ExpandableLayout>(R.id.exp).collapse()
            }



            if (model.isFav == 1) {
                itemView.btn_fav.setImageResource(R.drawable.ic_star_black_24dp)
            } else {
                itemView.btn_fav.setImageResource(R.drawable.ic_star)
            }

            itemView.setOnLongClickListener {

                val menu = PopupMenu(itemView.context, itemView)

                menu.onEdit = object : PopupMenu.OnClick {
                    override fun doThis() {
                        onEdit?.edit(adapterPosition)
                    }
                }

                menu.onDelete = object : PopupMenu.OnClick {
                    override fun doThis() {
                        val d = adapterPosition
                        val d1 = data[d]
                        notifyItemRemoved(adapterPosition)
                        val a = AtomicBoolean(false)

                        val snack = Snackbar.make(
                            itemView,
                            "Moving to deleting box. You can undo this.",
                            Snackbar.LENGTH_LONG
                        )
                        snack.setAction("UNDO") {
                            notifyItemInserted(d)

                            a.set(true)
                        }

                        snack.setCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                super.onDismissed(transientBottomBar, event)

                                if (!a.get()) {
                                    Database.getBase().moveToDeletingBox(d1.id)
                                    onChanged?.change()
                                }
                            }
                        })
                        snack.show()
                    }
                }

                menu.show()

                true
            }


            itemView.btn_fav.setOnClickListener {
                if (model.isFav == 0) {
                    data[adapterPosition].isFav = 1
                    Database.getBase().addToFavourites(model.id)
                    itemView.btn_fav.setImageResource(R.drawable.ic_star_black_24dp)
                    notifyItemChanged(adapterPosition)
                    onChanged?.change()
                } else {
                    data[adapterPosition].isFav = 0
                    itemView.btn_fav.setImageResource(R.drawable.ic_star)
                    Database.getBase().removeFromFavourites(model.id)
                    notifyItemChanged(adapterPosition)
                    onChanged?.change()
                }
            }
        }
    }


    companion object {
        interface OnChanged {
            fun change()
        }

        var onChanged: OnChanged? = null

        interface OnEdit {
            fun edit(pos: Int)
        }

        var onEdit: OnEdit? = null
    }


}