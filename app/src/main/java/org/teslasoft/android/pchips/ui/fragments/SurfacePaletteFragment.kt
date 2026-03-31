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
import com.google.android.material.elevation.SurfaceColors
import org.teslasoft.android.pchips.R
import org.teslasoft.android.pchips.adapters.ChipsAdapter
import org.teslasoft.android.pchips.util.GraphicUtil

class SurfacePaletteFragment : Fragment() {

    private var systemColors = arrayListOf<Int>()
    private var names = arrayListOf(
        "Surface 0", "Surface 1", "Surface 2", "Surface 3", "Surface 4", "Surface 5"
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

        initData()
        initRecyclerView()
        updateColor(0)
        initUX()
    }

    private fun initRecyclerView() {
        val heightOfElementInPx = calculateElementHeight()
        adapter = ChipsAdapter(systemColors, names, mContext ?: return, heightOfElementInPx, true)
        adapter?.setOnClickListener { _, position -> updateColor(position) }
        chips?.setLayoutManager(GridLayoutManager(mContext ?: return, 1))
        chips?.setAdapter(adapter)
        chips?.isNestedScrollingEnabled = false
    }

    private fun calculateElementHeight(): Int {
        val density = resources.displayMetrics.density
        val heightDensity = GraphicUtil.calculateRenderArea(mContext ?: return 0)
        val rawHeightOfElement: Int = (heightDensity / 6)
        val heightOfElementInPx: Int = (rawHeightOfElement * density).toInt()
        return heightOfElementInPx
    }

    private fun initData() {
        systemColors = arrayListOf(
            SurfaceColors.SURFACE_0.getColor(mContext ?: return),
            SurfaceColors.SURFACE_1.getColor(mContext ?: return),
            SurfaceColors.SURFACE_2.getColor(mContext ?: return),
            SurfaceColors.SURFACE_3.getColor(mContext ?: return),
            SurfaceColors.SURFACE_4.getColor(mContext ?: return),
            SurfaceColors.SURFACE_5.getColor(mContext ?: return)
        )
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
        selectedColor?.background = GraphicUtil.getBackgroundDrawable(selectedColor?.background!!, systemColors[position])
        colorCode?.text = "#${Integer.toHexString(systemColors[position]).substring(2).uppercase()}"

        if (GraphicUtil.isDarkThemeEnabled(mContext ?: return)) {
            colorCode?.setTextColor(resources.getColor(R.color.white, (mContext ?: return).theme))
            textHint?.setTextColor(resources.getColor(R.color.white_shadowed, (mContext ?: return).theme))
        } else {
            colorCode?.setTextColor(resources.getColor(R.color.black, (mContext ?: return).theme))
            textHint?.setTextColor(resources.getColor(R.color.black_shadowed, (mContext ?: return).theme))
        }
    }
}
