package org.teslasoft.android.pchips.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors
import com.google.android.material.textfield.TextInputEditText
import org.teslasoft.android.pchips.R

class HarmonizerFragment : Fragment() {
    private var fieldFrom: TextInputEditText? = null
    private var fieldTo: TextInputEditText? = null
    private var harmonizeWithPrimary: CheckBox? = null
    private var sourceColor: ConstraintLayout? = null
    private var harmonizedColor: ConstraintLayout? = null
    private var primaryColor: ConstraintLayout? = null
    private var sourceColorText: TextView? = null
    private var harmonizedColorText: TextView? = null
    private var primaryColorText: TextView? = null
    private var btnHarmonize: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_harmonizer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fieldFrom = view.findViewById(R.id.field_from)
        fieldTo = view.findViewById(R.id.field_to)
        harmonizeWithPrimary = view.findViewById(R.id.harmonize_with_primary)
        sourceColor = view.findViewById(R.id.source_color)
        harmonizedColor = view.findViewById(R.id.harmonized_color)
        primaryColor = view.findViewById(R.id.primary_color)
        sourceColorText = view.findViewById(R.id.source_color_text)
        harmonizedColorText = view.findViewById(R.id.harmonized_color_text)
        primaryColorText = view.findViewById(R.id.primary_color_text)
        btnHarmonize = view.findViewById(R.id.btn_harmonize)

        fieldFrom?.setText(requireActivity().getSharedPreferences("pchips", 0).getString("colorFrom", "#000000"))
        fieldTo?.setText(requireActivity().getSharedPreferences("pchips", 0).getString("colorTo", "#000000"))
        harmonizeWithPrimary?.isChecked = requireActivity().getSharedPreferences("pchips", 0).getBoolean("harmonizeWithPrimary", false)

        fieldFrom?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                harmonizeColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* unused */ }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* unused */ }
        })

        fieldTo?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                harmonizeColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /* unused */ }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /* unused */ }
        })

        harmonizeWithPrimary?.setOnCheckedChangeListener { _, _ ->
            harmonizeColor()
        }

        harmonizeColor()

        btnHarmonize?.setOnClickListener {
            harmonizeColor()
        }
    }

    private fun validateFields(): Boolean {
        return fieldFrom?.text.toString().isNotEmpty() && fieldTo?.text.toString().isNotEmpty()
    }

    private fun harmonizeColor() {
        if (validateFields()) {
            val from = fieldFrom?.text.toString()
            val to = fieldTo?.text.toString()
            val harmonizeWithPrimary = harmonizeWithPrimary?.isChecked

            try {
                val color = hexStringToColor(from)
                val with = hexStringToColor(to)
                val harmonized = harmonizeColor(color, with, false)
                val primary = harmonizeColor(color, with, true)
                applyColorToCards(color, if (harmonizeWithPrimary!!) primary else harmonized,
                    ResourcesCompat.getColor(resources, R.color.colorPrimary, requireActivity().theme))
            } catch (_: Exception) { /* unused */ }

            requireActivity().getSharedPreferences("pchips", 0).edit().putString("colorFrom", from).apply()
            requireActivity().getSharedPreferences("pchips", 0).edit().putString("colorTo", to).apply()
            requireActivity().getSharedPreferences("pchips", 0).edit().putBoolean("harmonizeWithPrimary", harmonizeWithPrimary!!).apply()
        }
    }

    private fun harmonizeColor(color: Int, with: Int, primary: Boolean): Int {
        return if (primary) {
            MaterialColors.harmonizeWithPrimary(requireActivity(), color)
        } else {
            MaterialColors.harmonize(color, with)
        }
    }

    private fun invertColor(color: Int): Int {
        return (0xFFFFFFFF.toInt() - color + 0xFF000000).toInt()
    }

    private fun hexStringToColor(hex: String): Int {
        val tmp = hex.replace("#", "")
        return (tmp.toInt(16) + 0xFF000000).toInt()
    }

    private fun applyColorToCards(color: Int, harmonized: Int, primary: Int) {
        sourceColor?.backgroundTintList = ColorStateList.valueOf(color)
        harmonizedColor?.backgroundTintList = ColorStateList.valueOf(harmonized)
        primaryColor?.backgroundTintList = ColorStateList.valueOf(primary)
        sourceColorText?.text = String.format("Source color: #%06X", 0xFFFFFF and color)
        harmonizedColorText?.text = String.format("Harmonized color: #%06X", 0xFFFFFF and harmonized)
        primaryColorText?.text = String.format("Primary Color: #%06X", 0xFFFFFF and primary)
        sourceColorText?.setTextColor(invertColor(color))
        harmonizedColorText?.setTextColor(invertColor(harmonized))
        primaryColorText?.setTextColor(invertColor(primary))
    }
}