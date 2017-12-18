package xyz.jmullin.prospector

import xyz.jmullin.drifter.assets.DrifterAssets

/**
 * Loads game assets and provides handles to them for simplified access.
 */
@Suppress("UNUSED")
object Assets : DrifterAssets("prospector") {
    val uiFont by font("kenyanCoffee")
    val scoreFont by font("upheaval")
    val titleFont by font("minerva")

    val ping by sprite
    val flag by sprite

    val slow by sprite
    val play by sprite
    val fastForward by sprite
    val tripleFast by sprite
}