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

package com.skydoves.mvvm.ui.details.tv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.observe
import com.skydoves.common_ui.extensions.toast
import com.skydoves.mvvm.R
import com.skydoves.mvvm.base.ViewModelActivity
import com.skydoves.mvvm.databinding.ActivityTvDetailBinding

class TvDetailActivity : ViewModelActivity() {

  private val vm: TvDetailViewModel by injectViewModels()
  private val binding
    by binding<ActivityTvDetailBinding>(R.layout.activity_tv_detail)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // post the tv id from intent
    vm.postTvId(intent.getIntExtra(tv, 0))
    // binding data into layout view
    with(binding) {
      lifecycleOwner = this@TvDetailActivity
      activity = this@TvDetailActivity
      viewModel = vm
      tv = vm.getTv()
    }
    // observe error messages
    observeMessages()
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == android.R.id.home) onBackPressed()
    return false
  }

  private fun observeMessages() =
    this.vm.toastLiveData.observe(this) { toast(it) }

  companion object {
    private const val tv = "tv"
    fun startActivityModel(context: Context?, tvId: Int) {
      val intent = Intent(context, TvDetailActivity::class.java)
      intent.putExtra(tv, tvId)
      context?.startActivity(intent)
    }
  }
}
