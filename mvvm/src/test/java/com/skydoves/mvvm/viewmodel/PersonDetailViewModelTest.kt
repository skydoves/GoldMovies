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
import com.skydoves.common.MockTestUtils.Companion.mockPerson
import com.skydoves.common.MockTestUtils.Companion.mockPersonDetail
import com.skydoves.entity.database.PeopleDao
import com.skydoves.entity.response.PersonDetail
import com.skydoves.mvvm.repository.PeopleRepository
import com.skydoves.mvvm.ui.details.person.PersonDetailViewModel
import com.skydoves.network.client.PeopleClient
import com.skydoves.network.service.PeopleService
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonDetailViewModelTest {

  private lateinit var viewModel: PersonDetailViewModel
  private lateinit var repository: PeopleRepository
  private val service = mock<PeopleService>()
  private val client = PeopleClient(service)
  private val peopleDao = mock<PeopleDao>()

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun setup() {
    this.repository = PeopleRepository(client, peopleDao)
    this.viewModel = PersonDetailViewModel(repository)
  }

  @Test
  fun loadPersonDetailFromLocalDatabase() {
    val mockResponse = mockPersonDetail()
    whenever(service.fetchPersonDetail(1)).thenReturn(getCall(mockResponse))
    whenever(peopleDao.getPerson(1)).thenReturn(mockPerson())

    val data = viewModel.personLiveData
    val observer = mock<Observer<PersonDetail>>()
    data.observeForever(observer)

    viewModel.postPersonId(1)
    viewModel.postPersonId(1)

    verify(peopleDao, atLeastOnce()).getPerson(1)
    verify(observer, atLeastOnce()).onChanged(mockResponse)
    data.removeObserver(observer)
  }
}
