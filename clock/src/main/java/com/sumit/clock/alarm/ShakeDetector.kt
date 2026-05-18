package com.sumit.clock.alarm

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    private val threshold: Float = 13f,
    private val debounceMs: Long = 250L,
    private val onShake: () -> Unit
) : SensorEventListener {

    private var lastShakeAt = 0L

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val gForce = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH
        if (gForce > threshold) {
            val now = System.currentTimeMillis()
            if (now - lastShakeAt > debounceMs) {
                lastShakeAt = now
                onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
