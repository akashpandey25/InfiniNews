package com.project.QuickNews.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.project.QuickNews.R
import com.project.QuickNews.databinding.FragmentArticleBinding
import com.project.QuickNews.ui.MainActivity
import com.project.QuickNews.ui.NewsViewModel

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