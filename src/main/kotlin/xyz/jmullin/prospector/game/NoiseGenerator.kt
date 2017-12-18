package xyz.jmullin.prospector.game

import fastnoise.FastNoise
import xyz.jmullin.drifter.extensions.rInt

class NoiseGenerator(private val frequency: Float,
                             private val amplitude: Float,
                             private val fractalType: FastNoise.FractalType) {
    private val gen = FastNoise(rInt(0, 10000)).apply {
        SetFrequency(frequency)
        SetNoiseType(FastNoise.NoiseType.Simplex)
        SetFractalType(fractalType)
    }

    fun noiseAt(x: Int, y: Int): Float {
        return (gen.GetNoise(x.toFloat(), y.toFloat())+1f)/2f * amplitude
    }
}
