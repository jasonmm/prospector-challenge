package xyz.jmullin.prospector.utility

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import xyz.jmullin.prospector.Prospector
import java.io.File

/**
 * Utility script which packs images into an atlas in the resources directory.
 */
fun main(args: Array<String>) {
    val resourcePath = "image"

    val packLogFile = FileHandle(File("atlas/lastPacked"))

    val lastPacked = if(packLogFile.exists()) packLogFile.readString()?.toLongOrNull() ?: 0L else 0L
    FileHandle(File(resourcePath)).list()?.map { it.lastModified() }?.max()?.let { mostRecent ->
        if(mostRecent > lastPacked) {
            println("Sprite changes detected. Running texture packer.")
            pack(resourcePath)
            packLogFile.writeString(System.currentTimeMillis().toString(), false)
        } else {
            println("No changes to sprite files. Skipping texture packer.")
        }
    }
}

fun pack(path: String) {
    val settings = TexturePacker.Settings()
    settings.maxWidth = 2048
    settings.maxHeight = 2048
    settings.filterMin = Texture.TextureFilter.Linear
    settings.filterMag = Texture.TextureFilter.Linear
    settings.paddingX = 2
    settings.paddingY = 2
    settings.duplicatePadding = true

    TexturePacker.process(settings, path, "atlas", Prospector.Name)
}