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

package com.skydoves.mvvm.repository

import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.entities.Movie
import com.skydoves.network.service.MovieService
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber

@Singleton
class MovieRepository @Inject constructor(
  private val movieService: MovieService,
  private val movieDao: MovieDao
) : Repository {

  init {
    Timber.d("Injection MovieRepository")
  }

  fun loadKeywordList(id: Int, onSuccess: () -> Unit) = flow {
    val movie = movieDao.getMovie(id)
    var keywords = movie.keywords
    if (keywords.isNullOrEmpty()) {
      val response = movieService.fetchKeywords(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          keywords = it.keywords
          movie.keywords = keywords
          movieDao.updateMovie(movie)
          emit(keywords)
        }
      }
    } else {
      emit(keywords)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun loadVideoList(id: Int, onSuccess: () -> Unit) = flow {
    val movie = movieDao.getMovie(id)
    var videos = movie.videos
    if (videos.isNullOrEmpty()) {
      val response = movieService.fetchVideos(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          videos = it.results
          movie.videos = videos
          movieDao.updateMovie(movie)
          emit(videos)
        }
      }
    } else {
      emit(videos)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun loadReviewsList(id: Int, onSuccess: () -> Unit) = flow {
    val movie = movieDao.getMovie(id)
    var reviews = movie.reviews
    if (reviews.isNullOrEmpty()) {
      val response = movieService.fetchReviews(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          reviews = it.results
          movie.reviews = reviews
          movieDao.updateMovie(movie)
          emit(reviews)
        }
      }
    } else {
      emit(reviews)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun getMovie(id: Int) = movieDao.getMovie(id)

  fun onClickFavourite(movie: Movie): Boolean {
    movie.favourite = !movie.favourite
    movieDao.updateMovie(movie)
    return movie.favourite
  }
}
