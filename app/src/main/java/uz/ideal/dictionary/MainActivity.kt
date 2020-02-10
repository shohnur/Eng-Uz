package uz.ideal.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import uz.ideal.dictionary.adapters.PageAdapter
import uz.ideal.dictionary.adapters.WordAdapter
import uz.ideal.dictionary.database.Database
import uz.ideal.dictionary.dialogs.Dialog
import uz.ideal.dictionary.models.WordData

class MainActivity : AppCompatActivity() {

    private var adapter: PageAdapter? = null
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = adapter

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

        var onUpdate: OnUpdate? = null
    }

    fun fullReload() {
        adapter = null
        adapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        viewPager.currentItem = position
    }
}
