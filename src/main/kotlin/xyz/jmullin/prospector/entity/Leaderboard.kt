package xyz.jmullin.prospector.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import xyz.jmullin.drifter.animation.delay
import xyz.jmullin.drifter.animation.event
import xyz.jmullin.drifter.animation.tween
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.fill
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.drifter.rendering.string
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Stage
import xyz.jmullin.prospector.Visualizer

/**
 * Puzzle visualizer. Displays all players and their current status.
 */
class Leaderboard : Entity2D() {
    var round = 0

    private var scores = emptyMap<String, Int>()
    private var toasts = emptyMap<String, Int>()
    private var playerY = mutableMapOf<String, Float>()
    private val displayScores = mutableMapOf<String, Float>()

    private var toastAlpha = 0f
    var puzzleAlpha = 1f

    private val glyphLayout = GlyphLayout()

    private var initYs = false

    fun applyScores(newScores: Map<String, Int>) {
        clearHooks()
        val oldScores = scores

        delay(0.25f*Prospector.delayMultiplier) {
            toasts = newScores.map { (player, newScore) ->
                val oldScore = oldScores[player] ?: 0
                player to (newScore - oldScore)
            }.toMap()
            toastAlpha = 1f
        } go(this)

        delay(0.5f*Prospector.delayMultiplier) {} then tween(1.5f*Prospector.delayMultiplier) { a ->
            toastAlpha = 1f-a
        } go(this)

        delay(0.6f*Prospector.delayMultiplier) {
            PlotFlags.clear()
        } then tween(0.4f*Prospector.delayMultiplier) { a ->
            puzzleAlpha = 1f-a
        } then event {
            scores = newScores.toMap()
        } then delay(0.25f*Prospector.delayMultiplier) {} then tween(0.2f*Prospector.delayMultiplier) { a ->
            if(round < Prospector.NumPlots) puzzleAlpha = a
        } go(this)

    }

    override fun update(delta: Float) {
        scores.forEach { player, score ->
            displayScores.compute(player, { _, current ->
                val display = current ?: 0f
                if(display < score) minOf(score.toFloat(), display + delta*(1000f/Prospector.delayMultiplier)) else display
            })
        }

        super.update(delta)
    }

    override fun render(stage: RenderStage) {
        val players = scores.keys
        val maxNameWidth: Float = players.map {
            glyphLayout.setText(Assets.uiFont, it)
            glyphLayout.width
        }.max() ?: 0f

        val scoreWidth = 240f

        stage.draw(Stage.Ui) {
            val dY = 64f
            val offsetY = gameH() /2f - (dY*players.size.toFloat())/2f

            fill(V2(0f, gameH() -52f), V2(gameW(), 52f), C(0.1f))

            Assets.uiFont.color = Color.WHITE
            val roundMessage = if(round < Prospector.NumPlots) {
                "Round ${round+1}/${Prospector.NumPlots}"
            } else {
                "Final"
            }
            string(roundMessage, V2(10f, gameH() -14f), Assets.uiFont, V2(1f, -1f))

            players.sortedBy { scores[it] ?: 0 }
                .forEachIndexed { playerI, player ->
                    val targetY = offsetY + dY*playerI
                    val y = if(!initYs) {
                        playerY.put(player, targetY)
                        targetY
                    } else {
                        playerY.compute(player, { _, currentY -> (currentY ?: 0f) + (targetY - (currentY ?: 0f))/10f }) ?: 0f
                    }

                    val score = displayScores[player]?.toInt() ?: 0
                    Assets.uiFont.color = when {
                        round < Prospector.NumPlots -> Color.WHITE
                        playerI == players.size-1 -> {
                            Assets.uiFont.color = Color.YELLOW
                            string("WINNER", V2(scoreWidth + maxNameWidth + 48f, y), Assets.uiFont, V2(1f, 0f))
                            Color.YELLOW
                        }
                        else -> Color.WHITE
                    }
                    string(score.toString(), V2(8f, y), Assets.uiFont, V2(1f, 0f))
                    Assets.tag.color = Visualizer.playerColors[player]!!
                    sprite(Assets.tag, V2(scoreWidth-22f, y).rectCenter(20f))
                    string(player, V2(scoreWidth, y), Assets.uiFont, V2(1f, 0f))

                    val toast = toasts[player] ?: 0
                    Assets.uiFont.color = when {
                        toast > 0 -> Color.GREEN
                        else -> Color.RED
                    }.alpha(toastAlpha)
                    string("+$toast", V2(scoreWidth-90f, y), Assets.uiFont, V2(-1f, 0f))
                }

            initYs = true
        }

        super.render(stage)
    }
}