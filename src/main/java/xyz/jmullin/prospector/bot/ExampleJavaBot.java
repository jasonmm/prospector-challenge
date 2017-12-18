package xyz.jmullin.prospector.bot;

import org.jetbrains.annotations.NotNull;
import xyz.jmullin.prospector.game.Coord;
import xyz.jmullin.prospector.game.Probe;
import xyz.jmullin.prospector.game.ProspectorBot;

/**
 * Copy this class to start implementing your bot!
 */
public class ExampleJavaBot implements ProspectorBot {
  /**
   * Return the name of your bot.
   */
  @NotNull
  @Override
  public String getName() {
    return "javaBot";
  }

  /**
   * Prospect the plot by calling .query(coord) on the provided probe instance with the desired
   * coordinate to query. Coordinates are in the range of 0 <= x < 512, 0 <= y < 512.
   * Each query will return the value of the plot at the given coordinate. A maximum of 100 queries
   * are allowed per plot; queries after this will return a value of 0.
   * After returning from this method, your score for the plot will be the value of the largest
   * query you made from the plot.
   */
  @Override
  public void prospect(@NotNull Probe probe) {
    probe.query(new Coord(123, 123)); // Query for the value of a coordinate
  }
}