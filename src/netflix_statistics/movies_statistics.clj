(ns movies-statistics
 (:require [clojure.data.json :as json]))

;; this file contains statistics for movies.json file, all MOVIES all over the world, with IMDb rating

; load data from json file

(def json-file (slurp "resources/movies.json"))

(def movies (json/read-str json-file :key-fn keyword))

(first movies)

;; Counting all movies
(def num-movies (count movies)) ; 16 744

;; the oldest movie

(def movies-years (map :Year movies))
movies-years

(def the-oldest-movie-year (apply min movies-years))
the-oldest-movie-year ;1902

;; the oldest movie/s is/are created in 1902
(def get-the-oldest-movie (filter #(= (:Year %) the-oldest-movie-year) movies))
get-the-oldest-movie ; only "A Trip to the Moon"
(count get-the-oldest-movie) ; 1 movie

;; the newest movie/s
(def the-newest-movie-year (apply max movies-years))
the-newest-movie-year ;2020

;; the oldest movie/s is/are created in 1902
(def get-the-newest-movie (filter #(= (:Year %) the-newest-movie-year) movies))
get-the-newest-movie ; we have many movies in 2020
(count get-the-newest-movie) ; 147 movies

;; so we have movies from 1902 to 2020 

;; movies on Netflix:
(def movies-netflix (filter #(= (:Netflix %) 1) movies))
(def count-netflix (count movies-netflix)) ; 3560

;; movies on Hulu: 
(def movies-hulu (filter #(= (:Hulu %) 1) movies))
(def count-hulu (count movies-hulu)) ; 903

;; movies on Disney+:
(def movies-disney(filter #(= (:Disney+ %) 1) movies))
(def count-disney (count movies-disney)) ; 564

;; movies on Prime Video 
(def count-prime (- num-movies count-netflix count-hulu count-disney)) ;; 11 717 movies on Prime Video

;; average runtime(mean)

; check and count nil values
(count (filter #(= (:Runtime %) "") movies)) ; 592 nil ("") values
; we do not need nil values, we choose movies WITH duration
(def movies-no-nil (filter #(not= (:Runtime %) "") movies)) 

(def runtimes (map :Runtime movies-no-nil))
runtimes
(type runtimes)

(def average-runtime (double (/ (reduce + runtimes) (count movies-no-nil)))) ; 93.41

;; I want to check deviation of each movie from mean, so we need variance and standard deviation

;; 1) Variance
;; variance is the expectation of the squared deviation of a random variable from its mean
;; v= ( (x1-mean)^2+(x2-mean)^2+ ... + (xn-mean)^2 ) / n

(defn variance [data]
    (def sqr (fn [x] (* x x)))
    (let [mv average-runtime]
      (/
        (reduce +
          (map
            #(sqr (- % mv)) data))
              (count data))))

(variance runtimes)



  



