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

package com.skydoves.mvvm_coroutines.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator
import com.skydoves.common_ui.adapters.MovieListAdapter
import com.skydoves.common_ui.extensions.toast
import com.skydoves.common_ui.viewholders.MovieListViewHolder
import com.skydoves.entity.entities.Movie
import com.skydoves.mvvm_coroutines.R
import com.skydoves.mvvm_coroutines.base.DatabindingFragment
import com.skydoves.mvvm_coroutines.databinding.FragmentMovieBinding
import com.skydoves.mvvm_coroutines.ui.details.movie.MovieDetailActivity
import kotlinx.android.synthetic.main.fragment_movie.recyclerView
import org.koin.android.viewmodel.ext.android.viewModel

class MovieListFragment : DatabindingFragment(), MovieListViewHolder.Delegate {

  private val viewModel: MainActivityViewModel by viewModel()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return binding<FragmentMovieBinding>(
      inflater, R.layout.fragment_movie, container).apply {
      viewModel = this@MovieListFragment.viewModel
      lifecycleOwner = this@MovieListFragment
      adapter = MovieListAdapter(this@MovieListFragment)
    }.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initializeUI()
    loadMore(page = 1)
    observeMessages()
  }

  private fun initializeUI() {
    RecyclerViewPaginator(
      recyclerView = recyclerView,
      isLoading = { viewModel.isLoading() },
      loadMore = { loadMore(it) },
      onLast = { false }
    ).apply {
      threshold = 4
      currentPage = 1
    }
  }

  private fun loadMore(page: Int) = this.viewModel.postMoviePage(page)

  override fun onItemClick(view: View, movie: Movie) =
    MovieDetailActivity.startActivityModel(requireContext(), view, movie)

  private fun observeMessages() =
    this.viewModel.toastLiveData.observe(this) { context?.toast(it) }
}
