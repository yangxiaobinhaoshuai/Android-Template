package me.yangxiaobin.colors

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * @see https://material.io/design/color/the-color-system.html#tools-for-picking-colors
 */
enum class HexColors(val hexVal: String) {

    WHITE("#FFFFFF"),
    BLACK("#000000"),

    // Red
    RED_50("#FFEBEE"),
    RED_100("#FFCDD2"),
    RED_200("#EF9A9A"),
    RED_300("#E57373"),
    RED_400("#EF5350"),
    RED_500("#F44336"),
    RED_600("#E53935"),
    RED_700("#D32F2F"),
    RED_800("#C62828"),
    RED_900("#B71C1C"),

    RED_A100("#FF8A80"),
    RED_A200("#FF5252"),
    RED_A400("#FF1744"),
    RED_A700("#D50000"),

    // Blue
    BLUE_50("#E3F2FD"),
    BLUE_100("#BBDEFB"),
    BLUE_200("#90CAF9"),
    BLUE_300("#64B5F6"),
    BLUE_400("#42A5F5"),
    BLUE_500("#2196F3"),
    BLUE_600("#1E88E5"),
    BLUE_700("#1976D2"),
    BLUE_800("#1565C0"),
    BLUE_900("#0D47A1"),

    BLUE_A100("#82B1FF"),
    BLUE_A200("#448AFF"),
    BLUE_A400("#2979FF"),
    BLUE_A700("#2962FF"),

    // Yellow
    YELLOW_50("#FFFDE7"),
    YELLOW_100("#FFF9C4"),
    YELLOW_200("#FFF59D"),
    YELLOW_300("#FFF176"),
    YELLOW_400("#FFEE58"),
    YELLOW_500("#FFEB3B"),
    YELLOW_600("#FDD835"),
    YELLOW_700("#FBC02D"),
    YELLOW_800("#F9A825"),
    YELLOW_900("#F57F17"),

    YELLOW_A100("#FFFF8D"),
    YELLOW_A200("#FFFF00"),
    YELLOW_A400("#FFEA00"),
    YELLOW_A700("#FFD600"),

}

@get:ColorInt
val HexColors.colorInt: Int get() = Color.parseColor(this.hexVal)


