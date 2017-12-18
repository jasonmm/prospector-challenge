package xyz.jmullin.prospector

import xyz.jmullin.drifter.rendering.DrawStage

/**
 * Drifter render stage definitions.
 */
object Stage {
    val Ui by lazy { DrawStage("draw") }
}