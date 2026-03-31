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
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teslasoft.android.pchips.R
import org.teslasoft.android.pchips.adapters.ChipsAdapter
import org.teslasoft.android.pchips.util.GraphicUtil

class AccentPaletteFragment : Fragment() {
    private val systemColors = arrayListOf(
        android.R.color.system_neutral1_0,
        android.R.color.system_neutral2_0,
        android.R.color.system_accent1_0,
        android.R.color.system_accent2_0,
        android.R.color.system_accent3_0,

        android.R.color.system_neutral1_50,
        android.R.color.system_neutral2_50,
        android.R.color.system_accent1_50,
        android.R.color.system_accent2_50,
        android.R.color.system_accent3_50,

        android.R.color.system_neutral1_100,
        android.R.color.system_neutral2_100,
        android.R.color.system_accent1_100,
        android.R.color.system_accent2_100,
        android.R.color.system_accent3_100,

        android.R.color.system_neutral1_200,
        android.R.color.system_neutral2_200,
        android.R.color.system_accent1_200,
        android.R.color.system_accent2_200,
        android.R.color.system_accent3_200,

        android.R.color.system_neutral1_300,
        android.R.color.system_neutral2_300,
        android.R.color.system_accent1_300,
        android.R.color.system_accent2_300,
        android.R.color.system_accent3_300,

        android.R.color.system_neutral1_400,
        android.R.color.system_neutral2_400,
        android.R.color.system_accent1_400,
        android.R.color.system_accent2_400,
        android.R.color.system_accent3_400,

        android.R.color.system_neutral1_500,
        android.R.color.system_neutral2_500,
        android.R.color.system_accent1_500,
        android.R.color.system_accent2_500,
        android.R.color.system_accent3_500,

        android.R.color.system_neutral1_600,
        android.R.color.system_neutral2_600,
        android.R.color.system_accent1_600,
        android.R.color.system_accent2_600,
        android.R.color.system_accent3_600,

        android.R.color.system_neutral1_700,
        android.R.color.system_neutral2_700,
        android.R.color.system_accent1_700,
        android.R.color.system_accent2_700,
        android.R.color.system_accent3_700,

        android.R.color.system_neutral1_800,
        android.R.color.system_neutral2_800,
        android.R.color.system_accent1_800,
        android.R.color.system_accent2_800,
        android.R.color.system_accent3_800,

        android.R.color.system_neutral1_900,
        android.R.color.system_neutral2_900,
        android.R.color.system_accent1_900,
        android.R.color.system_accent2_900,
        android.R.color.system_accent3_900,

        android.R.color.system_neutral1_1000,
        android.R.color.system_neutral2_1000,
        android.R.color.system_accent1_1000,
        android.R.color.system_accent2_1000,
        android.R.color.system_accent3_1000
    )

    private var names = arrayListOf(
        "N1 0",    "N2 0",    "A1 0",    "A2 0",    "A3 0",
        "N1 50",   "N2 50",   "A1 50",   "A2 50",   "A3 50",
        "N1 100",  "N2 100",  "A1 100",  "A2 100",  "A3 100",
        "N1 200",  "N2 200",  "A1 200",  "A2 200",  "A3 200",
        "N1 300",  "N2 300",  "A1 300",  "A2 300",  "A3 300",
        "N1 400",  "N2 400",  "A1 400",  "A2 400",  "A3 400",
        "N1 500",  "N2 500",  "A1 500",  "A2 500",  "A3 500",
        "N1 600",  "N2 600",  "A1 600",  "A2 600",  "A3 600",
        "N1 700",  "N2 700",  "A1 700",  "A2 700",  "A3 700",
        "N1 800",  "N2 800",  "A1 800",  "A2 800",  "A3 800",
        "N1 900",  "N2 900",  "A1 900",  "A2 900",  "A3 900",
        "N1 1000", "N2 1000", "A1 1000", "A2 1000", "A3 1000"
    )

    private var adapter: ChipsAdapter? = null
    private var chips: RecyclerView? = null
    private var selectedColor: LinearLayout? = null
    private var colorCode: TextView? = null
    private var textHint: TextView? = null
    private var view: View? = null
    private var mContext: Context? = null
    private var isViewInitialized = false

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
        return inflater.inflate(R.layout.fragment_palette, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.view = view
        mContext = context
        initComponents()
    }

    private fun initComponents() {
        if (view == null || mContext == null) return

        chips = view?.findViewById(R.id.accent_grid)
        selectedColor = view?.findViewById(R.id.selected_color)
        colorCode = view?.findViewById(R.id.color_code)
        textHint = view?.findViewById(R.id.text_hint)

        if (isViewInitialized) {
            initView()
        }
    }

    fun initView() {
        isViewInitialized = true
        if (view == null || mContext == null) return

        initRecyclerView()
        updateColor(0)
        initUX()
    }

    private fun initRecyclerView() {
        val heightOfElementInPx = calculateElementHeight()
        adapter = ChipsAdapter(systemColors, names, mContext ?: return, heightOfElementInPx, false)
        adapter?.setOnClickListener { _, position -> updateColor(position) }
        chips?.setLayoutManager(GridLayoutManager(mContext ?: return, 5))
        chips?.setAdapter(adapter)
        chips?.isNestedScrollingEnabled = false
    }

    private fun calculateElementHeight(): Int {
        val density = resources.displayMetrics.density
        val heightDensity = GraphicUtil.calculateRenderArea(mContext ?: return 0)
        val rawHeightOfElement: Int = (heightDensity / 12)
        val heightOfElementInPx: Int = (rawHeightOfElement * density).toInt()
        return heightOfElementInPx
    }

    private fun initUX() {
        selectedColor?.setOnLongClickListener {
            val clipboard = (mContext ?: return@setOnLongClickListener false).getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Color code", colorCode?.text)
            clipboard.setPrimaryClip(clip)
            true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateColor(position: Int) {
        selectedColor?.background = GraphicUtil.getBackgroundDrawable(selectedColor?.background!!, resources.getColor(systemColors[position], (mContext ?: return).theme))
        colorCode?.text = "#${Integer.toHexString(resources.getColor(systemColors[position], (mContext ?: return).theme)).substring(2).uppercase()}"

        if (position >= 35) {
            colorCode?.setTextColor(resources.getColor(R.color.white, (mContext ?: return).theme))
            textHint?.setTextColor(resources.getColor(R.color.white_shadowed, (mContext ?: return).theme))
        } else {
            colorCode?.setTextColor(resources.getColor(R.color.black, (mContext ?: return).theme))
            textHint?.setTextColor(resources.getColor(R.color.black_shadowed, (mContext ?: return).theme))
        }
    }
}
