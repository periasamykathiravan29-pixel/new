package com.musiq.app

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HeadphoneState(
    val connected: Boolean,
    val label: String,
)

/**
 * Watches the audio outputs and reports whether wired headphones, a USB headset
 * or a Bluetooth headset/earbuds are connected. Updates the moment one is plugged in or removed.
 */
class HeadphoneMonitor(context: Context) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _state = MutableStateFlow(current())
    val state: StateFlow<HeadphoneState> = _state

    private val callback = object : AudioDeviceCallback() {
        override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>) {
            _state.value = current()
        }

        override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>) {
            _state.value = current()
        }
    }

    fun start() {
        audioManager.registerAudioDeviceCallback(callback, Handler(Looper.getMainLooper()))
        _state.value = current()
    }

    fun stop() {
        audioManager.unregisterAudioDeviceCallback(callback)
    }

    private fun current(): HeadphoneState {
        val outputs = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        val device = outputs.firstOrNull { kindOf(it.type) != null }
            ?: return HeadphoneState(connected = false, label = "Speaker")

        val kind = kindOf(device.type) ?: "Headphones"
        val name = device.productName?.toString()?.takeIf { it.isNotBlank() }
        val label = if (name != null && !name.equals(Build.MODEL, ignoreCase = true)) {
            "$kind · $name"
        } else {
            kind
        }
        return HeadphoneState(connected = true, label = label)
    }

    private fun kindOf(type: Int): String? = when {
        type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> "Wired headphones"
        type == AudioDeviceInfo.TYPE_WIRED_HEADSET -> "Wired headset"
        type == AudioDeviceInfo.TYPE_USB_HEADSET -> "USB headset"
        type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> "Bluetooth"
        type == AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> "Bluetooth"
        Build.VERSION.SDK_INT >= 31 && type == AudioDeviceInfo.TYPE_BLE_HEADSET -> "Bluetooth LE"
        else -> null
    }
}
