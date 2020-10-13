package com.montdeska.android_hacker_news.ui.stories

import SwipeToDelete
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.montdeska.android_hacker_news.R
import com.montdeska.android_hacker_news.base.BaseFragment
import com.montdeska.android_hacker_news.data.local.models.Story
import com.montdeska.android_hacker_news.databinding.StoriesFragmentBinding
import com.montdeska.android_hacker_news.utils.getConnectivity
import kotlinx.android.synthetic.main.stories_fragment.*


class StoriesFragment : BaseFragment() {

    private lateinit var binding: StoriesFragmentBinding
    private lateinit var viewModel: StoriesViewModel
    private lateinit var adapter: StoriesAdapter
    private lateinit var mStories: MutableList<Story>
    private var offlineModeSnackbar: Snackbar? = null

    companion object {
        fun newInstance() = StoriesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.stories_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StoriesViewModel::class.java)
        adapter = StoriesAdapter(storyClickListener())
        binding.list.adapter = adapter

        addRefreshListener()
        observeOfflineMode()
        observeStories(adapter)


        enableSwipeToDeleteAndUndo()
        startGettingStories(getConnectivity(requireContext()))
    }

    private fun storyClickListener(): StoriesAdapter.StoryClickListener {
        return StoriesAdapter.StoryClickListener { storyUrl ->
            if (storyUrl.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.bloken_link_message),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                val bundle = Bundle()
                bundle.putString("storyUrl", storyUrl)
                NavHostFragment.findNavController(this).navigate(R.id.storyReaderFragment, bundle)
            }
        }
    }

    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDelete =
            object : SwipeToDelete(requireContext()) {
                override fun onSwiped(@NonNull viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    val item: Story = adapter.getItemStory(position)
                    mStories.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    viewModel.updateStory(item, true)
                    showSnackbarForSwipedStory(position, item)
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.list)
    }

    private fun showSnackbarForSwipedStory(
        position: Int,
        item: Story
    ) {
        val snackbar = Snackbar.make(
            requireView(),
            getString(R.string.story_deleted_message),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("UNDO") {
            restoreSwipedStory(position, item)
        }
        snackbar.setActionTextColor(Color.WHITE)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
        snackbar.show()
    }

    private fun restoreSwipedStory(
        position: Int,
        item: Story
    ) {
        mStories.add(position, item)
        list.scrollToPosition(position)
        viewModel.updateStory(item, false)
        adapter.notifyItemInserted(position)
    }

    private fun startGettingStories(hasConnectivity: Boolean) {
        pullToRefresh.isRefreshing = true
        viewModel.getStories(hasConnectivity)
    }

    private fun observeOfflineMode() {
        viewModel.offlineModeOn.observe(viewLifecycleOwner, { offlineModeOn ->
            offlineModeOn.let {
                if (offlineModeOn)
                    getOfflineModeSnackbar(
                        getString(R.string.offline_mode_message),
                        R.color.delete,
                        Snackbar.LENGTH_INDEFINITE
                    )!!.show()
                else if (offlineModeSnackbar != null && offlineModeSnackbar!!.isShown)
                    getOfflineModeSnackbar(
                        getString(R.string.online_mode_message),
                        R.color.success,
                        Snackbar.LENGTH_LONG
                    )!!.show()
            }
        })
    }

    private fun getOfflineModeSnackbar(message: String, color: Int, duration: Int): Snackbar? {
        offlineModeSnackbar = Snackbar.make(
            requireView(),
            message,
            duration
        )
        offlineModeSnackbar!!.view.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                color
            )
        )
        offlineModeSnackbar!!.setActionTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        return offlineModeSnackbar
    }

    private fun addRefreshListener() {
        pullToRefresh.setColorSchemeResources(R.color.white)
        pullToRefresh.setProgressBackgroundColorSchemeResource(R.color.colorPrimaryDark)
        pullToRefresh.setOnRefreshListener {
            list.alpha = 0.5f
            startGettingStories(getConnectivity(requireContext()))
        }
    }

    private fun observeStories(adapter: StoriesAdapter) {
        viewModel.stories.observe(viewLifecycleOwner, { stories ->
            stories.let {
                mStories = stories as MutableList<Story>
                adapter.submitList(stories)
                pullToRefresh.isRefreshing = false
                list.alpha = 1f
            }
        })
    }

}