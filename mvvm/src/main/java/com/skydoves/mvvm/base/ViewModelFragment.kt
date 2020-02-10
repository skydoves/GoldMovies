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

package com.skydoves.mvvm.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * ViewModelFragment is an abstract class for request dependency injection and
 * provides implementations of [ViewModel] and [ViewDataBinding] from an abstract information.
 * Do not modify this class. This is a first-level abstraction class.
 * If you want to add more specifications, make another class which extends [ViewModelFragment].
 */
abstract class ViewModelFragment : Fragment() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  protected inline fun <reified VM : ViewModel>
    injectViewModels(): Lazy<VM> = viewModels { viewModelFactory }

  protected inline fun <reified VM : ViewModel>
    injectActivityVIewModels(): Lazy<VM> = activityViewModels { viewModelFactory }

  protected inline fun <reified T : ViewDataBinding> binding(
    inflater: LayoutInflater,
    @LayoutRes resId: Int,
    container: ViewGroup?
  ): T = DataBindingUtil.inflate(inflater, resId, container, false)
}
