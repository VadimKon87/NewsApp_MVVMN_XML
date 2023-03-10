package com.androiddevs.mvvmnewsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
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
*    7.3 - in BreakingNewsFragment.kt create newsAdapter instance
*    7.4 - in NewsViewModel make API request (call getBreakingNews() )
*    7.5 - add internet permission in manifest
*
*          APP CRASHING ON LAUNCH - SOLVED
*          in activity_news.xml replace <fragment> with FragmentContainer and change code at
*          bottom of this file
*
* STEP 8 - SETTING UP SEARCH
*    8.1 - in NewsRepository.kt create fun searchNews()
*    8.2 - in NewsViewModel create fun handleSearchNewsResponse() and searchNews()
*    8.3 - in SearchNewsFragment setup recyclerview (like in 7.3)
*    8.4 - add delay before search starts (so we can finish writing the search request)
*
* STEP 9 - SHOWING ARTICLES IN WEBVIEW
*    9.1 - in Article.kt add that Article class inherits from Serializable
*    9.2 - in news_nav_graph.xml in visual mode add argument to articleFragment
*    9.3 - in every fragment class (except article) set a click listener on the "newsAdapter" object,
*          so when we click it we transfer to article fragment
*    9.4 - in ArticleFragment.kt
*    9.+ - Fixed NullPointerException (override hashCode() in Article.kt class)
*
* STEP 10 - ADDING AND DELETING ARTICLE IN ROOM DATABASE
*    10.1 - in NewsRepository.kt add the functions from interface - ArticleDAO.kt
*    10.2 - in NewsViewModel.kt call the above functions
*    10.3 - in ArticleFragment.kt add ability to save articles
*    10.4 - in SavedNewsFragment.kt observe the changes in the database
*
* STEP 11 - PAGINATION WITH RETROFIT (SCROLLING)
*    11.1 - in NewsViewModel.kt add code
*    11.2 - in NewsResponse.dt data class change parameter articles to Mutable list
*    11.3 - in BreakingNewsFragment.kt add code
*    11.4 - in SearchNewsFragment.kt add same code as previous step
*
* BUG FIX FROM STEP 10 - (app can throw NPE when attempt to save article with null parameters)
*    10.5 - in Article.kt make parameters nullable
*    10.6 - in NewsAdapter.kt add safe call
*
* STEP 12 - NO INTERNET CHECKING (app crashes if no internet)
*    12.1 - in NewsViewModel.kt change class that we inherit from ViewModel() to AndroidViewModel()
*    12.2 - create Application.kt class - required as parameter for AndroidViewModel()
*           and add it to Manifest
*    12.3 - in NewsViewModel.kt create function that checks if there is an internet connection
*    12.4 - in NewsViewModel.kt create fun to catch exceptions
*    12.5 - in NewsViewModelProviderFactory.kt add app parameter and in this file add application
*           parameter to val viewModelProviderFactory
*    12.6 - in BreakingNewsFragment and SearchNewsFragment add Toast to display No internet error
* */

class NewsActivity : AppCompatActivity() {

    // step 6.4
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //step  6.4 (+ 12.5)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository) //12.5 add application
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]


        // 1.5 - connecting bottom navigation view with navigation components
        //bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

        //replaced above code with this after replacing <fragment> with FragmentContainer
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
    }
}
