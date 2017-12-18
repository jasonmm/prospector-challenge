package xyz.jmullin.prospector.game

import jdk.nashorn.internal.objects.NativeArray.forEach
import kotlinx.coroutines.experimental.delay
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Visualizer
import xyz.jmullin.prospector.entity.PlotPings

/**
 * Tournament definition. Creates a set of plots and runs them on start().
 *
 * @param numPlots The number of puzzles to run in the tournament.
 * @param bots List of bot implementations to run against the plots list.
 */
class Tournament(private val numPlots: Int, private val bots: List<ProspectorBot>) {
    suspend fun start(): Map<String, Int> {
        delay(1000)
        if(Prospector.visualize) {
            Visualizer.initPlayers(bots)
        }

        val scores = mutableMapOf<String, Int>()

        repeat(numPlots) { i ->
            if(Prospector.visualize) {
                Visualizer.leaderboard.round = i
            }
            Plot(i).prospect(bots).apply {
                forEach { scores[it.playerName] = scores.getOrDefault(it.playerName, 0) + it.score }

                if(Prospector.visualize) {
                    while(PlotPings.pingsRemaining()) delay((100 * Prospector.delayMultiplier).toLong())
                    Visualizer.leaderboard.applyScores(scores)
                    delay((Prospector.RoundDelay * Prospector.delayMultiplier).toLong())
                }
            }
        }
        if(Prospector.visualize) {
            Visualizer.leaderboard.round = numPlots
        }

        return scores
    }
}