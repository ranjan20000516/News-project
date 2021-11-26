package com.example.neapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neapp.R
import com.example.neapp.ui.MainActivity
import com.example.neapp.ui.NewsViewModel
import com.example.neapp.ui.adapters.NewsAdapter
import com.example.neapp.ui.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.neapp.ui.utils.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_breaking_news.paginationProgressBar


class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    private val TAG="Breaking NewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= (activity as MainActivity).viewModel

        setUpRecycleView()

        newsAdapter.setOnItemClickedListener {
          val bundle= Bundle().apply {
              putSerializable("article", it)
          }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, { response ->
          when(response){
             is Resource.Success ->{
                 hideProgressBar()
                 response.data?.let { newsResponse ->
                  newsAdapter.differ.submitList(newsResponse.articles)
                     val totalPages= newsResponse.totalResults/ QUERY_PAGE_SIZE + 2
                     isLastPage= viewModel.breakingNewsPage == totalPages
                     if (isLastPage){
                         rvBreakingNews.setPadding(0,0,0,0)
                     }
                 }
             }

              is Resource.Error ->{
                hideProgressBar()
                  response.message?.let { message ->
                  Log.e(TAG,"An error occurred $message")
                  }
              }

              is Resource.Loading ->{
                  showProgressBar()
              }
          }
        })
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility= View.VISIBLE
        isLoading= true
    }

    private fun hideProgressBar() {
   paginationProgressBar.visibility= View.INVISIBLE
        isLoading=false
    }

    var isLoading:Boolean = false
    var isLastPage:Boolean = false
    var isScrolling:Boolean = false

     private val scrollListener = object : RecyclerView.OnScrollListener(){

         override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
             super.onScrollStateChanged(recyclerView, newState)
             if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL ){
                  isScrolling= true
             }
         }

         override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
             super.onScrolled(recyclerView, dx, dy)
             val layoutManager= recyclerView.layoutManager as LinearLayoutManager
             val firstVisibleItemPosition= layoutManager.findFirstVisibleItemPosition()
             val visibleItemCount= layoutManager.childCount
             val totalItemCount= layoutManager.itemCount

             val isNotLoadingAndNotLastPage= !isLoading && !isLastPage
             val  isAtLastItem= firstVisibleItemPosition + visibleItemCount >= totalItemCount
             val isNotBeginning= firstVisibleItemPosition >= 0
             val isTotalMoreThanVisible= totalItemCount >= QUERY_PAGE_SIZE
             val shouldPaginate= isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning && isTotalMoreThanVisible
                     && isScrolling
             if (shouldPaginate){
                 viewModel.getBreakingNews("in")
                 isScrolling= false
             }
         }
     }

    private fun setUpRecycleView() {
      newsAdapter= NewsAdapter()
        rvBreakingNews.apply {
          adapter= newsAdapter
            layoutManager= LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}