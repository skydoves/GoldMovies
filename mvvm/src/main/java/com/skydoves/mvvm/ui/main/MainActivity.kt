/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.mvvm.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.skydoves.common_ui.adapters.MovieFavouriteListAdapter
import com.skydoves.common_ui.adapters.TvFavouriteListAdapter
import com.skydoves.common_ui.customs.FlourishFactory
import com.skydoves.common_ui.viewholders.MovieFavouriteListViewHolder
import com.skydoves.common_ui.viewholders.TvFavouriteListViewHolder
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Tv
import com.skydoves.mvvm.R
import com.skydoves.mvvm.base.ViewModelActivity
import com.skydoves.mvvm.ui.details.movie.MovieDetailActivity
import com.skydoves.mvvm.ui.details.tv.TvDetailActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_favourites.view.*
import kotlinx.android.synthetic.main.toolbar_home.view.*
import ss.anoop.awesomenavigation.OnNavigationSelectedListener

class MainActivity : ViewModelActivity(), HasSupportFragmentInjector,
  MovieFavouriteListViewHolder.Delegate, TvFavouriteListViewHolder.Delegate {

  @Inject
  lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
  private val viewModel by viewModel<MainActivityViewModel>()

  private val adapterMovieList = MovieFavouriteListAdapter(this)
  private val adapterTvList = TvFavouriteListAdapter(this)

  private val flourish by lazy {
    FlourishFactory.create(parentView, R.layout.layout_favourites)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    initializeUI()
  }

  private fun initializeUI() {
    main_viewpager.adapter = MainPagerAdapter(supportFragmentManager)
    main_viewpager.offscreenPageLimit = 3
    main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) = Unit
      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit
      override fun onPageSelected(position: Int) {
        main_bottom_navigation.selectItem(position)
      }
    })
    main_bottom_navigation.setOnNavigationSelectedListener(object : OnNavigationSelectedListener {

      override fun onSelectNavigation(id: Int, position: Int) {
        when (id) {
          R.id.action_one -> main_viewpager.currentItem = 0
          R.id.action_two -> main_viewpager.currentItem = 1
          R.id.action_three -> main_viewpager.currentItem = 2
        }
      }

      override fun onReselectNavigation(id: Int, position: Int) {}
    })

    this.flourish.flourishView.recyclerViewMovies.adapter = adapterMovieList
    this.flourish.flourishView.recyclerViewTvs.adapter = adapterTvList
    this.flourish.flourishView.back.setOnClickListener { flourish.dismiss() }
    main_toolbar.toolbar_favourite.setOnClickListener {
      refreshFavourites()
      this.flourish.show()
    }
  }

  override fun onResume() {
    super.onResume()
    refreshFavourites()
  }

  private fun refreshFavourites() {
    this.adapterMovieList.addMovieList(viewModel.getFavouriteMovieList())
    this.adapterTvList.addTvList(viewModel.getFavouriteTvList())
  }

  override fun onItemClick(movie: Movie) =
    MovieDetailActivity.startActivityModel(this, movie.id)

  override fun onItemClick(tv: Tv) =
    TvDetailActivity.startActivityModel(this, tv.id)

  override fun onBackPressed() {
    if (this.flourish.isShowing()) {
      this.flourish.dismiss()
    } else {
      super.onBackPressed()
    }
  }

  override fun supportFragmentInjector() = fragmentInjector
}
