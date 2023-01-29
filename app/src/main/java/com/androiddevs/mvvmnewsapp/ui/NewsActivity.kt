package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.repository.NewsRepository
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
*
* STEP 6 - SETTING UP ARCHITECTURAL SKELETON OF APP (VIEW MODEL, REPOSITORY, VIEW MODEL PROVIDER
*          FACTORY AND WRAPPER CLASS AROUND OUR NETWORK RESPONSES)
*    6.1 - create NewsViewModel.kt class in ui package
*    6.2 - create NewsRepository.kt class in repository package
*    6.3 - create NewsViewModelProviderFactory.kt in ui package (we need factory because our
*          viewmodel takes parameters in the constructor and by default viewmodelprovider can only
*          create it without parameters)
*    6.4 - in NewsActivity.kt (this file) create instances of above classes
*    6.5 - in each of fragment classes add viewModel and override fun onViewCreated
*    6.6 - create Resource class in util package (class recommended by Google to wrap around network
*          responses. It's a Generic class and useful for differentiating between successful and
*          error responses and also helps us to handle loading state)
*
* STEP 7 - MAKING NETWORK REQUEST, HANDLING BREAKING NEWS RESPONSE AND DISPLAYING RESPONSE IN RECYCLER VIEW
*    7.1 - in NewsRepository.kt create fun to get breaking new from API
*    7.2 - in NewsViewModel.kt create val breakingNews (a LiveData object) and fun getBreakingNews
*    7.3 - in BreakingNews.kt fragment create newsAdapter instance
*    7.4 - in NewsViewModel make API request (call getBreakingNews() )
*    7.5 - add internet permission in manifest
* */

class NewsActivity : AppCompatActivity() {

    // step 6.4
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //step  6.4
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]


        // 1.5 - connecting bottom navigation view with navigation components
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
    }
}
