package uz.ideal.dictionary.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uz.ideal.dictionary.fragments.PageFragment

class PageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    var data: ArrayList<String> = arrayListOf("All words", "Favourites", "Last seen")

    override fun getItem(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putInt("page", position + 1)
        return PageFragment.getFragment(bundle)
    }

    override fun getCount(): Int = data.size

    override fun getPageTitle(position: Int): CharSequence? {
        return data[position]
    }

}