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

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.skydoves.bindables.BindingActivity
import com.skydoves.bundler.intentOf
import com.skydoves.mvvm_coroutines.R
import com.skydoves.mvvm_coroutines.databinding.ActivityPersonDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PersonDetailActivity : BindingActivity<ActivityPersonDetailBinding>(
  R.layout.activity_person_detail) {

  private val viewModel: PersonDetailViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // post the person id from intent
    viewModel.postPersonId(intent.getIntExtra(EXTRA_PERSON_ID, 0))
    // binding data into layout view
    binding {
      lifecycleOwner = this@PersonDetailActivity
      activity = this@PersonDetailActivity
      viewModel = this@PersonDetailActivity.viewModel
      person = this@PersonDetailActivity.viewModel.getPerson()
    }
  }

  companion object {
    const val EXTRA_PERSON_ID = "person"
    private const val intent_requestCode = 1000

    fun startActivity(activity: Activity?, personId: Int, view: View) {
      if (activity is Activity) {
        activity.intentOf<PersonDetailActivity> {
          putExtra(EXTRA_PERSON_ID to personId)
          ViewCompat.getTransitionName(view)?.let {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, it)
            activity.startActivityForResult(intent, intent_requestCode, options.toBundle())
          }
        }
      }
    }
  }
}
