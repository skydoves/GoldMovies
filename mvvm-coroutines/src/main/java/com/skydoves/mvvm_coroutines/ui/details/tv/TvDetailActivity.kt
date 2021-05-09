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

package com.skydoves.mvvm_coroutines.ui.details.tv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.skydoves.bindables.BindingActivity
import com.skydoves.mvvm_coroutines.R
import com.skydoves.mvvm_coroutines.databinding.ActivityTvDetailBinding
import org.koin.android.viewmodel.ext.android.viewModel

class TvDetailActivity : BindingActivity<ActivityTvDetailBinding>(R.layout.activity_tv_detail) {

  private val viewModel: TvDetailViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // post the tv id from intent
    viewModel.postTvId(intent.getIntExtra(tv, 0))
    // binding data into layout view
    binding {
      lifecycleOwner = this@TvDetailActivity
      activity = this@TvDetailActivity
      viewModel = this@TvDetailActivity.viewModel
      tv = this@TvDetailActivity.viewModel.getTv()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) onBackPressed()
    return false
  }

  companion object {
    private const val tv = "tv"
    fun startActivityModel(context: Context?, tvId: Int) {
      val intent = Intent(context, TvDetailActivity::class.java).apply { putExtra(tv, tvId) }
      context?.startActivity(intent)
    }
  }
}
