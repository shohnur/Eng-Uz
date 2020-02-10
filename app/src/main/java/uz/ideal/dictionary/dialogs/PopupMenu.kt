package uz.ideal.dictionary.dialogs

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import uz.ideal.dictionary.R

class PopupMenu(var context: Context, var view: View) :
    PopupMenu(context, view) {

    init {

        inflate(R.menu.menu)
        setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.edit) {
                onEdit?.doThis()
            }
            if (item.itemId == R.id.delete) {
                onDelete?.doThis()
            }
            true
        }

    }

    interface OnClick {
        fun doThis()
    }

    var onEdit: OnClick? = null
    var onDelete: OnClick? = null

}