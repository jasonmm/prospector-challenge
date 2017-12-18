package xyz.jmullin.prospector.entity

import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.gameH
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.string
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Stage

class Title : Entity2D() {
    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {
            string("Nerdery Prospecting Challenge", V2(320f, gameH()-14f), Assets.titleFont, V2(1f, -1f))
        }

        super.render(stage)
    }
}