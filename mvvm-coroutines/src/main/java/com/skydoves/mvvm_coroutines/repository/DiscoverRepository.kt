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
import com.skydoves.entity.database.TvDao
import com.skydoves.network.service.TheDiscoverService
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class DiscoverRepository constructor(
  private val discoverService: TheDiscoverService,
  private val movieDao: MovieDao,
  private val tvDao: TvDao
) : Repository {

  init {
    Timber.d("Injection DiscoverRepository")
  }

  @WorkerThread
  fun loadMovies(page: Int, onSuccess: () -> Unit) = flow {
    var movies = movieDao.getMovieList(page)
    if (movies.isEmpty()) {
      val response = discoverService.fetchDiscoverMovie(page)
      response.suspendOnSuccess {
        data.whatIfNotNull { data ->
          movies = data.results
          movies.forEach { it.page = page }
          movieDao.insertMovieList(movies)
          emit(movies)
          onSuccess()
        }
      }
    } else {
      emit(movies)
      onSuccess()
    }
  }.flowOn(Dispatchers.IO)

  @WorkerThread
  fun loadTvs(page: Int, onSuccess: () -> Unit) = flow {
    var tvs = tvDao.getTvList(page)
    if (tvs.isEmpty()) {
      val response = discoverService.fetchDiscoverTv(page)
      response.suspendOnSuccess {
        data.whatIfNotNull { data ->
          tvs = data.results
          tvs.forEach { it.page = page }
          tvDao.insertTv(tvs)
          emit(tvs)
          onSuccess()
        }
      }
    } else {
      emit(tvs)
      onSuccess()
    }
  }.flowOn(Dispatchers.IO)

  fun getFavouriteMovieList() = movieDao.getFavouriteMovieList()

  fun getFavouriteTvList() = tvDao.getFavouriteTvList()
}
