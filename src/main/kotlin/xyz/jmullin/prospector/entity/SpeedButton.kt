package xyz.jmullin.prospector.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.fill
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Stage
import xyz.jmullin.prospector.game.Playback

class SpeedButton(private val playback: Playback,
                  private val _icon: () -> Sprite) : Entity2D() {
    private val icon by lazy { _icon() }

    override val bounds: Rectangle
        get() = position.rect(V2(36f), V2(1f, -1f))

    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {
            val buttonCol = when {
                containsPoint(mouseV()) && Gdx.input.isTouched -> C(0.1f, 0.1f, 0.1f)
                Prospector.playbackMode == playback || containsPoint(mouseV()) -> C(0.4f, 0.4f, 0.4f)
                else -> C(0.2f, 0.2f, 0.2f)
            }
            fill(bounds, buttonCol)
            fill(bounds.inset(2f), buttonCol.cpy() + 0.1f)
            Assets.uiFont.color = Color.WHITE
            sprite(icon, bounds.inset(8f))
        }

        super.render(stage)
    }

    override fun touchUp(v: Vector2, pointer: Int, button: Int): Boolean {
        Prospector.playbackMode = playback

        return true
    }
}