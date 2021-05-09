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
import com.skydoves.entity.database.TvDao
import com.skydoves.network.service.TheDiscoverService
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
class DiscoverRepository @Inject constructor(
  private val discoverService: TheDiscoverService,
  private val movieDao: MovieDao,
  private val tvDao: TvDao
) : Repository {

  init {
    Timber.d("Injection DiscoverRepository")
  }

  fun loadMovies(page: Int, onSuccess: () -> Unit) = flow {
    var movies = movieDao.getMovieList(page)
    if (movies.isEmpty()) {
      val response = discoverService.fetchDiscoverMovie(page)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          movies = it.results
          movies.forEach { it.page = page }
          movieDao.insertMovieList(movies)
          emit(movies)
        }
      }
    } else {
      emit(movies)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun loadTvs(page: Int, onSuccess: () -> Unit) = flow {
    var tvs = tvDao.getTvList(page)
    if (tvs.isEmpty()) {
      val response = discoverService.fetchDiscoverTv(page)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          tvs = it.results
          tvs.forEach { it.page = page }
          tvDao.insertTv(tvs)
          emit(tvs)
        }
      }
    } else {
      emit(tvs)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun getFavouriteMovieList() = movieDao.getFavouriteMovieList()

  fun getFavouriteTvList() = tvDao.getFavouriteTvList()
}
