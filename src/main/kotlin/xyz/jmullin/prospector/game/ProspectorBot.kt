package xyz.jmullin.prospector.game

interface ProspectorBot {
    /**
     * Return the name of your bot.
     */
    val name: String

    /**
     * Prospect the plot by calling .query(coord) on the provided probe instance with the desired
     * coordinate to query. Coordinates are in the range of 0 <= x < 512, 0 <= y < 512.
     * Each query will return the value of the plot at the given coordinate. A maximum of 100 queries
     * are allowed per plot; queries after this will return a value of 0.
     *
     * After returning from this method, your score for the plot will be the value of the largest
     * query you made from the plot.
     */
    fun prospect(probe: Probe)
}