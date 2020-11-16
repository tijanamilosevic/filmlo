(ns kmeans-movies
  (:require [clojure.data.json :as json]))

(def json-file (slurp "resources/movies.json"))

(def movies (json/read-str json-file :key-fn keyword))
;(json/read-str json-file :key-fn keyword)

(first movies)
((first movies) :Title)


;;;;;;;;;;;;;;;;;;;;;;;;;;;; Kmeans for movies;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 
; I want to classify movies by IMDb rating
(def movies-ratings (map :IMDb movies))

; check and count nil values
(count (filter #(= (:IMDb %) "") movies)) ; we have 571 nil vaule("") for IMDb rating

;these movies are uncategorised
(def uncategorised-movies (filter #(= (:IMDb %) "") movies))

; we do not need them!
(filter #(not= (:IMDb %) "") movies)
(def movies-no-nil (filter #(not= (:IMDb %) "") movies))

(def ratings (map :IMDb movies-no-nil)) ; no "" values

;; You may be able to see some natural groupings in this data.

;; It's easy enough to say how far one number is from another
(defn distance[a b] = (if (< a b) (- b a) (- a b)))

;; To do K-means, we need to start with some guesses about where the clusters are
;; I choose: 
;; 1st group is bad ranking group, their rating is closest to the lowest value (min)
;; 2nd group is average ranking group, their rating is closest to the mean value
;; 3rd group is top ranking group, their rating is closest to the highest value (max)
(def min-rating (apply min ratings)) ; min is 0
(def max-rating (apply max ratings)) ; max is 9.3

;mean value:
(defn average-ratings [s] (double (/ (reduce + s) (count ratings))))
(def ratings-mean (average-ratings ratings))
ratings-mean ; mean is 5.09

;(def guessed-means '(min-rating ratings-mean max-rating ))
(def guessed-means '(0 5.09 9.3))
;; Given a particular point, we want to know which of our means is closest
(defn closest [point means distance]
  (first (sort-by #(distance % point) means)))

(closest 4   guessed-means distance) ; 5.09 group
(closest 7.8 guessed-means distance) ; 9.3 group


(defn point-groups [means data distance]
  (group-by #(closest % means distance) data))

;; our three groups:

(def groups (point-groups guessed-means ratings distance))
(def first-group-rating (groups 0))
(def second-group-rating (groups 5.09))
(def third-group-rating (groups 9.3))
(type third-group-rating)

;; TO DO:  movies in these groups:


;; We can take an average of a group of points
(defn average [& list] (/ (reduce + list) (count list)))

(average 3.5 8 7 1 6) ; 5.1

;; So we can take the average of each group, and use it as a new guess for where
;; the clusters are. If a mean doesn't have a group, then we'll leave it where
;; it is.
(defn new-means [average point-groups old-means]
  (for [o old-means]
    (if (contains? point-groups o)
      (apply average (get point-groups o)) o)))

(def groups-average (new-means average (point-groups guessed-means ratings distance) guessed-means)) ; (2.1484126984126988 5.589526222836502 7.63678398895792)
(def first-group-average (first groups-average))
(def second-group-average (second groups-average))
(def third-group-average (last groups-average))
