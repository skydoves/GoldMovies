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

import com.skydoves.entity.database.PeopleDao
import com.skydoves.network.service.PeopleService
import com.skydoves.sandwich.suspendOnSuccess
import com.skydoves.whatif.whatIfNotNull
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import timber.log.Timber

@Singleton
class PeopleRepository @Inject constructor(
  private val peopleService: PeopleService,
  private val peopleDao: PeopleDao
) : Repository {

  init {
    Timber.d("Injection PeopleRepository")
  }

  fun loadPeople(page: Int, onSuccess: () -> Unit) = flow {
    var people = peopleDao.getPeople(page)
    if (people.isEmpty()) {
      val response = peopleService.fetchPopularPeople(page)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          people = it.results
          people.forEach { it.page = page }
          peopleDao.insertPeople(people)
          emit(people)
        }
      }
    } else {
      emit(people)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun loadPersonDetail(id: Int, onSuccess: () -> Unit) = flow {
    val person = peopleDao.getPerson(id)
    var personDetail = person.personDetail
    if (personDetail == null) {
      val response = peopleService.fetchPersonDetail(id)
      response.suspendOnSuccess {
        data.whatIfNotNull {
          personDetail = it
          person.personDetail = personDetail
          peopleDao.updatePerson(person)
          emit(personDetail)
        }
      }
    } else {
      emit(personDetail)
    }
  }.onCompletion { onSuccess() }.flowOn(Dispatchers.IO)

  fun getPerson(id: Int) = peopleDao.getPerson(id)
}
