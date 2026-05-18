package com.sumit.launcher.service

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.sumit.launcher.R

/**
 * Renders the focus check-in card as a system overlay so it can appear on top of
 * any foreground app. Plain Android Views (no Compose) keep the overlay simple
 * — Compose in overlays requires a lot of lifecycle plumbing.
 */
object FocusCheckOverlay {

    private var attached: View? = null

    fun show(context: Context, elapsedMinutes: Int, onContinue: () -> Unit) {
        if (!Settings.canDrawOverlays(context)) return
        if (attached != null) return  // already showing
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager ?: return

        val card = buildCard(context, elapsedMinutes, onContinue)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        runCatching {
            wm.addView(card, params)
            attached = card
        }
    }

    fun hide(context: Context) {
        val view = attached ?: return
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        runCatching { wm?.removeView(view) }
        attached = null
    }

    private fun overlayType(): Int =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

    private fun buildCard(
        context: Context,
        elapsedMinutes: Int,
        onContinue: () -> Unit
    ): View {
        val density = context.resources.displayMetrics.density
        fun dp(value: Int): Int = (value * density).toInt()

        val cardBackground = GradientDrawable().apply {
            cornerRadius = dp(24).toFloat()
            setColor(Color.parseColor("#1E1E1E"))
            setStroke(dp(1), Color.parseColor("#33FFFFFF"))
        }

        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = cardBackground
            setPadding(dp(24), dp(20), dp(24), dp(16))
            // Cap width so it doesn't span huge tablets edge to edge.
            val widthCap = dp(320)
            layoutParams = LinearLayout.LayoutParams(widthCap, LinearLayout.LayoutParams.WRAP_CONTENT)

            addView(TextView(context).apply {
                text = context.getString(R.string.focus_check_title)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(Color.WHITE)
            })

            addView(TextView(context).apply {
                text = context.getString(R.string.focus_check_body, elapsedMinutes)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setTextColor(Color.parseColor("#CCCCCC"))
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.topMargin = dp(10)
                layoutParams = lp
            })

            addView(LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.END
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.topMargin = dp(16)
                layoutParams = lp

                addView(Button(context).apply {
                    text = context.getString(R.string.focus_check_action_step_away)
                    setOnClickListener { onContinue() }
                })
                addView(Button(context).apply {
                    text = context.getString(R.string.focus_check_action_continue)
                    setOnClickListener { onContinue() }
                })
            })
        }
    }
}
