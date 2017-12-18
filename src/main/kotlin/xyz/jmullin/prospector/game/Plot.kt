package xyz.jmullin.prospector.game

import fastnoise.FastNoise
import kotlinx.coroutines.experimental.async
import xyz.jmullin.drifter.extensions.FloatMath.pow
import xyz.jmullin.drifter.extensions.rColor
import xyz.jmullin.drifter.extensions.rFloat
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Visualizer

class Plot(private val id: Int = 0) {
    private val generators = listOf(
        NoiseGenerator(rFloat(0.0025f, 0.0075f), rFloat(0.0f, 0.1f), FastNoise.FractalType.RigidMulti),
        NoiseGenerator(rFloat(0.005f, 0.015f), rFloat(0.1f, 0.2f), FastNoise.FractalType.RigidMulti),
        NoiseGenerator(rFloat(0.0015f, 0.0025f), rFloat(0.4f, 0.5f), FastNoise.FractalType.Billow),
        NoiseGenerator(rFloat(0.0005f, 0.0015f), rFloat(0.3f, 0.4f), FastNoise.FractalType.FBM)
    )

    val terrain = (0 until Prospector.MapSize).flatMap { x ->
        (0 until Prospector.MapSize).map { y ->
            Coord(x, y) to generators.map { it.noiseAt(x, y) }.reduce { a, b -> a + b + a * b }
        }
    }

    private val values = terrain.map { it.first to noiseToValue(pow(it.second, 5f)) }

    private fun noiseToValue(density: Float) = (density * 1000f).toInt()

    val grid = values.toMap()

    suspend fun prospect(bots: List<ProspectorBot>): List<PlotResult> {
        if(Prospector.visualize) {
            Visualizer.newPlot(this)
        }
        println("Plot ${id+1}")

        return bots.map { bot ->
            val probe = PlotProbe(bot.name, this)
            async {
                try {
                    bot.prospect(probe)
                } catch (e: Exception) {

                }
            }.await()

            PlotResult(bot.name, probe.queryHistory.map { it.value }.max() ?: 0)
        }.apply {
            println(joinToString("\n") { "\t${it.playerName}: ${it.score}" })
        }
    }
}