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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Person
import com.skydoves.entity.entities.Tv
import com.skydoves.mvvm_coroutines.base.LiveCoroutinesViewModel
import com.skydoves.mvvm_coroutines.repository.DiscoverRepository
import com.skydoves.mvvm_coroutines.repository.PeopleRepository
import timber.log.Timber

class MainActivityViewModel
constructor(
  private val discoverRepository: DiscoverRepository,
  private val peopleRepository: PeopleRepository
) : LiveCoroutinesViewModel() {

  private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
  val movieListLiveData: LiveData<List<Movie>>

  private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData()
  val tvListLiveData: LiveData<List<Tv>>

  private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData()
  val peopleLiveData: LiveData<List<Person>>

  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  init {
    Timber.d("injection MainActivityViewModel")

    this.movieListLiveData = this.moviePageLiveData.switchMap { page ->
      launchOnViewModelScope {
        this.discoverRepository.loadMovies(page) { this.toastLiveData.postValue(it) }
      }
    }

    this.tvListLiveData = this.tvPageLiveData.switchMap { page ->
      launchOnViewModelScope {
        this.discoverRepository.loadTvs(page) { this.toastLiveData.postValue(it) }
      }
    }

    this.peopleLiveData = this.peoplePageLiveData.switchMap { page ->
      launchOnViewModelScope {
        this.peopleRepository.loadPeople(page) { this.toastLiveData.postValue(it) }
      }
    }
  }

  fun postMoviePage(page: Int) = this.moviePageLiveData.postValue(page)

  fun postTvPage(page: Int) = this.tvPageLiveData.postValue(page)

  fun postPeoplePage(page: Int) = this.peoplePageLiveData.postValue(page)

  fun getFavouriteMovieList() = this.discoverRepository.getFavouriteMovieList()

  fun getFavouriteTvList() = this.discoverRepository.getFavouriteTvList()

  fun isLoading() = this.discoverRepository.isLoading || this.peopleRepository.isLoading
}
