package com.project.infininews.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.project.infininews.R
import com.project.infininews.databinding.FragmentArticleBinding
import com.project.infininews.ui.MainActivity
import com.project.infininews.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var newsViewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentArticleBinding.bind(view)

        newsViewModel=(activity as MainActivity).newsViewModel
        val article=args.article

        binding.wbview.apply {
            webViewClient= WebViewClient()
            article.url.let {
                if (it != null) {
                    loadUrl(it)
                }
            }
        }

        binding.fabButton.setOnClickListener {
            newsViewModel.addToFavorite(article)
            Snackbar.make(view,"Added to favorites", Snackbar.LENGTH_SHORT).show()
        }
    }

}