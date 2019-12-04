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

package com.skydoves.common_ui.customs

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.skydoves.flourish.Flourish
import com.skydoves.flourish.FlourishAnimation
import com.skydoves.flourish.FlourishOrientation
import com.skydoves.flourish.createFlourish

/**
 * FlourishFactory creates an instance of the [Flourish].
 *
 * [Flourish](https://github.com/skydoves/flourish)
 */
object FlourishFactory {

  /** creates an instance of the [Flourish]. */
  fun create(parentView: ViewGroup, @LayoutRes resId: Int): Flourish {
    return createFlourish(parentView) {
      setFlourishLayout(resId)
      setFlourishAnimation(FlourishAnimation.BOUNCE)
      setFlourishOrientation(FlourishOrientation.TOP_RIGHT)
    }
  }
}
