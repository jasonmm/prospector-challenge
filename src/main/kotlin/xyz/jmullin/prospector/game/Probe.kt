package xyz.jmullin.prospector.game

/**
 * Interface for probing a plot for value.
 */
interface Probe {
    /**
     * The number of queries still available from this probe. After all queries are used,
     * subsequent invocations of .query(coord) will return 0.
     */
    val queriesRemaining: Int

    /**
     * Maintains a list of the previous query coordinates and values.
     */
    val queryHistory: Map<Coord, Int>

    /**
     * Queries a given coordinate and returns the value. If a coordinate outside of the bounds
     * of the plot is specified, the value returned will be 0.
     */
    fun query(coord: Coord): Int
}