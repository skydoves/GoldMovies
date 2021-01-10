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

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.entity.entities.Person
import com.skydoves.entity.response.PersonDetail
import com.skydoves.mvvm_coroutines.base.LiveCoroutinesViewModel
import com.skydoves.mvvm_coroutines.repository.PeopleRepository
import com.skydoves.network.extensions.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

class PersonDetailViewModel constructor(
  private val peopleRepository: PeopleRepository
) : LiveCoroutinesViewModel() {

  private val personIdMutableState: MutableStateFlow<Int> = MutableStateFlow(0)
  val personLiveData: LiveData<PersonDetail?>
  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  private lateinit var person: Person

  val isLoading: ObservableBoolean = ObservableBoolean(false)

  init {
    Timber.d("Injection : PersonDetailViewModel")

    this.personLiveData = personIdMutableState.asLiveData().switchMap { id ->
      launchOnViewModelScope {
        isLoading.set(true)
        peopleRepository.loadPersonDetail(id) {
          isLoading.set(false)
        }.asLiveData()
      }
    }
  }

  fun postPersonId(id: Int) {
    this.personIdMutableState.setValue(id)
    this.person = peopleRepository.getPerson(id)
  }

  fun getPerson() = this.person
}
