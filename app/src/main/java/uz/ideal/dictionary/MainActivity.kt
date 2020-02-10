package uz.ideal.dictionary

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import uz.ideal.dictionary.adapters.PageAdapter
import uz.ideal.dictionary.adapters.WordAdapter
import uz.ideal.dictionary.database.Database
import uz.ideal.dictionary.dialogs.Dialog
import uz.ideal.dictionary.models.WordData

@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var adapter: PageAdapter? = null
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        addWord.visibility = View.VISIBLE
        clear.visibility = View.GONE

        search_view.setOnQueryTextListener(this)



        clear.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("Warning!!!").setMessage("Are you sure to clear this list?")
                .setPositiveButton("Sure") { dialog, _ ->
                    val d = Database.getBase().getSeenWords()
                    for (i in d.indices) {
                        Database.getBase().removeFromSeen(d[i].id)
                    }
                    dialog.cancel()
                    fullReload()
                }.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }.create().show()
        }
        addWord.setOnClickListener {
            val dialog = Dialog(this, "New word")
            dialog.onSaveClicked = object : Dialog.OnSaveClicked {
                override fun save(word: String, translation: String, description: String) {
                    Database.getBase().newWord(word, translation, description)
                    fullReload()
                }
            }
            dialog.show()
        }

        WordAdapter.onChanged = object : WordAdapter.Companion.OnChanged {
            override fun change() = fullReload()
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                this@MainActivity.position = position
                if (position == 0) {
                    addWord.visibility = View.VISIBLE
                    clear.visibility = View.GONE
                }
                if (position == 1) {
                    addWord.visibility = View.GONE
                    clear.visibility = View.GONE
                }
                if (position == 2) {
                    addWord.visibility = View.GONE
                    clear.visibility = View.VISIBLE
                }
            }

            override fun onPageSelected(position: Int) {
            }
        })

        WordAdapter.onEdit = object : WordAdapter.Companion.OnEdit {
            override fun edit(pos: Int) {
                var data = arrayListOf<WordData>()
                when (position) {
                    0 -> {
                        data = Database.getBase().getAllWords()
                    }
                    1 -> {
                        data = Database.getBase().getFavouriteWords()
                    }
                    2 -> {
                        data = Database.getBase().getSeenWords()
                    }
                }
                val dialog = Dialog(this@MainActivity, "Edit")
                dialog.setWord(data[pos].word)
                dialog.setTranslation(data[pos].translation)
                dialog.setDescription(data[pos].description)
                dialog.onSaveClicked = object : Dialog.OnSaveClicked {
                    override fun save(word: String, translation: String, description: String) {
                        Database.getBase().update(data[pos].id, word, translation, description)
                        fullReload()
                        onUpdate?.goTo(pos)
                    }
                }
                dialog.show()
            }
        }
    }

    companion object {
        interface OnUpdate {
            fun goTo(pos: Int)
        }

        interface OnSearch {
            fun search(text: String, pos: Int)
        }

        var onSearch: OnSearch? = null
        var onUpdate: OnUpdate? = null
    }

    fun fullReload() {
        adapter = null
        adapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.currentItem = position

        if (position == 0) {
            addWord.visibility = View.VISIBLE
            clear.visibility = View.GONE
        }
        if (position == 1) {
            addWord.visibility = View.GONE
            clear.visibility = View.GONE
        }
        if (position == 2) {
            addWord.visibility = View.GONE
            clear.visibility = View.VISIBLE
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        text = if (newText != "") {
            newText!!
        } else {
            ""
        }

        fullReload()
        return true
    }


}
