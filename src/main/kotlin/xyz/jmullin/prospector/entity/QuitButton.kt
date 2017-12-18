package xyz.jmullin.prospector.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.fill
import xyz.jmullin.drifter.rendering.string
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Stage

class QuitButton : Entity2D() {
    override val bounds: Rectangle
        get() = gameSize().rect(V2(86f, 52f), V2(-1f))

    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {
            val buttonCol = when {
                containsPoint(mouseV()) && Gdx.input.isTouched -> C(0.2f, 0.1f, 0.1f)
                containsPoint(mouseV()) -> C(0.4f, 0.2f, 0.2f)
                else -> C(0.3f, 0.2f, 0.2f)
            }
            fill(bounds, buttonCol)
            fill(bounds.inset(4f), buttonCol.cpy() + 0.1f)
            Assets.uiFont.color = Color.WHITE
            string("Quit", bounds.center, Assets.uiFont, V2(0f))
        }

        super.render(stage)
    }

    override fun touchUp(v: Vector2, pointer: Int, button: Int): Boolean {
        Gdx.app.exit()

        return true
    }
}