package com.project.QuickNews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.QuickNews.R
import com.project.QuickNews.adpters.NewsAdapter
import com.project.QuickNews.databinding.FragmentFavoritesBinding
import com.project.QuickNews.ui.MainActivity
import com.project.QuickNews.ui.NewsViewModel


class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavoritesBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentFavoritesBinding.bind(view)

        newsViewModel=(activity as MainActivity).newsViewModel
        setUpFavoriteRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_favoritesFragment_to_articleFragment, bundle)
        }

        val itemCallBackHelper=object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN
        , ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition
                val article=newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        newsViewModel.addToFavorite(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemCallBackHelper).apply {
            attachToRecyclerView(binding.recFav)
        }

        newsViewModel.getFavoriteNews().observe(viewLifecycleOwner, Observer {articles->
            newsAdapter.differ.submitList(articles)

        })
    }
    private fun setUpFavoriteRecycler(){
        newsAdapter= NewsAdapter() //bind data to recycler view
        binding.recFav.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity) //it will get rearranged in vertical scroll manner

        }
    }

}