# Nerdery JVM Prospecting Challenge

In this challenge your task is to find the most mineral-rich location on various randomly generated
plots of land, by writing a bot which probes coordinates for their mineral value, attempting to
probe mineral-rich areas. Bots will receive points based on the highest mineral value they discovered
in a plot of land.

## The Rules:

1. A tournament consists of 100 rounds.
1. For each round in a tournament:
    1. A plot of land is generated with a random gradient distribution of mineral values for each integer
     coordinate pair (x, y) across the range 0 <= x < 512 and 0 <= y < 512.
    1. Each bot is initiated by passing a Probe class to their 'prospect' method.
        1. During execution of the prospect method, each bot is able to make up to 100 probe queries using
        the Probe class.
        1. Each query specifies a coordinate in the plot, and returns the mineral value at that coordinate.
        1. A maximum of 100 queries are allowed per plot - subsequent queries will return a value of 0.
        1. After returning from the 'prospect' method, each bot receives points equal to the highest mineral
           value they probed for the plot.
1. Points are summed for each bot across the 100 rounds. The bot with the most points wins the tournament.
1. In the event of a tie, a victor will be chosen based on arbitrary and capricious judging of the tied
bot's implementations.

Mineral distributions are generated randomly by combining various 2D samples of Simplex Noise. This results
in graded "hills" of various heights and slopes. Here's an example distribution rendered in the visualizer:
<div style="text-align:center">
<img src="https://i.imgur.com/Up9W7ga.png" width="500" height="500">
</div>

## Implementing Your Bot

Example bots are provided in Kotlin, Java, Clojure and Scala. To implement a bot, open the source root for your
chosen language and copy the example bot from the `xyz.jmullin.prospector.bot` package.

Bots implement the `xyz.jmullin.prospector.game.ProspectorBot` interface. More specific documentation pertaining
to implementation can be found there. Any bots implementing the `ProspectorBot` interface which are located
in the `xyz.jmullin.prospector.bot` package will be automatically included in the tournament.

When you're happy with your bot, submit a PR with your bot implementation on GitHub, or send an e-mail
to me at jmullin@nerdery.com. We'll run the final tournament after the challenge has completed and declare a victor.

## Testing Your Bot

You can run a test tournament in the visualizer or headless mode. In the visualizer you can see all the bot
implementations competing against each other in realtime, and control simulation speed. Headless mode
eschews display and animations, and outputs the results of each puzzle and the final scores to standard out;
it's useful for rapid testing and comparison of new strategies.

`./gradlew run`: Run a tournament in the visualizer.

`./gradlew runHeadless`: Run a text-based tournament.

## Visualizer Features

The visualizer displays the generated plots as a color gradient so you can see where the most mineral-rich
areas are at a glance, and how effective your bot implementations are at finding them. 

Each query made by a bot will display as a color-coded ping on the map. A color-code flag icon will also be placed at the
location of the highest mineral value that has been queried by each player. You can mouse over the plot
to see the exact mineral value at a given coordinate.

## Questions or Concerns?

Direct 'em to Justin Mullin, via Nerdery chat or at jmullin@nerdery.com.
