(ns xyz.jmullin.prospector.bot.JasonBotDivideAndConquer)

(defn probe-points
  [[x y] dimension]
  (let [dim-quarter (quot dimension 4)
        point1 [(- x dim-quarter) (- y dim-quarter)]
        point2 [(+ x dim-quarter) (- y dim-quarter)]
        point3 [(+ x dim-quarter) (+ y dim-quarter)]
        point4 [(- x dim-quarter) (+ y dim-quarter)]]
    [[x y] point1 point2 point3 point4]))

(defn coord [x y] (new xyz.jmullin.prospector.game.Coord x y))

(defn divide-and-conquer
  "Prospects by dividing a plot centered at `center` into four sections, probing `center` and the center of each section, and using the largest value as the center of a plot 1/4th the size of the current plot."
  [[center-x center-y] dimension probe]
  (loop [center-x    center-x
         center-y    center-y
         dimension   dimension
         probe-round 1]
    (let [points-to-probe (probe-points [center-x center-y] dimension)
          probe-values    (map-indexed (fn [index [x y]] {index (.query probe (coord x y))}) points-to-probe)
          new-center      (nth (key (apply max-key val probe-values)) points-to-probe)]
      (println new-center)
      (if (> 19 probe-round)
        (recur (first new-center) (second new-center) (quot dimension 2) (inc probe-round)))
      )))
