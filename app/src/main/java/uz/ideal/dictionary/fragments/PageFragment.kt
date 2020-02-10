package uz.ideal.dictionary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import uz.ideal.dictionary.MainActivity
import uz.ideal.dictionary.R
import uz.ideal.dictionary.adapters.WordAdapter
import uz.ideal.dictionary.database.Database
import uz.ideal.dictionary.models.WordData
import uz.ideal.dictionary.text

class PageFragment : Fragment() {

    var recyclerView: RecyclerView? = null
    var adapter = WordAdapter()
    var data = arrayListOf<WordData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.page_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.page_list)
        val arguments = arguments!!
        when (arguments.getInt("page")) {
            1 -> {
                data = Database.getBase().getAllWords()
            }
            2 -> {
                data = Database.getBase().getFavouriteWords()
            }
            3 -> {
                data = Database.getBase().getSeenWords()
            }
        }
        if (text != "") {
            when (arguments.getInt("page")) {
                1 -> {
                    data = Database.getBase().searchFromAllWords(text)
                }
                2 -> {
                    data = Database.getBase().searchFromFavourites(text)
                }
                3 -> {
                    data = Database.getBase().searchFromSeen(text)
                }
            }
        }

        adapter.data = data
        recyclerView!!.adapter = adapter
        MainActivity.onUpdate = object : MainActivity.Companion.OnUpdate {
            override fun goTo(pos: Int) {
                recyclerView!!.smoothScrollToPosition(pos)

            }
        }

    }

    companion object {
        fun getFragment(bundle: Bundle): PageFragment {
            val fragment = PageFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

}