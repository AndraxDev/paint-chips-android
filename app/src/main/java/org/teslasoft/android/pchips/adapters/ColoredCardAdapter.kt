/**************************************************************************
 * Copyright (c) 2024, Dmytro Ostapenko (AndraxDev). All rights reserved.
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

package org.teslasoft.android.pchips.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.teslasoft.android.pchips.R

class ColoredCardAdapter(private val items: ArrayList<Int>, private val context: Context) : RecyclerView.Adapter<ColoredCardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_colored_card, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var card: LinearLayout = itemView.findViewById(R.id.card)

        fun bind(color: Int, context: Context) {
            val drawable = ColorDrawable(color)
            val rippleColor = ContextCompat.getColor(context, R.color.ripple)
            val colorStateList = ColorStateList.valueOf(rippleColor)
            val rippleDrawable = RippleDrawable(colorStateList, drawable, null)

            card.background = rippleDrawable
        }
    }
}
