package xyz.jmullin.prospector

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.reflections.Reflections
import xyz.jmullin.drifter.application.DrifterGame
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.xI
import xyz.jmullin.drifter.extensions.yI
import xyz.jmullin.prospector.game.Playback
import xyz.jmullin.prospector.game.ProspectorBot
import xyz.jmullin.prospector.game.Tournament

/**
 * Main entry point to the application.
 */
fun main(args: Array<String>) {
    val reflections = Reflections("xyz.jmullin.prospector.bot")
    val bots = reflections.getSubTypesOf(ProspectorBot::class.java).mapNotNull {
        if(!it.isInterface && !it.name.startsWith("Example")) {
            it.newInstance()
        } else null
    }

    val tournament = Tournament(Prospector.NumPlots, bots)

    if(!args.contains("headless")) {
        Prospector.visualize = true

        val config = Lwjgl3ApplicationConfiguration()

        config.setTitle(Prospector.Name)
        config.setWindowedMode(Prospector.Size.xI, Prospector.Size.yI)
        config.setResizable(false)

        config.useOpenGL3(true, 3, 3)

        config.useVsync(true)
        config.setIdleFPS(60)

        Lwjgl3Application(Prospector(tournament), config)
    } else {
        runBlocking { runTournament(tournament) }
    }
}

class Prospector(private val tournament: Tournament) : DrifterGame(Name, Assets) {
    companion object {
        val Name = "prospector"
        val Size = V2(1000f, 636f)

        var visualize = false

        val NumPlots = 100
        val MapSize = 512

        val PingDelay = 20L
        val RoundDelay = 1000

        var playbackMode: Playback = Playback.Play
        val delayMultiplier: Float
            get() = playbackMode.delayMultiplier
    }

    override fun create() {
        super.create()

        launch { runTournament(tournament) }
        setScreen(Visualizer)
    }
}

/**
 * Run a tournament with the specified number of plots, and print the final results.
 */
suspend fun runTournament(tournament: Tournament) {
    val scores = tournament.start()
    println("\nFinal scores:")
    println(scores.toList().sortedBy {
        -it.second
    }.joinToString("\n") {
        "\t${it.first}: ${it.second}"
    })
}
