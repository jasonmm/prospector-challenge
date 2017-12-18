package xyz.jmullin.prospector.entity

import com.badlogic.gdx.graphics.Color
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.div
import xyz.jmullin.drifter.extensions.plus
import xyz.jmullin.drifter.extensions.times
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Visualizer
import xyz.jmullin.prospector.game.Coord

object PlotPings : Entity2D() {
    data class QueuedPing(val coord: Coord, var delay: Float = (Prospector.PingDelay*Prospector.delayMultiplier)/1000f)
    private val bestValues = mutableMapOf<String, Int>()
    private val pingQueues = mutableMapOf<String, MutableList<QueuedPing>>()

    override fun update(delta: Float) {
        val scale = Visualizer.plotVisualizer.mapSize / Prospector.MapSize.toFloat()

        repeat(maxOf(1, (1f/Prospector.delayMultiplier).toInt())) {
            pingQueues.forEach { playerName, queue ->
                queue.firstOrNull()?.let { first ->
                    first.delay -= delta / Prospector.delayMultiplier
                    if(first.delay <= 0) {
                        val v = V2(first.coord.x, first.coord.y)
                        val ping = Ping(Visualizer.playerColors[playerName] ?: Color.WHITE)
                            .apply { position.set(Visualizer.plotVisualizer.mapOrigin + v*scale) }
                        add(ping)
                        queue.removeAt(0)

                        val value = Visualizer.plotVisualizer.plot.grid[first.coord] ?: 0
                        if(value > bestValues.getOrPut(playerName, { 0 })) {
                            bestValues.put(playerName, value)
                            PlotFlags.setFlag(playerName, first.coord)
                        }
                    }
                }
            }
        }

        super.update(delta)
    }

    fun reset() {
        bestValues.clear()
    }

    fun ping(playerName: String, coord: Coord) {
        pingQueues.getOrPut(playerName, { mutableListOf() })
            .add(QueuedPing(coord))
    }

    fun pingsRemaining(): Boolean {
        return pingQueues.any { it.value.isNotEmpty() }
    }
}