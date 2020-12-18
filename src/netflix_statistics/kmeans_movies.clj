(ns kmeans-movies
  (:require [clojure.data.json :as json]))

(def json-file (slurp "resources/movies.json"))

(def movies (json/read-str json-file :key-fn keyword))

;;;;;;;;;;;;;;;;;;;;;;;;;;;; Kmeans for movies ;;;;;;;;;;;;;;;;;;;;;;;;;;;;
 
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
(defn distance[a b] = (if (< a b) 
                        (- b a) 
                        (- a b)))

;; To do K-means, we need to start with some guesses about where the clusters are
;; I choose: 
;; 1st group is bad ranking group, their rating is closest to the lowest value (min)
;; 2nd group is average ranking group, their rating is closest to the mean value
;; 3rd group is top ranking group, their rating is closest to the highest value (max)

(def min-rating (apply min ratings)) ; min is 0
(def max-rating (apply max ratings)) ; max is 9.3

;mean value:
(defn average-ratings [s] 
  (double (/ (reduce + s) (count s))))

(def ratings-mean (average-ratings ratings))
ratings-mean ; mean is 5.9

;(def guessed-means '(min-rating ratings-mean max-rating ))
(def guessed-means '(0 5.9 9.3))

;; Given a particular point, we want to know which of our means is closest
(defn closest [point means distance]
  (first (sort-by #(distance % point) means)))

(defn point-groups [means data distance]
  (group-by #(closest % means distance) data))

;; our three groups:

(def groups (point-groups guessed-means ratings distance))
(def bad-ranking-group (groups 0)) ;; first group
(def average-ranking-group (groups 5.9)) ;; second group
(def top-ranking-group (groups 9.3)) ;; third group

;; movies in these groups: in popularity-prediction ns (top-ranking, average-ranking, bad-ranking)

;; We can take an average of a group of points
(defn average [& list] (/ (reduce + list) (count list)))

;; So we can take the average of each group, and use it as a new guess for where
;; the clusters are. If a mean doesn't have a group, then we'll leave it where
;; it is.
(defn new-means [average point-groups old-means]
  (for [o old-means]
    (if (contains? point-groups o)
      (apply average (get point-groups o)) o)))

(def groups-average (new-means average (point-groups guessed-means ratings distance) 
                               guessed-means)) ; (2.4421711899791205 5.848244106934228 8.046894138232716)

(def first-group-average (first groups-average)) 
(def second-group-average (second groups-average))
(def third-group-average (last groups-average))

;; (in popularity-prediction namespace are listed movies in these trhee groups)