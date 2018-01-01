(ns xyz.jmullin.prospector.bot.JasonBotDAC
  ""
  (:gen-class :implements [xyz.jmullin.prospector.game.ProspectorBot])
  (:require [clojure.pprint :as pp]))

(def num-probes (atom 0))

(defn probe-points
  [[x y] dimension]
  (let [dim-quarter (quot dimension 4)
        point1 [(- x dim-quarter) (- y dim-quarter)]
        point2 [(+ x dim-quarter) (- y dim-quarter)]
        point3 [(+ x dim-quarter) (+ y dim-quarter)]
        point4 [(- x dim-quarter) (+ y dim-quarter)]]
    [[x y] point1 point2 point3 point4]))

(defn coord [x y] (new xyz.jmullin.prospector.game.Coord x y))
(defn probe-fn
  "Returns a function that probes a given x,y coordinate."
  [probe]
  (fn [x y]
    (swap! num-probes inc)
    (.query probe (coord x y))))

(defn divide-and-conquer
  "Prospects by dividing a plot centered at `center` into four sections, probing `center` and the center of each section, and using the largest value as the center of a plot 1/4th the size of the current plot."
  [[center-x center-y] dimension probe]
  (reset! num-probes 0)
  (let [probe (probe-fn probe)]
    (loop [center       [center-x center-y]
           dimension    dimension
           probe-values []]
      (let [points-to-probe (probe-points center dimension)
            probe-values    (->> points-to-probe
                                 (map (fn [[x y]] [[x y] (probe x y)]))
                                 (concat probe-values)
                                 (sort-by second)
                                 reverse)
            ;; _               (pp/pprint probe-values)
            new-center      (ffirst probe-values)]
        (if (> 100 @num-probes)
          (recur new-center (quot dimension 2) probe-values))))))

(comment
  (def probe-values '({[256 256] 36} {[128 128] 21} {[384 128] 3} {[384 384] 19} {[128 384] 159}))
  (def probe-values '([[256 256] 36] [[128 128] 21] [[384 128] 3] [[384 384] 19] [[128 384] 159]))
  (pp/pprint probe-values)
  (type (first probe-values))
  (def pv-map (into {} probe-values))
  (pp/pprint pv-map)
  (def sorted-pv (reverse (sort-by second probe-values)))
  (pp/pprint sorted-pv)
  (apply max-key val pv-map))

(defn -getName
  "Return the name of your bot."
  [this]
  "JasonBotDAC")

(defn -prospect
  "Prospect the plot by calling .query(coord) on the provided probe instance with the desired
   coordinate to query. Coordinates are in the range of 0 <= x < 512, 0 <= y < 512.
   Each query will return the value of the plot at the given coordinate. A maximum of 100 queries
   are allowed per plot; queries after this will return a value of 0.
   After returning from this method, your score for the plot will be the value of the largest
   query you made from the plot."
  [this probe]
  (divide-and-conquer [256 256] 512 probe))
