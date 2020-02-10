package uz.ideal.dictionary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import uz.ideal.dictionary.adapters.PageAdapter

class MainActivity : AppCompatActivity() {

    private var adapter: PageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = PageAdapter(supportFragmentManager)
        viewPager.adapter = adapter

    }
}
