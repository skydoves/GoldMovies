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

package com.skydoves.mvvm.ui.main

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Person
import com.skydoves.entity.entities.Tv
import com.skydoves.mvvm.repository.DiscoverRepository
import com.skydoves.mvvm.repository.PeopleRepository
import javax.inject.Inject
import timber.log.Timber

class MainActivityViewModel @Inject constructor(
  private val discoverRepository: DiscoverRepository,
  private val peopleRepository: PeopleRepository
) : BindingViewModel() {

  private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
  val movieListLiveData: LiveData<List<Movie>>

  private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData(1)
  val tvListLiveData: LiveData<List<Tv>>

  private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData(1)
  val peopleLiveData: LiveData<List<Person>>

  @get:Bindable
  var toastLiveData: String? by bindingProperty(null)
    private set

  init {
    Timber.d("injection MainActivityViewModel")

    this.movieListLiveData = moviePageLiveData.switchMap { page ->
      discoverRepository.loadMovies(page) { toastLiveData = it }
    }

    this.tvListLiveData = tvPageLiveData.switchMap { page ->
      discoverRepository.loadTvs(page) { toastLiveData = it }
    }

    this.peopleLiveData = peoplePageLiveData.switchMap { page ->
      peopleRepository.loadPeople(page) { toastLiveData = it }
    }
  }

  fun postMoviePage(page: Int) = moviePageLiveData.postValue(page)

  fun postTvPage(page: Int) = tvPageLiveData.postValue(page)

  fun postPeoplePage(page: Int) = peoplePageLiveData.postValue(page)

  fun getFavouriteMovieList() = discoverRepository.getFavouriteMovieList()

  fun getFavouriteTvList() = discoverRepository.getFavouriteTvList()

  fun isLoading() = discoverRepository.isLoading || peopleRepository.isLoading
}
