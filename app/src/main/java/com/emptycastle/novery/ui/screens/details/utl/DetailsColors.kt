package com.emptycastle.novery.ui.screens.details.util

import com.emptycastle.novery.ui.theme.DawnGoldInk
import com.emptycastle.novery.ui.theme.DawnLilacInk
import com.emptycastle.novery.ui.theme.DawnPeachInk
import com.emptycastle.novery.ui.theme.DawnRoseInk
import com.emptycastle.novery.ui.theme.DawnSageInk
import com.emptycastle.novery.ui.theme.DawnSkyInk
import com.emptycastle.novery.ui.theme.DawnError

/**
 * Color palette used throughout the Details screen.
 * Brand accents follow Dawn's warm/pastel family; status meanings remain distinct.
 */
object DetailsColors {
    val Pink = DawnRoseInk
    val Success = DawnSageInk
    val Warning = DawnGoldInk
    val Dropped = DawnError

    val StatusReading = DawnSkyInk
    val StatusSpicy = DawnPeachInk
    val StatusCompleted = DawnSageInk
    val StatusOnHold = DawnGoldInk
    val StatusPlanToRead = DawnLilacInk
    val StatusDropped = DawnError
}
