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

package com.skydoves.mvvm_coroutines.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.common.ApiUtil.getCall
import com.skydoves.common.MockTestUtils.Companion.mockKeywordList
import com.skydoves.common.MockTestUtils.Companion.mockMovie
import com.skydoves.common.MockTestUtils.Companion.mockReviewList
import com.skydoves.common.MockTestUtils.Companion.mockVideoList
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.database.MovieDao
import com.skydoves.entity.response.KeywordListResponse
import com.skydoves.entity.response.ReviewListResponse
import com.skydoves.entity.response.VideoListResponse
import com.skydoves.mvvm_coroutines.repository.MovieRepository
import com.skydoves.mvvm_coroutines.ui.details.movie.MovieDetailViewModel
import com.skydoves.network.client.MovieClient
import com.skydoves.network.service.MovieService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDetailViewModelTest {

  private lateinit var viewModel: MovieDetailViewModel
  private lateinit var repository: MovieRepository
  private val service = mock<MovieService>()
  private val client = MovieClient(service)
  private val movieDao = mock<MovieDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    this.repository = MovieRepository(client, movieDao)
    this.viewModel = MovieDetailViewModel(repository)
  }

  @Test
  fun loadKeywordListFromNetwork() = runBlocking {
    val loadFromDB = mockMovie()
    whenever(movieDao.getMovie(1)).thenReturn(loadFromDB)

    val mockResponse = KeywordListResponse(1, mockKeywordList())
    whenever(service.fetchKeywords(1)).thenReturn(getCall(mockResponse))

    val data = repository.loadKeywordList(1) { }
    val observer = mock<Observer<List<Keyword>>>()
    data.observeForever(observer)

    viewModel.postMovieId(1)
    viewModel.postMovieId(1)

    verify(movieDao, atLeastOnce()).getMovie(1)
    verify(service, atLeastOnce()).fetchKeywords(1)
    verify(observer, atLeastOnce()).onChanged(loadFromDB.keywords)
    data.removeObserver(observer)
  }

  @Test
  fun loadVideoListFromNetwork() = runBlocking {
    val loadFromDB = mockMovie()
    whenever(movieDao.getMovie(1)).thenReturn(loadFromDB)

    val mockResponse = VideoListResponse(1, mockVideoList())
    whenever(service.fetchVideos(1)).thenReturn(getCall(mockResponse))

    val data = repository.loadVideoList(1) { }
    val observer = mock<Observer<List<Video>>>()
    data.observeForever(observer)

    viewModel.postMovieId(1)
    viewModel.postMovieId(1)

    verify(movieDao, atLeastOnce()).getMovie(1)
    verify(service, atLeastOnce()).fetchVideos(1)
    verify(observer, atLeastOnce()).onChanged(loadFromDB.videos)
    data.removeObserver(observer)
  }

  @Test
  fun loadReviewsListFromNetwork() = runBlocking {
    val loadFromDB = mockMovie()
    whenever(movieDao.getMovie(1)).thenReturn(loadFromDB)

    val mockResponse = ReviewListResponse(1, 0, mockReviewList(), 0, 0)
    whenever(service.fetchReviews(1)).thenReturn(getCall(mockResponse))

    val data = repository.loadReviewsList(1) { }
    val observer = mock<Observer<List<Review>>>()
    data.observeForever(observer)

    viewModel.postMovieId(1)
    viewModel.postMovieId(1)

    verify(movieDao, atLeastOnce()).getMovie(1)
    verify(service, atLeastOnce()).fetchReviews(1)
    verify(observer, atLeastOnce()).onChanged(loadFromDB.reviews)
    data.removeObserver(observer)
  }
}
