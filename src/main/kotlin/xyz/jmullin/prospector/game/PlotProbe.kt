package xyz.jmullin.prospector.game

import xyz.jmullin.prospector.entity.PlotPings

class PlotProbe(private val playerName: String, private val plot: Plot) : Probe {
    private val _queries = mutableMapOf<Coord, Int>()
    override var queriesRemaining = 100
        private set(r) { field = r }

    override val queryHistory: Map<Coord, Int> get() = _queries

    override fun query(coord: Coord): Int {
        if(queriesRemaining <= 0) return 0
        queriesRemaining -= 1

        PlotPings.ping(playerName, coord)

        val value = plot.grid[coord] ?: 0

        _queries.put(coord, value)

        return value
    }
}