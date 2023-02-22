package me.yangxiaobin.android.kotlin.codelab.ext.device

import android.annotation.SuppressLint
import android.os.Build

/**
 * 判断设备厂商
 */

enum class Rom(val propKey: String) {
    UNKNOW(""),
    MIUI("ro.miui.ui.version.name"),
    EMUI("ro.build.version.emui"),
    SAMSUNG(""),
    OPPO("ro.build.version.opporom"),
    VIVO("ro.vivo.os.version"),
    FLYME(""),
    ONE_PLUS(""),
    MEI_TU(""),
    SMARTISAN("ro.smartisan.version"),
}

private var sRomName = ""

public fun getRomName(): String = sRomName.ifEmpty {
    Rom.values()
        .find { checkRom(it.name) }
        ?.name
        ?.also { sRomName = it }
        ?: Rom.UNKNOW.name
}

public fun isMiui(): Boolean = checkRom(Rom.MIUI.name).also { if (it) sRomName = Rom.MIUI.name }

public fun isEmui(): Boolean = checkRom(Rom.EMUI.name).also { if (it) sRomName = Rom.EMUI.name }

public fun isSamsung(): Boolean = checkRom(Rom.SAMSUNG.name).also { if (it) sRomName = Rom.SAMSUNG.name }

public fun isOppo(): Boolean = checkRom(Rom.OPPO.name).also { if (it) sRomName = Rom.OPPO.name }

public fun isVivo(): Boolean = checkRom(Rom.VIVO.name).also { if (it) sRomName = Rom.VIVO.name }

public fun isFlyme(): Boolean = checkRom(Rom.FLYME.name).also { if (it) sRomName = Rom.FLYME.name }

public fun isOnePlus(): Boolean = checkRom(Rom.ONE_PLUS.name).also { if (it) sRomName = Rom.ONE_PLUS.name }

public fun isMeiTu(): Boolean = checkRom(Rom.MEI_TU.name).also { if (it) sRomName = Rom.MEI_TU.name }

public fun isSmartisan(): Boolean = checkRom(Rom.SMARTISAN.name).also { if (it) sRomName = Rom.SMARTISAN.name }


/**
 * Rom 信息是否已知
 */
private fun checkRom(rom: String): Boolean {
    if (sRomName.isNotEmpty()) return sRomName.contains(rom, ignoreCase = true)

    val parsedRom: Rom = Rom.valueOf(rom)

    if (parsedRom.propKey.isNotEmpty()) return !getRomProp(parsedRom.propKey).isNullOrEmpty()

    return when (rom) {
        Rom.FLYME.name -> Build.DISPLAY.contains(rom, true)
        Rom.MEI_TU.name -> Build.MANUFACTURER.contains(rom, true)
        Rom.SAMSUNG.name -> Build.MANUFACTURER.contains(rom, true)
        Rom.ONE_PLUS.name -> Build.MANUFACTURER.contains(rom, true)
        else -> false
    }
}


@SuppressLint("PrivateApi")
private fun getRomProp(key: String): String? {
    return try {
        val clazz = Class.forName("android.os.SystemProperties")
        val method = clazz.getDeclaredMethod("get", String::class.java)
        var properties = method.invoke(clazz, key) as String?
        if (properties.isNullOrEmpty()) {
            val process = Runtime.getRuntime().exec("getprop $key")
            process.inputStream.bufferedReader().use {
                properties = it.readLine()
            }
        }
        properties
    } catch (e: Throwable) {
        null
    }
}
