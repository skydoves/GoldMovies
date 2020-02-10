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
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.main_bottom_navigation
import kotlinx.android.synthetic.main.activity_main.main_toolbar
import kotlinx.android.synthetic.main.activity_main.main_viewpager
import kotlinx.android.synthetic.main.activity_main.parentView
import kotlinx.android.synthetic.main.layout_favourites.view.back
import kotlinx.android.synthetic.main.layout_favourites.view.recyclerViewMovies
import kotlinx.android.synthetic.main.layout_favourites.view.recyclerViewTvs
import kotlinx.android.synthetic.main.toolbar_home.view.toolbar_favourite

class MainActivity : ViewModelActivity(), HasAndroidInjector,
  MovieFavouriteListViewHolder.Delegate, TvFavouriteListViewHolder.Delegate {

  @Inject
  lateinit var fragmentInjector: DispatchingAndroidInjector<Any>
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
    with(main_viewpager) {
      adapter = MainPagerAdapter(supportFragmentManager)
      offscreenPageLimit = 3
      addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit
        override fun onPageScrolled(
          position: Int,
          positionOffset: Float,
          positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) {
          main_bottom_navigation.menu.getItem(position).isChecked = true
        }
      })
      main_bottom_navigation.setOnNavigationItemSelectedListener {
        when (it.itemId) {
          R.id.action_one -> currentItem = 0
          R.id.action_two -> currentItem = 1
          R.id.action_three -> currentItem = 2
        }
        true
      }
    }

    with(flourish.flourishView) {
      recyclerViewMovies.adapter = adapterMovieList
      recyclerViewTvs.adapter = adapterTvList
      back.setOnClickListener { flourish.dismiss() }
      main_toolbar.toolbar_favourite.setOnClickListener {
        refreshFavourites()
        flourish.show()
      }
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

  override fun androidInjector(): AndroidInjector<Any> {
    return fragmentInjector
  }
}
