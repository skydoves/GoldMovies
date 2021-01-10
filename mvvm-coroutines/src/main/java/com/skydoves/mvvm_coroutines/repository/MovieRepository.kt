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

package com.skydoves.mvvm_coroutines.repository

import androidx.annotation.WorkerThread
import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.entities.Movie
import com.skydoves.network.service.MovieService
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class MovieRepository constructor(
  private val movieService: MovieService,
  private val movieDao: MovieDao
) : Repository {

  init {
    Timber.d("Injection MovieRepository")
  }

  @WorkerThread
  fun loadKeywordList(id: Int, success: () -> Unit) = flow {
    val movie = movieDao.getMovie(id)
    var keywords = movie.keywords
    if (keywords.isNullOrEmpty()) {
      val response = movieService.fetchKeywords(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          keywords = data!!.keywords
          movie.keywords = keywords
          movieDao.updateMovie(movie)
          emit(keywords)
          success()
        }
      }
    } else {
      emit(keywords)
      success()
    }
  }.flowOn(Dispatchers.IO)

  @WorkerThread
  fun loadVideoList(id: Int, success: () -> Unit) = flow {
    val movie = movieDao.getMovie(id)
    var videos = movie.videos
    if (videos.isNullOrEmpty()) {
      val response = movieService.fetchVideos(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          videos = data!!.results
          movie.videos = videos
          movieDao.updateMovie(movie)
          emit(videos)
          success()
        }
      }
    } else {
      emit(videos)
      success()
    }
  }.flowOn(Dispatchers.IO)

  @WorkerThread
  fun loadReviewsList(id: Int, success: () -> Unit) = flow {
    val movie = movieDao.getMovie(id)
    var reviews = movie.reviews
    if (reviews.isNullOrEmpty()) {
      val response = movieService.fetchReviews(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          reviews = data!!.results
          movie.reviews = reviews
          movieDao.updateMovie(movie)
          emit(reviews)
          success()
        }
      }
    } else {
      emit(reviews)
      success()
    }
  }.flowOn(Dispatchers.IO)

  @WorkerThread
  fun getMovie(id: Int) = movieDao.getMovie(id)

  fun onClickFavourite(movie: Movie): Boolean {
    movie.favourite = !movie.favourite
    movieDao.updateMovie(movie)
    return movie.favourite
  }
}
