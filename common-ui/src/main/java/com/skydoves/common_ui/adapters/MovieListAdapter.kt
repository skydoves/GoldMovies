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

package com.skydoves.common_ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.Bindable
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.bindables.BindingRecyclerViewAdapter
import com.skydoves.bindables.binding
import com.skydoves.common_ui.R
import com.skydoves.common_ui.databinding.ItemMovieBinding
import com.skydoves.entity.entities.Movie

/** MovieListAdapter is an adapter class for binding [Movie] items. */
class MovieListAdapter(
  private val delegate: Delegate
) : BindingRecyclerViewAdapter<MovieListAdapter.MovieListViewHolder>() {

  private val items: ArrayList<Movie> = arrayListOf()

  @get:Bindable
  val isEmpty: Boolean
    get() = items.isEmpty()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
    val binding = parent.binding<ItemMovieBinding>(R.layout.item_movie)
    return MovieListViewHolder(binding).apply {
      binding.root.setOnClickListener {
        val position =
          adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
        delegate.onItemClick(binding.root, items[position])
      }
    }
  }

  override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
    val data = items[position]
    with(holder.binding) {
      ViewCompat.setTransitionName(itemMovieContainer, data.title)
      movie = data
      palette = itemPosterPalette
      executePendingBindings()
    }
  }

  fun addMovieList(movieItemList: List<Movie>) {
    items.clear()
    items.addAll(movieItemList)
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = items.size

  interface Delegate {
    fun onItemClick(view: View, movie: Movie)
  }

  class MovieListViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)
}
