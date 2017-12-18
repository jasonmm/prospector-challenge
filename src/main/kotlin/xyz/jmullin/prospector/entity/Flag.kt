package xyz.jmullin.prospector.entity

import com.badlogic.gdx.graphics.Color
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.entity.EntityContainer2D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.alpha
import xyz.jmullin.drifter.extensions.minus
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Stage

class Flag(private val flagColor: Color) : Entity2D() {
    override fun create(container: EntityContainer2D) {
        depth = -100f

        super.create(container)
    }

    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {
            Assets.flag.color = flagColor
            sprite(Assets.flag, position - V2(3f, 0f), V2(16f))
        }

        super.render(stage)
    }
}