package com.slack.exercise.ui.usersearch

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.slack.exercise.R
import com.slack.exercise.databinding.FragmentUserSearchBinding
import com.slack.exercise.model.UserSearchResult
import com.slack.exercise.view.ViewPager2Adapter
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

/**
 * Main fragment displaying and handling interactions with the view.
 * We use the MVP pattern and attach a Presenter that will be in charge of non view related operations.
 */
class UserSearchFragment : DaggerFragment(), UserSearchContract.View {

    @Inject
    internal lateinit var presenter: UserSearchPresenter

    private lateinit var userSearchBinding: FragmentUserSearchBinding

    private val alphabets = arrayOf("a", "b", "c")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        userSearchBinding = FragmentUserSearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return userSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpList()
        toSetUpViewPager()
        setUpSearch()
    }

    override fun onStart() {
        super.onStart()

        presenter.attach(this)
    }

    override fun onStop() {
        super.onStop()

        presenter.detach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user_search, menu)

        val searchView: SearchView = menu.findItem(R.id.search_menu_item).actionView as SearchView
        searchView.queryHint = getString(R.string.search_users_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                presenter.onQueryTextChange(newText)
                return true
            }
        })
    }

    override fun onUserSearchResults(results: Set<UserSearchResult>) {
        val adapter = userSearchBinding.userSearchResultList.adapter as UserSearchAdapter
        adapter.setResults(results)
    }

    override fun onUserSearchError(error: Throwable) {
        Timber.e(error, "Error searching users.")
    }

    private fun setUpList() {
        with(userSearchBinding.userSearchResultList) {
            adapter = UserSearchAdapter()
            layoutManager = LinearLayoutManager(activity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setItemSpacing(DividerItemDecoration.VERTICAL, R.drawable.list_item_divider_decoration)
            setHasFixedSize(true)
        }
    }

    private fun setUpSearch() {
      userSearchBinding.searchInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s?.length == 0) {
                val position = userSearchBinding.viewpager.currentItem
                presenter.onQueryTextChange(alphabets[position])
            } else {
                presenter.onQueryTextChange(s.toString())
            }
        }
      })
    }

    private fun toSetUpViewPager() {

        userSearchBinding.viewpager.adapter = ViewPager2Adapter(requireContext())

        TabLayoutMediator(
            userSearchBinding.tabLayout,
            userSearchBinding.viewpager
        ) { tab, position ->
        }.attach()

        userSearchBinding.viewpager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    presenter.onQueryTextChange(
                        if (position < alphabets.size) alphabets.get(
                            position
                        ) else alphabets.get(0)
                    )
                    super.onPageSelected(position)
                }
            }
        )

    }

    private fun RecyclerView.setItemSpacing(direction: Int, element_id: Int) {
        val itemDecoration = DividerItemDecoration(context, direction)
        itemDecoration.setDrawable(ContextCompat.getDrawable(context, element_id)!!)
        this.addItemDecoration(itemDecoration)
    }

}