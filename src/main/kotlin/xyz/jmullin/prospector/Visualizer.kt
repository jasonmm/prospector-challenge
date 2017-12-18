package xyz.jmullin.prospector

import com.badlogic.gdx.graphics.Color
import xyz.jmullin.drifter.application.DrifterScreen
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.prospector.entity.QuitButton
import xyz.jmullin.prospector.entity.SpeedButton
import xyz.jmullin.prospector.Stage.Ui
import xyz.jmullin.prospector.entity.*
import xyz.jmullin.prospector.game.Playback
import xyz.jmullin.prospector.game.Plot
import xyz.jmullin.prospector.game.ProspectorBot

/**
 * LibGDX screen definition for rendering the visualizer.
 */
object Visualizer : DrifterScreen() {
    private val Colors = listOf(
        Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA
    ) + (0 until 100).map { rColor(0.5f, 1f) }

    private var plot = Plot()
    var plotVisualizer = PlotVisualizer(plot)
    val leaderboard = Leaderboard()

    val playerColors = mutableMapOf<String, Color>()

    fun initPlayers(bots: List<ProspectorBot>) {
        playerColors.clear()
        bots.forEachIndexed { i, bot -> playerColors.put(bot.name, Colors[i]) }
    }

    val ui = newLayer2D(2, Prospector.Size, false, Ui) {
        cameraPos = gameSize()/2f

        add(plotVisualizer)
        add(leaderboard)
        add(PlotPings)
        add(PlotFlags)
        add(Title())

        add(QuitButton())

        listOf(
            Assets.slow to Playback.Slow,
            Assets.play to Playback.Play,
            Assets.fastForward to Playback.FastForward,
            Assets.tripleFast to Playback.TripleFast
        ).forEachIndexed { i, (icon, mode) ->
            add(SpeedButton(mode) { icon }).apply {
                position.set(V2(155f+36f*i, gameH()-9f))
            }
        }
    }

    fun newPlot(new: Plot) {
        plotVisualizer.initialized = true
        plot = new
        plotVisualizer.plot = plot
        PlotPings.reset()
    }
}