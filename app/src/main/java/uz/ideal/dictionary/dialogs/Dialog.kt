package uz.ideal.dictionary.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import uz.ideal.dictionary.R

class Dialog(context: Context, title: String) : AlertDialog(context) {

    private var word: EditText? = null
    private var translation: EditText? = null
    private var description: EditText? = null
    private var save: Button? = null
    private var cancel: Button? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_item, null, false)
        word = view.findViewById(R.id.word)
        translation = view.findViewById(R.id.translation)
        description = view.findViewById(R.id.description)
        save = view.findViewById(R.id.save)
        cancel = view.findViewById(R.id.cancel)
        setTitle(title)
        cancel!!.setOnClickListener {
            cancel()
        }
        save!!.setOnClickListener {
            if (word!!.text.toString() != "" && translation!!.text.toString() != "" && description!!.text.toString() != "") {
                onSaveClicked?.save(
                    word!!.text.toString(),
                    translation!!.text.toString(),
                    description!!.text.toString()
                )
                cancel()
            } else {
                if (word!!.text.toString() == "") {
                    word!!.error = "You must fill this"
                }
                if (translation!!.text.toString() == "") {
                    translation!!.error = "You must fill this"
                }

                if (description!!.text.toString() == "") {
                    description!!.error = "You must fill this"
                }
            }
        }
        setCancelable(false)
        setView(view)
    }

    fun setWord(word: String) {
        this.word!!.setText(word)
    }

    fun setTranslation(translation: String) {
        this.translation!!.setText(translation)
    }

    fun setDescription(description: String) {
        this.description!!.setText(description)
    }

    var onSaveClicked: OnSaveClicked? = null

    interface OnSaveClicked {
        fun save(word: String, translation: String, description: String)
    }

}