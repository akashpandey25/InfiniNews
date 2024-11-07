package com.project.QuickNews.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.QuickNews.R
import com.project.QuickNews.adpters.NewsAdapter
import com.project.QuickNews.databinding.FragmentHeadlinesBinding
import com.project.QuickNews.ui.MainActivity

import com.project.QuickNews.ui.NewsViewModel
import com.project.QuickNews.utils.Contents
import com.project.QuickNews.utils.Resource


class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorTextView:TextView
    lateinit var itemHeadlinesError: CardView
    lateinit var binding: FragmentHeadlinesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentHeadlinesBinding.bind(view)

        itemHeadlinesError=view.findViewById(R.id.itemHeadlinesError)
        //calling the layout here from layout in res for accesing item_error
        val inflater=requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View=inflater.inflate(R.layout.item_error, null)

        retryButton=view.findViewById(R.id.retrybutton)
        errorTextView=view.findViewById(R.id.txtview)

        newsViewModel=(activity as MainActivity).newsViewModel
        setUpHeadlinesRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment, bundle)
        }

        newsViewModel.headlines.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success<*>->{
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let{NewsResponse->
                        newsAdapter.differ.submitList(NewsResponse.articles.toList())
                        val totalpages=NewsResponse.totalResults /Contents.QUERY_PER_PAGE+2
                        isLastPage=newsViewModel.headlinesPage==totalpages
                        if(isLastPage){
                            binding.recyclerView.setPadding(0,0,0,0)
                        }

                    }?:showErrorMessage("No data available")
                }

                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let{message->
                        Toast.makeText(activity, "Sorry error: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)

                    }?:showErrorMessage("Unknown error occurred")




                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }

        })
        retryButton.setOnClickListener {
            newsViewModel.getHeadlines("in")
        }



    }

    var isError=false
    var isLoading=false
    var isLastPage=false
    var isScrolling=false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    private fun hideErrorMessage(){
        itemHeadlinesError.visibility=View.INVISIBLE
        isError=false
    }
    private fun showErrorMessage(message: String){
        itemHeadlinesError.visibility=View.VISIBLE
        errorTextView.text=message
        isError=true
    }
    val scrollListener= object:RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            //retrives the linear layoutmanager from recycler view and gathers the information for the first visible item and the total item count
            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNoErrors= !isError //this must be satisfy for pagination to occur
            val isNotLoadingAndIsNotLastPage= !isLoading && !isLastPage //it also
            val isAtLastItem=firstVisibleItemPosition + visibleItemCount >=totalItemCount
            val isNotAtBeginning=firstVisibleItemPosition>=0
            val isTotalMoreThanVisible= totalItemCount>= Contents.QUERY_PER_PAGE
            val shouldPaginate= isNoErrors && isNotLoadingAndIsNotLastPage && isAtLastItem
            //based on above conditions down if decides for pagination to happen
            if(shouldPaginate){
                newsViewModel.getHeadlines("in") //if true then it gives the headline
                isScrolling=false //updates the state by setting up false for next pagination
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){ //it checks that user is scrolling the screen or not
                isScrolling=true
            }
        }

    }
    private fun setUpHeadlinesRecycler(){ //se
        newsAdapter= NewsAdapter() //bind data to recycler view
        binding.recyclerView.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity) //it will get rearranged in vertical scroll manner
            addOnScrollListener(this@HeadlinesFragment.scrollListener) //it sets for pagination
        }

    }



}