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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
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
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.MovieClient
import com.skydoves.network.service.MovieService
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieRepositoryTest {

  private lateinit var repository: MovieRepository
  private lateinit var client: MovieClient
  private val service = mock<MovieService>()
  private val movieDao = mock<MovieDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  @Before
  fun setup() {
    client = MovieClient(service)
    repository = MovieRepository(client, movieDao)
  }

  @Test
  fun loadKeywordListFromNetworkTest() = runBlocking {
    val mockResponse = KeywordListResponse(1, emptyList())
    whenever(service.fetchKeywords(1)).thenReturn(getCall(mockResponse))
    whenever(movieDao.getMovie(1)).thenReturn(mockMovie())

    val data = repository.loadKeywordList(1) { }
    val observer = mock<Observer<List<Keyword>>>()
    data.observeForever(observer)
    verify(movieDao).getMovie(1)

    val loadFromDB = movieDao.getMovie(1)
    data.postValue(loadFromDB.keywords)
    verify(observer, times(2)).onChanged(loadFromDB.keywords)

    val updatedData = mockMovie(keywords = mockKeywordList())
    whenever(movieDao.getMovie(1)).thenReturn(updatedData)
    data.postValue(updatedData.keywords)
    verify(observer).onChanged(updatedData.keywords)

    client.fetchKeywords(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.keywords, `is`(updatedData.keywords))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }

  @Test
  fun loadVideoListFromNetworkTest() = runBlocking {
    val mockResponse = VideoListResponse(1, emptyList())
    whenever(service.fetchVideos(1)).thenReturn(getCall(mockResponse))
    whenever(movieDao.getMovie(1)).thenReturn(mockMovie())

    val data = repository.loadVideoList(1) { }
    val observer = mock<Observer<List<Video>>>()
    data.observeForever(observer)
    verify(movieDao).getMovie(1)

    val loadFromDB = movieDao.getMovie(1)
    data.postValue(loadFromDB.videos)
    verify(observer, times(2)).onChanged(loadFromDB.videos)

    val updatedData = mockMovie(videos = mockVideoList())
    whenever(movieDao.getMovie(1)).thenReturn(updatedData)
    data.postValue(updatedData.videos)
    verify(observer).onChanged(updatedData.videos)

    client.fetchVideos(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.results, `is`(updatedData.videos))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }

  @Test
  fun loadReviewListFromNetworkTest() = runBlocking {
    val mockResponse = ReviewListResponse(1, 0, emptyList(), 0, 0)
    whenever(service.fetchReviews(1)).thenReturn(getCall(mockResponse))
    whenever(movieDao.getMovie(1)).thenReturn(mockMovie())

    val data = repository.loadReviewsList(1) { }
    val observer = mock<Observer<List<Review>>>()
    data.observeForever(observer)
    verify(movieDao).getMovie(1)

    val loadFromDB = movieDao.getMovie(1)
    data.postValue(loadFromDB.reviews)
    verify(observer, times(2)).onChanged(loadFromDB.reviews)

    val updatedData = mockMovie(reviews = mockReviewList())
    whenever(movieDao.getMovie(1)).thenReturn(updatedData)
    data.postValue(updatedData.reviews)
    verify(observer).onChanged(updatedData.reviews)

    client.fetchReviews(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.results, `is`(updatedData.videos))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }
}
