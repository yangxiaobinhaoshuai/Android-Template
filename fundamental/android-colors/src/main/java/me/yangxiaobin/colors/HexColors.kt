package me.yangxiaobin.colors

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * @see https://material.io/design/color/the-color-system.html#tools-for-picking-colors
 */
enum class HexColors(val hexVal: String) {

    WHITE("#FFFFFF"),
    BLACK("#000000"),

    /**
     *  Red
     */
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

    /**
     * Pink
     */
    PINK_50("#FCE4EC"),
    PINK_100("#F8BBD0"),
    PINK_200("#F48FB1"),
    PINK_300("#F06292"),
    PINK_400("#EC407A"),
    PINK_500("#E91E63"),
    PINK_600("#D81B60"),
    PINK_700("#C2185B"),
    PINK_800("#AD1457"),
    PINK_900("#880E4F"),
    PINK_A100("#FF80AB"),
    PINK_A200("#FF4081"),
    PINK_A400("#F50057"),
    PINK_A700("#C51162"),

    /***
     *  Blue
     */
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

    /**
     * Yellow
     */
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

    /**
     * Green
     */

    GREEN_50("#E8F5E9"),
    GREEN_100("#C8E6C9"),
    GREEN_200("#A5D6A7"),
    GREEN_300("#81C784"),
    GREEN_400("#66BB6A"),
    GREEN_500("#4CAF50"),
    GREEN_600("#43A047"),
    GREEN_700("#388E3C"),
    GREEN_800("#2E7D32"),
    GREEN_900("#1B5E20"),
    GREEN_A100("#B9F6CA"),
    GREEN_A200("#69F0AE"),
    GREEN_A400("#00E676"),
    GREEN_A700("#00C853"),

    /**
     * Gray
     */
    GRAY_50("#FAFAFA"),
    GRAY_100("#F5F5F5"),
    GRAY_200("#EEEEEE"),
    GRAY_300("#E0E0E0"),
    GRAY_400("#BDBDBD"),
    GRAY_500("#9E9E9E"),
    GRAY_600("#757575"),
    GRAY_700("#616161"),
    GRAY_800("#424242"),
    GRAY_900("#212121"),

    /**
     * purple
     */
    PURPLE_50("#F3E5F5"),
    PURPLE_100("#E1BEE7"),
    PURPLE_200("#CE93D8"),
    PURPLE_300("#BA68C8"),
    PURPLE_400("#AB47BC"),
    PURPLE_500("#9C27B0"),
    PURPLE_600("#8E24AA"),
    PURPLE_700("#7B1FA2"),
    PURPLE_800("#6A1B9A"),
    PURPLE_900("#4A148C"),
    PURPLE_A100("#EA80FC"),
    PURPLE_A200("#E040FB"),
    PURPLE_A400("#D500F9"),
    PURPLE_A700("#AA00FF"),
}

@get:ColorInt
val HexColors.colorInt: Int
    get() = try {
        Color.parseColor(this.hexVal)
    } catch (e: Exception) {
        e.printStackTrace()
        throw IllegalArgumentException("Can't covert $this to colorInt")
    }


