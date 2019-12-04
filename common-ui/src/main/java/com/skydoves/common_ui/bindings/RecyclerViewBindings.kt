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

package com.skydoves.common_ui.bindings

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.common_ui.PosterPath
import com.skydoves.common_ui.adapters.MovieListAdapter
import com.skydoves.common_ui.adapters.PeopleAdapter
import com.skydoves.common_ui.adapters.ReviewListAdapter
import com.skydoves.common_ui.adapters.TvListAdapter
import com.skydoves.common_ui.adapters.VideoListAdapter
import com.skydoves.common_ui.extensions.visible
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Person
import com.skydoves.entity.entities.Tv
import com.skydoves.whatif.whatIfNotNullOrEmpty

@BindingAdapter("adapterMovieList")
fun bindAdapterMovieList(view: RecyclerView, movies: List<Movie>?) {
  movies.whatIfNotNullOrEmpty {
    val adapter = view.adapter as? MovieListAdapter
    adapter?.addMovieList(it)
  }
}

@BindingAdapter("adapterTvList")
fun bindAdapterTvList(view: RecyclerView, tvs: List<Tv>?) {
  tvs.whatIfNotNullOrEmpty {
    val adapter = view.adapter as? TvListAdapter
    adapter?.addTvList(it)
  }
}

@BindingAdapter("adapterPersonList")
fun bindAdapterPersonList(view: RecyclerView, people: List<Person>?) {
  people.whatIfNotNullOrEmpty {
    val adapter = view.adapter as? PeopleAdapter
    adapter?.addPeople(it)
  }
}

@BindingAdapter("adapterVideoList")
fun bindAdapterVideoList(recyclerView: RecyclerView, videos: List<Video>?) {
  videos.whatIfNotNullOrEmpty {
    val adapter = VideoListAdapter()
    adapter.addVideoList(it)
    recyclerView.adapter = adapter
    recyclerView.visible()
  }
}

@BindingAdapter("adapterReviewList")
fun bindAdapterReviewList(recyclerView: RecyclerView, reviews: List<Review>?) {
  reviews.whatIfNotNullOrEmpty {
    val adapter = ReviewListAdapter()
    adapter.addReviewList(it)
    recyclerView.adapter = adapter
    recyclerView.isNestedScrollingEnabled = false
    recyclerView.setHasFixedSize(true)
    recyclerView.visible()
  }
}

@BindingAdapter("onVideoItemClick")
fun onVideoItemClick(view: View, video: Video) {
  view.setOnClickListener {
    val playVideoIntent = Intent(
      Intent.ACTION_VIEW, Uri.parse(PosterPath.getYoutubeVideoPath(video.key)))
    view.context.startActivity(playVideoIntent)
  }
}
