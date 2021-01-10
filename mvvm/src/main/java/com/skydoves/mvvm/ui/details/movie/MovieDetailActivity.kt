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

package com.skydoves.mvvm.ui.details.movie

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.observe
import com.skydoves.common_ui.extensions.applyMaterialTransform
import com.skydoves.common_ui.extensions.toast
import com.skydoves.entity.entities.Movie
import com.skydoves.mvvm.R
import com.skydoves.mvvm.base.ViewModelActivity
import com.skydoves.mvvm.databinding.ActivityMovieDetailBinding

class MovieDetailActivity : ViewModelActivity() {

  private val vm: MovieDetailViewModel by injectViewModels()
  private val binding: ActivityMovieDetailBinding by binding(R.layout.activity_movie_detail)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // post the movie id from intent
    vm.postMovieId(intent.getIntExtra(movieKey, 0))

    // apply material container transform
    applyMaterialTransform(vm.getMovie().title)

    // binding data into layout view
    with(binding) {
      lifecycleOwner = this@MovieDetailActivity
      activity = this@MovieDetailActivity
      viewModel = vm
      movie = vm.getMovie()
    }

    // observe error messages
    observeMessages()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) onBackPressed()
    return false
  }

  private fun observeMessages() =
    this.vm.toastLiveData.observe(this) { toast(it) }

  companion object {
    private const val movieKey = "movie"
    fun startActivityModel(context: Context?, startView: View, movie: Movie) {
      if (context is Activity) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra(movieKey, movie.id)
        val options = ActivityOptions.makeSceneTransitionAnimation(context,
          startView, movie.title)
        context.startActivity(intent, options.toBundle())
      }
    }
  }
}
