package xyz.jmullin.prospector.game

enum class Playback(val delayMultiplier: Float) {
    Slow(3f),
    Play(1f),
    FastForward(0.25f),
    TripleFast(0.05f)
}