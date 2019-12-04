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
import com.skydoves.common.MockTestUtils.Companion.mockPerson
import com.skydoves.common.MockTestUtils.Companion.mockPersonDetail
import com.skydoves.common.MockTestUtils.Companion.mockPersonList
import com.skydoves.entity.database.PeopleDao
import com.skydoves.entity.entities.Person
import com.skydoves.entity.response.PeopleResponse
import com.skydoves.entity.response.PersonDetail
import com.skydoves.network.ApiResponse
import com.skydoves.network.client.PeopleClient
import com.skydoves.network.service.PeopleService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalStdlibApi
class PeopleRepositoryTest {

  private lateinit var repository: PeopleRepository
  private lateinit var client: PeopleClient
  private val service = mock<PeopleService>()
  private val peopleDao = mock<PeopleDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  @Before
  fun setup() {
    client = PeopleClient(service)
    repository = PeopleRepository(client, peopleDao)
  }

  @Test
  fun loadPeopleListFromNetworkTest() = runBlocking {
    val mockResponse = PeopleResponse(1, emptyList(), 0, 0)
    whenever(service.fetchPopularPeople(1)).thenReturn(getCall(mockResponse))
    whenever(peopleDao.getPeople(1)).thenReturn(emptyList())

    val data = repository.loadPeople(1) { }
    val observer = mock<Observer<List<Person>>>()
    data.observeForever(observer)
    verify(peopleDao).getPeople(1)

    val loadFromDB = peopleDao.getPeople(1)
    data.postValue(loadFromDB)
    verify(observer, times(2)).onChanged(loadFromDB)

    val updatedData = mockPersonList()
    whenever(peopleDao.getPeople(1)).thenReturn(updatedData)
    data.postValue(updatedData)
    verify(observer).onChanged(updatedData)

    client.fetchPopularPeople(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it.data, `is`(mockResponse))
          assertEquals(it.data?.results, `is`(updatedData))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }

  @Test
  fun loadPersonDetailFromNetworkTest() = runBlocking {
    val mockResponse = mockPersonDetail()
    whenever(service.fetchPersonDetail(1)).thenReturn(getCall(mockResponse))
    whenever(peopleDao.getPerson(1)).thenReturn(mockPerson())

    val data = repository.loadPersonDetail(1) { }
    val observer = mock<Observer<PersonDetail>>()
    data.observeForever(observer)
    verify(peopleDao).getPerson(1)

    val loadFromDB = peopleDao.getPerson(1)
    data.postValue(loadFromDB.personDetail)
    verify(observer, times(2)).onChanged(loadFromDB.personDetail)

    val updatedData = mockPerson()
    whenever(peopleDao.getPerson(1)).thenReturn(updatedData)
    data.postValue(updatedData.personDetail)
    verify(observer, times(3)).onChanged(updatedData.personDetail)

    client.fetchPersonDetail(1) {
      when (it) {
        is ApiResponse.Success -> {
          assertEquals(it, `is`(mockResponse))
          assertEquals(it.data, `is`(updatedData.personDetail))
        }
        else -> assertThat(it, instanceOf(ApiResponse.Failure::class.java))
      }
    }
  }
}
