package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import kotlinx.android.synthetic.main.activity_news.*

/*
* STEP 0 - renaming MainActivity to NewsActivity (renaming layout to activity_news),
*          creating packages for MVVM structure
*
* STEP 1 - SETTING UP NAVIGATION (navigating between UI fragments)
*    1.1 - add newsNavHostFragment to layout, that uses navGraph (not yet created)
*    1.2 - create all fragment classes (ArticleFragment, BreakingNewsFragment, etc.)
*    1.3 - create Navigation Graph (res -> new resource file -> Navigation -> news_nav_graph)
*         and define transition from fragments (design tab of xml file)
*    1.4 - create bottom_navigation_menu.xml
*    1.5 - in NewsActivity.kt connect bottomNavigationView with navigation components
*
* STEP 2 - SETTING UP RETROFIT (for making network requests)
*    2.1 - creating response objects that we need - data classes from JSON that our API returns
*          (copy a JSON from our API - newsapi.org -> new Kotlin Data class from JSON and as a result
*          we get 3 data classes - NewsResponse (this name I entered) and Article and Source (created
*          automatically from JSON by inferring)
*    2.2 - create API interface - NewsAPI.kt
*    2.3 - create RetrofitInstance - singleton class that allows us to make requests from anywhere in our code
*
* STEP 3 - SETTING UP ROOM AND DATA ACCESS OBJECT (DAO) - for saving articles to local database
*    3.1 - Annotate Article class with @Entity (that way it will be a table in a room data base)
*    3.2 - Add id parameter and @PrimaryKey(autoGenerate = true) parameter to Article data class
*    3.3 - Creating DAO interface - ArticleDao.kt, which is like API, but for our local database,
*          this interface defines functions to insert/updateR, retrieve, delete articles from our local DB.
*
* STEP 4 - SETTING UP ROOM DATABASE CLASS AND ROOM TYPE CONVERTER
*    4.1 - create ArticleDatabase.kt abstract class
*    4.2 - create Converters.kt class - Room only handles primitive data types (int, string, etc),
*          but our Article class has a source parameter of a custom class Source, which we need to
*          convert to string using TypeConverter (and also visa versa from String to Source)
*    4.3 - add converters from 4.2 to ArticleDatabase.kt via annotation
*
* STEP 5 - SETTING UP RECYCLERVIEW ADAPTER (WITH DIFFUTIL)
*    5.1 - create NewsAdapter.kt class in adapters package
*    5.2 - add AsyncListDiffer to NewsAdapter.kt
*    5.3 - implement RecyclerView functions (ctrl + i)
* */

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // 1.5 - connecting bottom navigation view with navigation components
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
