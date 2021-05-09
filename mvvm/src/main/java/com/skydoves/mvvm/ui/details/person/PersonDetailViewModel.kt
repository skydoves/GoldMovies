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

package com.skydoves.mvvm.ui.details.person

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.skydoves.bindables.BindingViewModel
import com.skydoves.bindables.bindingProperty
import com.skydoves.entity.entities.Person
import com.skydoves.entity.response.PersonDetail
import com.skydoves.mvvm.repository.PeopleRepository
import javax.inject.Inject
import timber.log.Timber

class PersonDetailViewModel @Inject constructor(
  private val peopleRepository: PeopleRepository
) : BindingViewModel() {

  private val personIdLiveData: MutableLiveData<Int> = MutableLiveData()
  val personLiveData: LiveData<PersonDetail?>
  val toastLiveData: MutableLiveData<String> = MutableLiveData()

  private lateinit var person: Person

  @get:Bindable
  var isLoading: Boolean by bindingProperty(false)
    private set

  init {
    Timber.d("Injection : PersonDetailViewModel")

    this.personLiveData = personIdLiveData.switchMap { id ->
      isLoading = true
      peopleRepository.loadPersonDetail(id) {
        isLoading = false
      }.asLiveData()
    }
  }

  fun postPersonId(id: Int) {
    this.personIdLiveData.postValue(id)
    this.person = peopleRepository.getPerson(id)
  }

  fun getPerson() = this.person
}
