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

package org.teslasoft.android.pchips.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import org.teslasoft.android.pchips.R
import org.teslasoft.android.pchips.adapters.ColoredCardAdapter

class AboutFragment : Fragment() {

    private var version: TextView? = null
    private var view: View? = null
    private var mContext: Context? = null

    private var colors: ArrayList<Int> = arrayListOf()

    private var carousel: RecyclerView? = null
    private var coloredCardAdapter: ColoredCardAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.mContext = context
        initComponents()
    }

    override fun onDetach() {
        super.onDetach()
        this.mContext = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.view = view
        this.mContext = context

        initComponents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initComponents() {
        if (view == null || context == null) return

        version = view?.findViewById(R.id.app_version)
        carousel = view?.findViewById(R.id.carousel)

        version?.text = String.format(getString(R.string.version), mContext?.packageManager?.getPackageInfo(mContext?.packageName!!, 0)?.versionName)

        colors.clear()

        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_50))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_100))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_200))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_300))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_400))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_500))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_600))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_700))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_800))
        colors.add(ContextCompat.getColor(mContext ?: return, android.R.color.system_accent3_900))

        carousel?.layoutManager = CarouselLayoutManager()
        coloredCardAdapter = ColoredCardAdapter(colors, mContext ?: return)
        carousel?.adapter = coloredCardAdapter
        coloredCardAdapter?.notifyDataSetChanged()
    }
}
