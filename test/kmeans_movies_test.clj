(ns kmeans-movies-test
  (:require [midje.sweet :refer [=> facts]]
            [kmeans-movies :refer [distance average-ratings closest guessed-means]]))

(facts "Returns distance two values"
       (distance 5 4)
       => 1
       (distance 4 5)
       => 1)

(facts "Returns average"
       (average-ratings '(1 2 3))
       => 2.0
       (average-ratings '(0 0 0 0))
       => 0.0)

(facts "Returns closest number (0, 5.9 or 9.3)"
       (closest 1 guessed-means distance)
       => 0
       (closest 5 guessed-means distance)
       => 5.9
       (closest 8 guessed-means distance)
       => 9.3)
