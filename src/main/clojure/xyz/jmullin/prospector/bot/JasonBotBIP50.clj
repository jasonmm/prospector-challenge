(ns xyz.jmullin.prospector.bot.JasonBotBIP50
  ""
  (:gen-class :implements [xyz.jmullin.prospector.game.ProspectorBot])
  (:require [clojure.pprint :as pp]))

(def num-probes (atom 0))
(def delta-size 50)
(def deltas (filter (fn [[dx dy]] (or (= dx 0) (= dy 0))) (drop 1 (for [x [0 delta-size (- delta-size)] y [0 (- delta-size) delta-size]] [x y]))))
(def initial-probe-points (for [x (range 50 500 100) y (range 50 500 100)] [x y]))
;; (defn initial-probe-points [[50 50] [150 50] [250 50] [350 50] [450 50]
;;                             [50 150] [150 150] [250 150] [350 150] [450 150]
;;                             [50 250] [150 250] [250 250] [350 250] [450 250]
;;                             [50 350] [150 350] [250 350] [350 350] [450 350]
;;                             [50 450] [150 450] [250 450] [350 450] [450 450]])


(defn coord [x y] (new xyz.jmullin.prospector.game.Coord x y))
(defn probe-fn
  "Returns a function that probes a given x,y coordinate."
  [probe]
  (fn [x y]
    (swap! num-probes inc)
    (.query probe (coord x y))))

(defn coord-compass-points
  "Returns a sequence of the north, east, south, west points from the given x,y coordinate using the `deltas`."
  [[x y]]
  (map (fn [[dx dy]] [(+ x dx) (+ y dy)]) deltas))

(defn calc-new-probe-points
  [prev-points]
  (mapcat (fn [[coord value]] (coord-compass-points coord)) prev-points))

(comment
  (for [x (range 50 500 100) y (range 50 500 100)] [x y])
  (def deltas (filter (fn [[dx dy]] (or (= dx 0) (= dy 0)))(drop 1 (for [x [0 10 -10] y [0 -10 10]] [x y]))))
  (def compass-points (map (fn [[dx dy]] [(+ 100 dx) (+ 200 dy)]) deltas))
  (mapcat (fn [x] compass-points) [1 2 3 4])
  (def nested [[[1 2] [2 3]] [[10 11] [20 21]]])
  (flatten nested)
  )

(defn -getName
  "Return the name of your bot."
  [this]
  "JasonBotBIP50")

(defn -prospect
  "Prospect the plot by calling .query(coord) on the provided probe instance with the desired
   coordinate to query. Coordinates are in the range of 0 <= x < 512, 0 <= y < 512.
   Each query will return the value of the plot at the given coordinate. A maximum of 100 queries
   are allowed per plot; queries after this will return a value of 0.
   After returning from this method, your score for the plot will be the value of the largest
   query you made from the plot."
  [this probe]
  (let [probe (probe-fn probe)]
    (reset! num-probes 0)
    (loop [points-to-probe initial-probe-points]
      (if (> 100 @num-probes)
        (recur (->> points-to-probe
                    (map (fn [[x y]] [[x y] (probe x y)]))
                    (sort-by second)
                    reverse
                    (take 5)
                    calc-new-probe-points))))))
