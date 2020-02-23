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

package com.skydoves.common_ui.viewholders

import android.view.View
import androidx.core.view.ViewCompat
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.common_ui.databinding.ItemMovieBinding
import com.skydoves.entity.entities.Movie

/** MovieListViewHolder is a viewHolder class for binding a [Movie] item. */
class MovieListViewHolder(
  view: View,
  private val delegate: Delegate
) : BaseViewHolder(view) {

  interface Delegate {
    fun onItemClick(view: View, movie: Movie)
  }

  private lateinit var movie: Movie
  private val binding by bindings<ItemMovieBinding>(view)

  override fun bindData(data: Any) {
    if (data is Movie) {
      movie = data
      binding.apply {
        ViewCompat.setTransitionName(binding.itemMovieContainer, data.title)
        movie = data
        palette = itemPosterPalette
        executePendingBindings()
      }
    }
  }

  override fun onClick(v: View?) = delegate.onItemClick(binding.itemMovieContainer, movie)

  override fun onLongClick(v: View?) = false
}
