package xyz.jmullin.prospector.entity

import com.badlogic.gdx.graphics.Color
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.C
import xyz.jmullin.drifter.extensions.FloatMath.min
import xyz.jmullin.drifter.extensions.FloatMath.pow
import xyz.jmullin.drifter.extensions.rectCenter
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.prospector.Assets
import xyz.jmullin.prospector.Prospector
import xyz.jmullin.prospector.Stage

class Ping(private val pingColor: Color) : Entity2D() {
    private var life = 1f

    override fun update(delta: Float) {
        life -= delta / Prospector.delayMultiplier
        if(life < delta) remove()

        super.update(delta)
    }

    override fun render(stage: RenderStage) {
        stage.draw(Stage.Ui) {
            Assets.ping.color = C(pingColor, min(1f, life)*0.1f)
            sprite(Assets.ping, position.rectCenter((0.3f + 0.7f*(1f-life)) * 16f))
            Assets.ping.color = C(pingColor, min(1f, life*2f))
            sprite(Assets.ping, position.rectCenter(3f))
        }

        super.render(stage)
    }
}