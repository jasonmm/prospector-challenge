(ns xyz.jmullin.prospector.bot.jasonbot-algos.JasonBotDivideAndConquer
  (:require [clojure.pprint :as pp]))

(defn probe-points
  [[x y] dimension]
  (let [dim-quarter (quot dimension 4)
        point1 [(- x dim-quarter) (- y dim-quarter)]
        point2 [(+ x dim-quarter) (- y dim-quarter)]
        point3 [(+ x dim-quarter) (+ y dim-quarter)]
        point4 [(- x dim-quarter) (+ y dim-quarter)]]
    [[x y] point1 point2 point3 point4]))

(defn coord [x y] (new xyz.jmullin.prospector.game.Coord x y))
(defn probe-fn [probe] (fn [x y] (.query probe (coord x y))))

(defn divide-and-conquer
  "Prospects by dividing a plot centered at `center` into four sections, probing `center` and the center of each section, and using the largest value as the center of a plot 1/4th the size of the current plot."
  [[x y] dim probe]
  (let [probe        (probe-fn probe)
        probe-values []]
    (loop [center-x    x
           center-y    y
           dimension   dim
           probe-round 1]
      (let [points-to-probe (probe-points [center-x center-y] dimension)
            probe-values    (->> points-to-probe
                                 (map (fn [[x y]] {[x y] (probe x y)}))
                                 (concat probe-values)
                                 (into {}))
            ;; _               (pp/pprint probe-values)
            ;; _               (println "apply just ran")
            new-center      (key (apply max-key val probe-values))]
        ;; (println "inside let")
        ;; (println new-center)
        (if (> 19 probe-round)
          (recur (first new-center) (second new-center) (quot dimension 2) (inc probe-round)))))))

(comment
  (def probe-values '({[256 256] 36} {[128 128] 21} {[384 128] 3} {[384 384] 19} {[128 384] 159}))
  (pp/pprint probe-values)
  (def pv-map (into {} probe-values))
  (apply max-key val [95 477])
  (apply max-key val pv-map)
  )
