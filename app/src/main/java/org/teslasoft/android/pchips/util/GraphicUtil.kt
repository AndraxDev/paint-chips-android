/**************************************************************************
 * Copyright (c) 2024-2026, Dmytro Ostapenko (AndraxDev). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **************************************************************************/

package org.teslasoft.android.pchips.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat

class GraphicUtil {
    companion object {
        fun isDarkThemeEnabled(context: Context): Boolean {
            return when (context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                Configuration.UI_MODE_NIGHT_UNDEFINED -> false
                else -> false
            }
        }

        fun getBackgroundDrawable(drawable: Drawable, color: Int) : Drawable {
            DrawableCompat.setTint(DrawableCompat.wrap(drawable), color)
            return drawable
        }

        fun calculateRenderArea(context: Context): Int {
            val rect = Rect()
            (context as Activity).window.decorView.getWindowVisibleDisplayFrame(rect)
            val height = rect.height()
            val density = context.resources.displayMetrics.density
            val heightDensity: Int = (height / density).toInt() - 250
            return heightDensity
        }
    }
}
