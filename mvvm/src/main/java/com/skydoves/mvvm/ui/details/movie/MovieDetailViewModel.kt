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

package com.skydoves.mvvm.ui.details.movie

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.entities.Movie
import com.skydoves.mvvm.repository.MovieRepository
import javax.inject.Inject
import timber.log.Timber

class MovieDetailViewModel @Inject
constructor(private val movieRepository: MovieRepository) : ViewModel() {

  private val movieIdLiveData: MutableLiveData<Int> = MutableLiveData()
  val keywordListLiveData: LiveData<List<Keyword>>
  val videoListLiveData: LiveData<List<Video>>
  val reviewListLiveData: LiveData<List<Review>>
  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  private lateinit var movie: Movie
  val favourite = ObservableBoolean()

  init {
    Timber.d("Injection MovieDetailViewModel")

    this.keywordListLiveData = movieIdLiveData.switchMap { id ->
      movieRepository.loadKeywordList(id) { toastLiveData.postValue(it) }
    }

    this.videoListLiveData = movieIdLiveData.switchMap { id ->
      movieRepository.loadVideoList(id) { toastLiveData.postValue(it) }
    }

    this.reviewListLiveData = movieIdLiveData.switchMap { id ->
      movieRepository.loadReviewsList(id) { toastLiveData.postValue(it) }
    }
  }

  fun postMovieId(id: Int) {
    this.movieIdLiveData.postValue(id)
    this.movie = movieRepository.getMovie(id)
    this.favourite.set(this.movie.favourite)
  }

  fun getMovie() = this.movie

  fun onClickedFavourite(movie: Movie) =
    favourite.set(movieRepository.onClickFavourite(movie))
}
