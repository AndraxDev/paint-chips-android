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

package org.teslasoft.android.pchips.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import org.teslasoft.android.pchips.R
import org.teslasoft.android.pchips.util.GraphicUtil

class ChipsAdapter(private val items: ArrayList<Int>, private val names: ArrayList<String>, private val context: Context, private val heightOfElementInPx: Int, private val rawColor: Boolean) : RecyclerView.Adapter<ChipsAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_chip, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (rawColor) {
            holder.chip.backgroundTintList = ColorStateList.valueOf(items[position])
            holder.chip.textSize = 16f
        } else {
            holder.chip.backgroundTintList = ColorStateList.valueOf(ResourcesCompat.getColor(context.resources, items[position], null))
        }

        holder.chip.text = names[position]
        holder.chip.layoutParams.height = heightOfElementInPx

        if (position >= 35 || (rawColor && GraphicUtil.isDarkThemeEnabled(context))) {
            holder.chip.setTextColor(ResourcesCompat.getColor(context.resources, R.color.white, null))
        } else {
            holder.chip.setTextColor(ResourcesCompat.getColor(context.resources, R.color.black, null))
        }

        holder.chip.setOnClickListener {
            onClickListener?.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chip: MaterialButton = itemView.findViewById(R.id.chip)
    }

    fun interface OnClickListener {
        fun onClick(v: View?, position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}
