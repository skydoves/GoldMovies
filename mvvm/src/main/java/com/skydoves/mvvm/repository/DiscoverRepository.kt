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

import androidx.lifecycle.MutableLiveData
import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Tv
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.TheDiscoverClient
import com.skydoves.network.message
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class DiscoverRepository @Inject constructor(
  private val discoverClient: TheDiscoverClient,
  private val movieDao: MovieDao,
  private val tvDao: TvDao
) : Repository {

  override var isLoading: Boolean = false

  init {
    Timber.d("Injection DiscoverRepository")
  }

  fun loadMovies(page: Int, error: (String) -> Unit): MutableLiveData<List<Movie>> {
    val liveData = MutableLiveData<List<Movie>>()
    var movies = movieDao.getMovieList(page)
    if (movies.isEmpty()) {
      this.isLoading = true
      discoverClient.fetchDiscoverMovie(page) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              movies = data.results
              movies.forEach { it.page = page }
              liveData.postValue(movies)
              movieDao.insertMovieList(movies)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(movies)
    return liveData
  }

  fun loadTvs(page: Int, error: (String) -> Unit): MutableLiveData<List<Tv>> {
    val liveData = MutableLiveData<List<Tv>>()
    var tvs = tvDao.getTvList(page)
    if (tvs.isEmpty()) {
      this.isLoading = true
      discoverClient.fetchDiscoverTv(page) { response ->
        this.isLoading = false
        when (response) {
          is ApiResponse.Success -> {
            response.data?.let { data ->
              tvs = data.results
              tvs.forEach { it.page = page }
              liveData.postValue(tvs)
              tvDao.insertTv(tvs)
            }
          }
          is ApiResponse.Failure.Error -> error(response.message())
          is ApiResponse.Failure.Exception -> error(response.message())
        }
      }
    }
    liveData.postValue(tvs)
    return liveData
  }

  fun getFavouriteMovieList() = movieDao.getFavouriteMovieList()

  fun getFavouriteTvList() = tvDao.getFavouriteTvList()
}
