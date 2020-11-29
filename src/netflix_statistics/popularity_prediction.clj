(ns popularity-prediction
  (:require [clojure.data.json :as json])
  (:require [ultra-csv.core :as csv]))

;; the popularity of movies depends on several factors
;; 1) actors popularity
;; 2) streaming platform popularity
;; 3) director popularity
;; 4) IMDb rating
;; 5) Rotten Tomatoes rating

;; movie popularity score is 10 points:
;; 1) actors popularity - 0-not popular, 1-popular
;; 2) streaming platform popularity-  0-not popular, 1-mid-popular, 2-popular, 3-very popular
;; 3) director popularity-  0-not popular, 1-popular
;; 4) IMDb rating-  0-not popular, 1-mid-popular, 2-popular
;; 5) Rotten Tomatoes rating-  0-not popular, 1-mid-popular, 2-popular

;; RANKING LIST:
;; 0,1,2,3,4,5 points - not popular movies
;; 6,7,8,9,10 points- popular movies

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; IMPLEMENTATION ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; load movies csv:
(def movies-csv (csv/read-csv "resources/movies.csv"))
movies-csv

;; representation is like [7748 7749 "March or Die" 1977 "7+" 6.3 nil 0 0 1 0 0 "Dick Richards" "Adventure,Drama,Romance,War" "United Kingdom" "English" 107]
;; so, its not so useful
;; better option is reading from json file:

;; load movies json
(def json-movies (slurp "resources/movies.json"))
(def movies (json/read-str json-movies :key-fn keyword))

;; streaming platform popularity:
;; acording to statista.com most popular streaming platform are:
;; Netflix(3 points), 
;; Hulu(2 points), 
;; Prime video (1 point)
;; and Disney (0 points).


(defn insert-score [s points] 
  (into s [{:score points}]))

(defn insert-score-platform [s points] 
  (into s [{:score_platform points}]))


(def netflix-movies (into [] 
      (map (fn [e] (insert-score-platform e 3)) 
           (into [] 
                 (filter #(= (:Netflix %) 1) movies)))))

netflix-movies

(def hulu-movies (into [] 
      (map (fn [e] (insert-score-platform e 2)) 
           (into [] 
                 (filter #(= (:Hulu %) 1) movies)))))
hulu-movies

(def disney-movies (into [] 
      (map (fn [e] (insert-score-platform e 0)) 
           (into [] 
                 (filter #(= (:Disney+ %) 1) movies)))))

disney-movies

(def prime-movies (into [] 
      (map (fn [e] (insert-score-platform e 1)) 
           (into [] 
                 (filter #(and (= (:Netflix %) 0) (= (:Hulu %) 0) (= (:Disney+ %) 0)) movies)))))

prime-movies

;; merging movies with platforme scores:
(def netflix-hulu (into netflix-movies hulu-movies))
(def disney-prime (into disney-movies prime-movies))
(def movies-with-platform-score (into netflix-hulu disney-prime))
movies-with-platform-score ;; from now, we use this movie data, and we will add other scores :)



;; actors points:

; load json file
(def json-actors (slurp "resources/actors.json"))
(def actors (json/read-str json-actors :key-fn keyword))
actors
; persistent vector
(type actors)

(def actors-rates (map :rating actors))
actors-rates

; (filter #(= (:rating %) "") actors-rates)

(def max-rating (apply min actors-rates))
max-rating ; 299

(def min-rating (apply max actors-rates))
min-rating ; 4875

(def average-rating (double (/ (reduce + actors-rates) (count actors-rates)))) ; 670.464
average-rating

;; if actor raiting is <=average-rating its popular (1 point)
;; >average-rating its not popular (0 points)


(def popular-actors (into [] 
      (map (fn [e] (insert-score e 1)) 
           (into [] 
                 (filter #(<= (:rating %) average-rating) actors)))))

popular-actors

(def not-popular-actors (into [] 
      (map (fn [e] (insert-score e 0)) 
           (into [] 
                 (filter #(> (:rating %) average-rating) actors)))))

not-popular-actors

;; merge two vectors into one- actors with scores
(def actors-score (into popular-actors not-popular-actors))

;; TO DO: add score to movies!



;; director popularity:
;; here is list 100 most popular directors in the world, so is movies director is on this list its 1 point, if not 0 poins

; load json file
(def json-directors (slurp "resources/directors.json"))
(def directors (json/read-str json-directors :key-fn keyword))








;; IMDb rating:
;; I already defined groups in kmeans-movies file, so:
;;  first-group-rating will get 0 points, 
;;  second-group-rating 1 point, 
;;  and third-group-rating 2 points.







;;Rotten Tomatoes rating:
;; according to Rotten Tomatoes web site:
;; >60% - 2 points
;; 30% - 59% - 1 point
;; 0% - 29% - 0 points



