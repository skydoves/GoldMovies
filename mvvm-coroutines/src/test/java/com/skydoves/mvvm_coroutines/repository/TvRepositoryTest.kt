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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.common.ApiUtil.getCall
import com.skydoves.common.MockTestUtils.Companion.mockKeywordList
import com.skydoves.common.MockTestUtils.Companion.mockTv
import com.skydoves.common.MockTestUtils.Companion.mockVideoList
import com.skydoves.entity.Keyword
import com.skydoves.entity.Review
import com.skydoves.entity.Video
import com.skydoves.entity.database.TvDao
import com.skydoves.entity.response.KeywordListResponse
import com.skydoves.entity.response.ReviewListResponse
import com.skydoves.entity.response.VideoListResponse
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.TvClient
import com.skydoves.network.service.TvService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TvRepositoryTest {

  private lateinit var repository: TvRepository
  private lateinit var client: TvClient
  private val service = mock<TvService>()
  private val tvDao = mock<TvDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    client = TvClient(service)
    repository = TvRepository(client, tvDao)
  }

  @Test
  fun loadKeywordListFromNetworkTest() = runBlocking {
    val mockResponse = KeywordListResponse(1, emptyList())
    whenever(service.fetchKeywords(1)).thenReturn(getCall(mockResponse))
    whenever(tvDao.getTv(1)).thenReturn(mockTv())

    val data = repository.loadKeywordList(1) { }
    val observer = mock<Observer<List<Keyword>>>()
    data.observeForever(observer)
    verify(tvDao).getTv(1)

    val loadFromDB = tvDao.getTv(1)
    data.postValue(loadFromDB.keywords)
    verify(observer, times(2)).onChanged(loadFromDB.keywords)

    val updatedData = mockTv(keywords = mockKeywordList())
    whenever(tvDao.getTv(1)).thenReturn(updatedData)
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
    whenever(tvDao.getTv(1)).thenReturn(mockTv())

    val data = repository.loadVideoList(1) { }
    val observer = mock<Observer<List<Video>>>()
    data.observeForever(observer)
    verify(tvDao).getTv(1)

    val loadFromDB = tvDao.getTv(1)
    data.postValue(loadFromDB.videos)
    verify(observer, times(2)).onChanged(loadFromDB.videos)

    val updatedData = mockTv(videos = mockVideoList())
    whenever(tvDao.getTv(1)).thenReturn(updatedData)
    data.postValue(updatedData.videos)
    verify(observer).onChanged(updatedData.videos)

    client.fetchVideos(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.results, `is`(updatedData.videos))
        }
        else -> assertThat(it,
          instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }

  @Test
  fun loadReviewListFromNetworkTest() = runBlocking {
    val liveData = MutableLiveData<List<Review>>()
    whenever(tvDao.getTv(1)).thenReturn(mockTv())

    val loadFromDB = tvDao.getTv(1)
    liveData.postValue(loadFromDB.reviews)

    val mockResponse = ReviewListResponse(1, 0, emptyList(), 0, 0)
    whenever(service.fetchReviews(1)).thenReturn(getCall(mockResponse))

    val data = repository.loadReviewsList(1) { }
    verify(tvDao, times(2)).getTv(1)

    val observer = mock<Observer<List<Review>>>()
    data.observeForever(observer)
    val updatedData = mockTv()
    whenever(tvDao.getTv(1)).thenReturn(updatedData)
    liveData.postValue(updatedData.reviews)
    verify(observer).onChanged(updatedData.reviews)

    client.fetchReviews(1) {
      when (it) {
        is ApiResponse.Success -> assertEquals(it, `is`(mockResponse))
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }
}
