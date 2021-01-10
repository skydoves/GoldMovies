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

package com.skydoves.mvvm_coroutines.ui.main

import androidx.annotation.MainThread
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Person
import com.skydoves.entity.entities.Tv
import com.skydoves.mvvm_coroutines.base.LiveCoroutinesViewModel
import com.skydoves.mvvm_coroutines.repository.DiscoverRepository
import com.skydoves.mvvm_coroutines.repository.PeopleRepository
import com.skydoves.network.extensions.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class MainActivityViewModel constructor(
  private val discoverRepository: DiscoverRepository,
  private val peopleRepository: PeopleRepository
) : LiveCoroutinesViewModel() {

  private var moviePageStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)
  val movieListLiveData: LiveData<List<Movie>?>

  private var tvPageStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)
  val tvListLiveData: LiveData<List<Tv>?>

  private var peoplePageStateFlow: MutableStateFlow<Int> = MutableStateFlow(0)
  val peopleLiveData: LiveData<List<Person>>

  val isLoading: ObservableBoolean = ObservableBoolean(false)

  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  init {
    Timber.d("injection MainActivityViewModel")

    this.movieListLiveData = this.moviePageStateFlow.asLiveData().switchMap { page ->
      launchOnViewModelScope {
        isLoading.set(true)
        this.discoverRepository.loadMovies(page) {
          isLoading.set(false)
        }.asLiveData()
      }
    }

    this.tvListLiveData = this.tvPageStateFlow.asLiveData().switchMap { page ->
      launchOnViewModelScope {
        isLoading.set(true)
        this.discoverRepository.loadTvs(page) {
          isLoading.set(false)
        }.asLiveData()
      }
    }

    this.peopleLiveData = this.peoplePageStateFlow.asLiveData().switchMap { page ->
      launchOnViewModelScope {
        isLoading.set(true)
        this.peopleRepository.loadPeople(page) {
          isLoading.set(false)
        }.asLiveData()
      }
    }
  }

  @MainThread
  fun postMoviePage(page: Int) = this.moviePageStateFlow.setValue(page)

  @MainThread
  fun postTvPage(page: Int) = this.tvPageStateFlow.setValue(page)

  @MainThread
  fun postPeoplePage(page: Int) = this.peoplePageStateFlow.setValue(page)

  fun getFavouriteMovieList() = this.discoverRepository.getFavouriteMovieList()

  fun getFavouriteTvList() = this.discoverRepository.getFavouriteTvList()
}
