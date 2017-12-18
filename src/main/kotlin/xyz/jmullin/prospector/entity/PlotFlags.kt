package xyz.jmullin.prospector.entity

import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.div
import xyz.jmullin.drifter.extensions.plus
import xyz.jmullin.drifter.extensions.times
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Visualizer
import xyz.jmullin.prospector.game.Coord

object PlotFlags : Entity2D() {
    private val flagPositions = mutableMapOf<String, Flag>()

    fun setFlag(playerName: String, coord: Coord) {
        val flag = flagPositions.getOrPut(playerName, { add(Flag(Visualizer.playerColors[playerName]!!)) })
        val scale = Visualizer.plotVisualizer.mapSize / Prospector.MapSize.toFloat()

        flag.position.set(Visualizer.plotVisualizer.mapOrigin + V2(coord.x, coord.y) * scale)
    }

    fun clear() {
        flagPositions.values.forEach(Flag::remove)
        flagPositions.clear()
    }
}