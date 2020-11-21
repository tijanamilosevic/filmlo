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

;; I want to check movie deviation from mean, so we need variance and standard deviation

;; 1) Variance
;; variance is the expectation of the squared deviation of a random variable from its mean
;; v= ( (x1-mean)^2+(x2-mean)^2+ ... + (xn-mean)^2 ) / n

(defn variance 
  "A measure of how far a set of numbers is spread out."
  [data]
    (def sqr (fn [x] (* x x)))
    (let [mv average-runtime]
      (/
        (reduce +
          (map
            #(sqr (- % mv)) data))
              (count data))))

(Math/round (variance runtimes)) ; variance is 796

;; 2) Standard deviation

(defn standard-deviation
  "In statistics and probability theory, standard deviation (represented by the symbol sigma, σ)
   shows how much variation or dispersion exists from the average (mean, or expected value).
   A low standard deviation indicates that the data points tend to be very close to the mean,
   whereas high standard deviation indicates that the data points are spread out over a large
   range of values."
  [data]
  (Math/sqrt (variance data)))

(Math/round (standard-deviation runtimes)) ;; the average deviation of the duration of the films from the average value is 28

;;;;;;;;;;;; Mode ;;;;;;;;;;;;;;;

;; - which duration is most recurring, can be more than one value!

(defn mode [coll]
  (let [frequency-distribution (frequencies coll)
        sorted (sort-by
                 (comp - second) frequency-distribution)
        mxfreq (second (first sorted))]
    (map first
  (take-while
    (fn [[val freq]]
      (= mxfreq freq)) sorted))))

(def mode-duration (mode runtimes)); its 90
mode-duration
;; we can see which movies have duration 90
(def movies-mode-duration (filter #(= (:Runtime %) 90) movies)) 
movies-mode-duration
(count movies-mode-duration) ;971 movies

;; which language is most recurring:

(def languages (map :Language movies))
(mode languages) ; its English (only english)
;; we can see which movies have language english
(def movies-mode-language (filter #(= (:Language %) "English") movies)) 
movies-mode-language
(count movies-mode-language) ; 10 955 movies on English

;; which genre is most recurring:

(def genres (map :Genres movies))
(mode genres) ; its Drama (only Drama)
;; we can see which movies have genre drama
(def movies-mode-genre (filter #(= (:Genres %) "Drama") movies)) 
movies-mode-genre
(count movies-mode-genre) ; 1341 movie Drama

;; which county is most recurring:

(def countries (map :Country movies))
(mode countries) ; its United States (only United States)
;; we can see which movies have country United States
(def movies-mode-country (filter #(= (:Country %) "United States") movies)) 
movies-mode-country
(count movies-mode-country) ; 8776 movie from United States

  



