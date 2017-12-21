(ns xyz.jmullin.prospector.bot.JasonBot
  ""
  (:gen-class :implements [xyz.jmullin.prospector.game.ProspectorBot])
  (:require [xyz.jmullin.prospector.bot.JasonBotDivideAndConquer :as dac]))


(defn -getName
  "Return the name of your bot."
  [this]
  "JasonBot")

(defn -prospect
  "Prospect the plot by calling .query(coord) on the provided probe instance with the desired
   coordinate to query. Coordinates are in the range of 0 <= x < 512, 0 <= y < 512.
   Each query will return the value of the plot at the given coordinate. A maximum of 100 queries
   are allowed per plot; queries after this will return a value of 0.
   After returning from this method, your score for the plot will be the value of the largest
   query you made from the plot."
  [this probe]
  (dac/divide-and-conquer [256 256] 512 probe)
  ;; (comment Query for the value of a coordinate.)
  ;; (.query probe (new xyz.jmullin.prospector.game.Coord 123 123))
  )
