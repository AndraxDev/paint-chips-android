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

package org.teslasoft.android.pchips.ui

import android.os.Bundle
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.elevation.SurfaceColors
import org.teslasoft.android.pchips.R
import org.teslasoft.android.pchips.ui.fragments.AboutFragment
import org.teslasoft.android.pchips.ui.fragments.AccentPaletteFragment
import org.teslasoft.android.pchips.ui.fragments.HarmonizerFragment
import org.teslasoft.android.pchips.ui.fragments.SurfacePaletteFragment

class MainActivity : FragmentActivity() {

    private var bottomNavigationView: BottomNavigationView? = null
    private var windowContainer: ConstraintLayout? = null
    private var accentPaletteFragment: AccentPaletteFragment? = null
    private var surfacePaletteFragment: SurfacePaletteFragment? = null
    private var harmonizerFragment: HarmonizerFragment? = null
    private var aboutFragment: AboutFragment? = null

    private var nextTab = 0
    private var prevTab = 0

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window?.statusBarColor = SurfaceColors.SURFACE_0.getColor(this)
            window?.navigationBarColor = SurfaceColors.SURFACE_2.getColor(this)
        }

        initViews()
        initFragments()

        bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_accent_palette -> {
                    nextTab = 0
                    loadFragment(accentPaletteFragment!!, prevTab, nextTab)
                    prevTab = nextTab
                }

                R.id.menu_surface_palette -> {
                    nextTab = 1
                    loadFragment(surfacePaletteFragment!!, prevTab, nextTab)
                    prevTab = nextTab
                }

                R.id.menu_harmonizer -> {
                    nextTab = 2
                    loadFragment(harmonizerFragment!!, prevTab, nextTab)
                    prevTab = nextTab
                }

                R.id.menu_about -> {
                    nextTab = 3
                    loadFragment(aboutFragment!!, prevTab, nextTab)
                    prevTab = nextTab
                }
            }

            true
        }

        if (savedInstanceState != null) {
            nextTab = savedInstanceState.getInt("nextTab")
            prevTab = savedInstanceState.getInt("nextTab")

            updateTab()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("nextTab", nextTab)
    }

    private fun initViews() {
        try {
            if (bottomNavigationView == null) bottomNavigationView = findViewById(R.id.bottom_navigation)
            if (windowContainer == null) windowContainer = findViewById(R.id.window)
        } catch (_: NullPointerException) { /* ignored */ }

        bottomNavigationView?.setBackgroundColor(SurfaceColors.SURFACE_2.getColor(this))
        windowContainer?.setBackgroundColor(SurfaceColors.SURFACE_0.getColor(this))
    }

    private fun initFragments() {
        if (accentPaletteFragment == null) accentPaletteFragment = AccentPaletteFragment()
        if (surfacePaletteFragment == null) surfacePaletteFragment = SurfacePaletteFragment()
        if (harmonizerFragment == null) harmonizerFragment = HarmonizerFragment()
        if (aboutFragment == null) aboutFragment = AboutFragment()
    }

    private fun loadFragment(fragment: Fragment, prevTab: Int, nextTab: Int) {
        if (nextTab > prevTab) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.mtrl_fragment_open_enter, R.anim.mtrl_fragment_open_exit)
                .replace(R.id.fragment_container, fragment)
                .commit()
        } else if (nextTab < prevTab) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.mtrl_fragment_close_enter, R.anim.mtrl_fragment_close_exit)
                .replace(R.id.fragment_container, fragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fade_in_tab, R.anim.fade_out_tab)
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        initViews()
        initFragments()

        val statusBarHeight = window.decorView.rootWindowInsets.getInsets(WindowInsets.Type.statusBars()).top
        val navigationBarHeight = window.decorView.rootWindowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // Handle Android 15 edge-to-edge mode
            windowContainer?.setPadding(0, statusBarHeight, 0, 0)
        }

        updateTab()

        accentPaletteFragment?.initView(statusBarHeight, navigationBarHeight)
        surfacePaletteFragment?.initView(statusBarHeight, navigationBarHeight)
    }

    private fun updateTab() {
        when (nextTab) {
            0 -> {
                bottomNavigationView?.selectedItemId = R.id.menu_accent_palette
                loadFragment(accentPaletteFragment!!, prevTab, nextTab)
            }

            1 -> {
                bottomNavigationView?.selectedItemId = R.id.menu_surface_palette
                loadFragment(surfacePaletteFragment!!, prevTab, nextTab)
            }

            2 -> {
                bottomNavigationView?.selectedItemId = R.id.menu_harmonizer
                loadFragment(harmonizerFragment!!, prevTab, nextTab)
            }

            else -> {
                bottomNavigationView?.selectedItemId = R.id.menu_about
                loadFragment(aboutFragment!!, prevTab, nextTab)
            }
        }
    }
}
