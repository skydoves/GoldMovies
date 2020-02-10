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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.observe
import com.skydoves.common_ui.extensions.checkIsMaterialVersion
import com.skydoves.common_ui.extensions.toast
import com.skydoves.mvvm.R
import com.skydoves.mvvm.base.ViewModelActivity
import com.skydoves.mvvm.databinding.ActivityPersonDetailBinding

class PersonDetailActivity : ViewModelActivity() {

  private val vm: PersonDetailViewModel by injectViewModels()
  private val binding
    by binding<ActivityPersonDetailBinding>(R.layout.activity_person_detail)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // post the person id from intent
    vm.postPersonId(intent.getIntExtra(person, 0))
    // binding data into layout view
    with(binding) {
      lifecycleOwner = this@PersonDetailActivity
      activity = this@PersonDetailActivity
      viewModel = vm
      person = vm.getPerson()
    }
    // observe error messages
    observeMessages()
  }

  private fun observeMessages() =
    this.vm.toastLiveData.observe(this) { toast(it) }

  companion object {
    const val person = "person"
    private const val intent_requestCode = 1000

    fun startActivity(activity: Activity?, personId: Int, view: View) {
      if (activity != null) {
        if (checkIsMaterialVersion()) {
          val intent = Intent(activity, PersonDetailActivity::class.java)
          ViewCompat.getTransitionName(view)?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, it)
            intent.putExtra(person, personId)
            activity.startActivityForResult(intent, intent_requestCode, options.toBundle())
          }
        } else {
          val intent = Intent(activity, PersonDetailActivity::class.java)
          intent.putExtra(person, personId)
          activity.startActivity(intent)
        }
      }
    }
  }
}
