package com.xtree.live.uitl

import java.util.Locale

object KeyboardHeightCompat {


    fun getMinLimitHeight(): Int {
        if (hasPhysicsKeyboard()) {
            return 100
        }
        return 300
    }

    fun hasPhysicsKeyboard(): Boolean {
        val rom = RomUtils.getRomInfo()
        if (rom.name == "blackberry") {
            return rom.model?.toLowerCase(Locale.ROOT)?.contains("bbf100") ?: false
        }
        return false
    }


    fun panelDefaultHeight(defaultHeight : Int): Int {
        if (hasPhysicsKeyboard()) {
            return 705
        }
        return defaultHeight
    }


}