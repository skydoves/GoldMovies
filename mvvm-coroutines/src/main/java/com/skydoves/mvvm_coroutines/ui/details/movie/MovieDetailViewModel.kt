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

package com.skydoves.mvvm_coroutines.ui.details.movie

import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.entities.Movie
import com.skydoves.mvvm_coroutines.base.LiveCoroutinesViewModel
import com.skydoves.mvvm_coroutines.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class MovieDetailViewModel constructor(
  private val movieRepository: MovieRepository
) : LiveCoroutinesViewModel() {

  private val movieIdStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)
  val keywordListLiveData: LiveData<List<Keyword>?>
  val videoListLiveData: LiveData<List<Video>?>
  val reviewListLiveData: LiveData<List<Review>?>

  private lateinit var movie: Movie
  val favourite = ObservableBoolean()

  @get:Bindable
  var isLoading: Boolean = false
    private set

  init {
    Timber.d("Injection MovieDetailViewModel")

    this.keywordListLiveData = movieIdStateFlow.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading = true
        movieRepository.loadKeywordList(id) {
          isLoading = false
        }.asLiveData()
      }
    }

    this.videoListLiveData = movieIdStateFlow.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading = true
        movieRepository.loadVideoList(id) {
          isLoading = false
        }.asLiveData()
      }
    }

    this.reviewListLiveData = movieIdStateFlow.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading = true
        movieRepository.loadReviewsList(id) {
          isLoading = false
        }.asLiveData()
      }
    }
  }

  fun postMovieId(id: Int) {
    this.movieIdStateFlow.value = id
    this.movie = movieRepository.getMovie(id)
    this.favourite.set(movie.favourite)
  }

  fun getMovie() = this.movie

  fun onClickedFavourite(movie: Movie) =
    favourite.set(movieRepository.onClickFavourite(movie))
}
