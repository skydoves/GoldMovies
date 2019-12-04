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

package com.skydoves.mvvm_coroutines.ui.details.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.skydoves.entity.entities.Person
import com.skydoves.entity.response.PersonDetail
import com.skydoves.mvvm_coroutines.base.LiveCoroutinesViewModel
import com.skydoves.mvvm_coroutines.repository.PeopleRepository
import timber.log.Timber

class PersonDetailViewModel
constructor(private val peopleRepository: PeopleRepository) : LiveCoroutinesViewModel() {

  private val personIdLiveData: MutableLiveData<Int> = MutableLiveData()
  val personLiveData: LiveData<PersonDetail>
  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  private lateinit var person: Person

  init {
    Timber.d("Injection : PersonDetailViewModel")

    this.personLiveData = personIdLiveData.switchMap { id ->
      launchOnViewModelScope {
        peopleRepository.loadPersonDetail(id) { toastLiveData.postValue(it) }
      }
    }
  }

  fun postPersonId(id: Int) {
    this.personIdLiveData.postValue(id)
    this.person = peopleRepository.getPerson(id)
  }

  fun getPerson() = this.person
}
