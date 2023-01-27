package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import kotlinx.android.synthetic.main.activity_news.*

// TODO implement ViewBinding, remove deprecated extensions plugin

/*
* Step 0 - renaming MainActivity to NewsActivity (renaming layout to activity_news),
*          adding UI package, adding fragments package inside ui package
*
* Step 1 - SETTING UP NAVIGATION (navigating between UI fragments)
*   1.1 - add newsNavHostFragment to layout, that uses navGraph (not yet created)
*   1.2 - create all fragment classes (ArticleFragment, BreakingNewsFragment, etc.)
*   1.3 - create Navigation Graph (res -> new resource file -> Navigation -> news_nav_graph)
*         and define transition from fragments (design tab of xml file)
*   1.4 - create bottom_navigation_menu.xml
*   1.5 - in NewsActivity.kt connect bottomNavigationView with navigation components
*
* Step 2 - SETTING UP RETROFIT (for making network requests)
*   2.1 - creating response objects that we need - data classes from JSON that our API returns
*         (copy a JSON from our API - newsapi.org -> new Kotlin Data class from JSON and as a result
*         we get 3 data classes - NewsResponse (this name I entered) and Article and Source (created
*         automatically from JSON by inferring)
* */

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // 1.5 - connecting bottom navigation view with navigation components
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
