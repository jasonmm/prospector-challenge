package xyz.jmullin.prospector.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.entity.EntityContainer2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.extensions.FloatMath.clamp
import xyz.jmullin.drifter.extensions.FloatMath.pow
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.fill
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.drifter.rendering.string
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Stage
import xyz.jmullin.prospector.game.Coord
import xyz.jmullin.prospector.game.Plot

class PlotVisualizer(var plot: Plot) : Entity2D() {
    var initialized = false
    private var plotSprite = Sprite()

    private var renderedPlot: Plot? = null

    private lateinit var pixmap: Pixmap
    val mapSize = V2((gameSize()-V2(0f, 36f)-50f).minComponent)
    val mapOrigin = V2(435f, 18f)

    override fun create(container: EntityContainer2D) {
        pixmap = Pixmap(Prospector.MapSize, Prospector.MapSize, Pixmap.Format.RGBA8888)

        super.create(container)
    }

    override fun update(delta: Float) {
        if(plot != renderedPlot) {
            renderedPlot = plot

            val maxTerrain = plot.terrain.map { it.second }.max() ?: 0f
            plot.terrain.forEach { (v, noise) ->
                val normalized = noise / maxTerrain
                val i = pow(normalized, 8f) * maxTerrain * 3f
                pixmap.setColor(C(0f)
                    .lerp(C(0.3f, 0.3f, 0.2f), clamp(i, 0f, 1f))
                    .lerp(C(0.6f, 0.6f, 0.1f), clamp(i-1, 0f, 1f))
                    .lerp(C(0.7f, 0.7f, 0.7f), clamp(i-2f, 0f, 1f)))
                pixmap.drawPixel(v.x, v.y)
            }

            plotSprite = Sprite(Texture(pixmap))
        }

        super.update(delta)
    }

    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {
            fill(mapOrigin-4f, mapSize+8f, Color.DARK_GRAY)

            if(initialized) {
                sprite(plotSprite, mapOrigin + V2(0f, mapSize.y), mapSize * V2(1f, -1f))

                val gridV = (((mouseV() - mapOrigin)/mapSize)*Prospector.MapSize.toFloat()).snap().clamp(0f, Prospector.MapSize.toFloat())
                plot.grid[Coord(gridV.xI, gridV.yI)]?.let { value ->
                    string(value.toString(), V2(gameW()-3f, 3f), Assets.uiFont, V2(-1f, 1f))
                }
            } else {
                fill(mapOrigin, mapSize, Color.BLACK)
            }
        }

        super.render(stage)
    }

}