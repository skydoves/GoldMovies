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

package com.skydoves.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.common.ApiUtil.getCall
import com.skydoves.common.MockTestUtils.Companion.mockMovieList
import com.skydoves.common.MockTestUtils.Companion.mockPersonList
import com.skydoves.common.MockTestUtils.Companion.mockTvList
import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.database.PeopleDao
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.entities.Movie
import com.skydoves.entity.entities.Person
import com.skydoves.entity.entities.Tv
import com.skydoves.entity.response.DiscoverMovieResponse
import com.skydoves.entity.response.DiscoverTvResponse
import com.skydoves.entity.response.PeopleResponse
import com.skydoves.mvvm.repository.DiscoverRepository
import com.skydoves.mvvm.repository.PeopleRepository
import com.skydoves.mvvm.ui.main.MainActivityViewModel
import com.skydoves.network.client.PeopleClient
import com.skydoves.network.client.TheDiscoverClient
import com.skydoves.network.service.PeopleService
import com.skydoves.network.service.TheDiscoverService
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {

  private lateinit var viewModel: MainActivityViewModel

  private lateinit var discoverRepository: DiscoverRepository
  private val discoverService = mock<TheDiscoverService>()
  private val discoverClient = TheDiscoverClient(discoverService)
  private val movieDao = mock<MovieDao>()
  private val tvDao = mock<TvDao>()

  private lateinit var peopleRepository: PeopleRepository
  private val peopleService = mock<PeopleService>()
  private val peopleClient = PeopleClient(peopleService)
  private val peopleDao = mock<PeopleDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    this.discoverRepository = DiscoverRepository(discoverClient, movieDao, tvDao)
    this.peopleRepository = PeopleRepository(peopleClient, peopleDao)
    this.viewModel = MainActivityViewModel(discoverRepository, peopleRepository)
  }

  @Test
  fun loadMovieListFromNetwork() {
    val loadFromDB = emptyList<Movie>()
    whenever(movieDao.getMovieList(1)).thenReturn(loadFromDB)

    val mockResponse = DiscoverMovieResponse(1, mockMovieList(), 100, 10)
    whenever(discoverService.fetchDiscoverMovie(1)).thenReturn(getCall(mockResponse))

    val data = viewModel.movieListLiveData
    val observer = mock<Observer<List<Movie>>>()
    data.observeForever(observer)

    viewModel.postMoviePage(1)
    viewModel.postMoviePage(1)

    verify(movieDao, atLeastOnce()).getMovieList(1)
    verify(discoverService, atLeastOnce()).fetchDiscoverMovie(1)
    verify(observer, atLeastOnce()).onChanged(loadFromDB)
    data.removeObserver(observer)
  }

  @Test
  fun loadTvListFromNetwork() {
    val loadFromDB = emptyList<Tv>()
    whenever(tvDao.getTvList(1)).thenReturn(loadFromDB)

    val mockResponse = DiscoverTvResponse(1, mockTvList(), 100, 10)
    whenever(discoverService.fetchDiscoverTv(1)).thenReturn(getCall(mockResponse))

    val data = viewModel.tvListLiveData
    val observer = mock<Observer<List<Tv>>>()
    data.observeForever(observer)

    viewModel.postTvPage(1)
    viewModel.postTvPage(1)

    verify(tvDao, atLeastOnce()).getTvList(1)
    verify(discoverService, atLeastOnce()).fetchDiscoverTv(1)
    verify(observer, atLeastOnce()).onChanged(loadFromDB)
    data.removeObserver(observer)
  }

  @Test
  fun loadPeopleFromNetwork() {
    val loadFromDB = emptyList<Person>()
    whenever(peopleDao.getPeople(1)).thenReturn(loadFromDB)

    val mockResponse = PeopleResponse(1, mockPersonList(), 100, 10)
    whenever(peopleService.fetchPopularPeople(1)).thenReturn(getCall(mockResponse))

    val data = viewModel.peopleLiveData
    val observer = mock<Observer<List<Person>>>()
    data.observeForever(observer)

    viewModel.postPeoplePage(1)
    viewModel.postPeoplePage(1)

    verify(peopleDao, atLeastOnce()).getPeople(1)
    verify(peopleService, atLeastOnce()).fetchPopularPeople(1)
    verify(observer, atLeastOnce()).onChanged(loadFromDB)
    data.removeObserver(observer)
  }
}
