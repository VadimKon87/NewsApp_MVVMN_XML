package com.androiddevs.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.adapters.NewsAdapter
import com.androiddevs.mvvmnewsapp.ui.NewsActivity
import com.androiddevs.mvvmnewsapp.ui.NewsViewModel
import com.androiddevs.mvvmnewsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    //step 6.5.1
    lateinit var viewModel: NewsViewModel
    //step 7.3.1
    lateinit var newsAdapter: NewsAdapter
    //7.3.6
    val TAG = "BreakingNewsFragment"


    //6.5.2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel

        //7.3.4
        setupRecyclerView()

        //STEP 9.3
        /*
        * This code sets a click listener on the "newsAdapter" object and defines the behavior that
        * should occur when an item in the adapter is clicked. The listener takes as an argument
        * an "article" object, creates a Bundle object and adds the "article" object to it as a
        * serializable item under the key "article". Finally, the code uses the "findNavController()"
        * method to navigate to a different fragment (the "articleFragment") by following the action
        * specified in the navigation graph (R.id.action_breakingNewsFragment_to_articleFragment),
        * and passing the bundle as arguments to the destination fragment.
        * */

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
        //9.3 end

        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
        //11.3.6
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        // add 2 because of division result will be rounded (+1) and last page of
                        // response will always be empty (another +1 for that)
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage) {
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        // 12.6 replace Log with Toast
                        //Log.e(TAG, "An error occurred: $message")
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }
    //7.3.5
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false //11.3.5
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true //11.3.5

    }

    // 11.3.1
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    //11.3.2 ctrl + o to override functions, choose these 2

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            //added code below
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            //11.3.3
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE // create constant

            // based on these booleans we decide to paginate or not
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("ca")
                isScrolling = false
            }
        }
    }
    // end 11.3.3

    //7.3.2
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
    // 11.3.4
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}