(ns movies-statistics
 (:require [clojure.data.json :as json]
           [clojure.string :refer [lower-case blank?]]))

;; this file contains statistics for movies.json file, all MOVIES all over the world, with IMDb rating

; load data from json file

(def json-movies (slurp "resources/movies.json"))
(def movies-str (json/read-str json-movies ))

(defn to-keyword
 "Return string as keyword(lower case, with -)." 
  [str]
  (keyword (lower-case 
            (clojure.string/replace str " " "-"))))

 
(defn keywordise
  "Apply to-keyword to all movies in vector."
  [movies]
  (map #(into {} (map (fn [[k v]]
                        (vector (to-keyword k) v))
                      %))
       movies))

(def movies (keywordise (json/read-str json-movies)))

;; Counting all movies
(def num-movies (count movies)) ; 16 744

;; the oldest movie

(def movies-years (map :year movies))
movies-years

(def the-oldest-movie-year (apply min movies-years))
the-oldest-movie-year ;1902

;; the oldest movie/s is/are created in 1902
(defn get-the-oldest-movie 
  "Return the oldest movie."
  []
  (filter #(= (:year %) the-oldest-movie-year) 
          movies))

(get-the-oldest-movie) ; only "A Trip to the Moon"
(count (get-the-oldest-movie)) ; 1 movie

;; the newest movie/s
(def the-newest-movie-year (apply max movies-years))
the-newest-movie-year ;2020

;; the oldest movie/s is/are created in 1902
(defn get-the-newest-movie
  "Returns the newest movie."
 []
  (filter #(= (:year %) the-newest-movie-year) 
          movies))

(get-the-newest-movie) ; we have many movies in 2020
(count (get-the-newest-movie)) ; 147 movies

;; so we have movies from 1902 to 2020 

;; movies on Netflix:
(def movies-netflix (filter #(= (:netflix %) 1) movies))
(def count-netflix (count movies-netflix)) ; 3560

;; movies on Hulu: 
(def movies-hulu (filter #(= (:hulu %) 1) movies))
(def count-hulu (count movies-hulu)) ; 903

;; movies on Disney+:
(def movies-disney(filter #(= (:disney+ %) 1) movies))
(def count-disney (count movies-disney)) ; 564

;; movies on Prime Video
(def movies-prime(filter #(= (:prime-video %) 1) movies)) 
(def count-prime (count movies-prime)) ;; 12 354 movies on Prime Video

;; average runtime(mean)

; check and count nil values
(defn count-nil-vaules
 "Counts nil runtime values." 
  [] 
  (count (filter #(= (:runtime %) "") 
                 movies))) 
(count-nil-vaules); 592 nil ("") values

; we do not need nil values, we choose movies WITH duration
(def movies-no-nil (filter #(not= (:runtime %) "") movies)) 

(def runtimes (map :runtime movies-no-nil))

(defn average-runtime 
  "Returns average runtime."
  []
  (double 
   (/ 
    (reduce + runtimes) 
    (count movies-no-nil)))) ; 93.41

;; I want to check movie deviation from mean, so we need variance and standard deviation

;; 1) Variance
;; variance is the expectation of the squared deviation of a random variable from its mean
;; v= ( (x1-mean)^2+(x2-mean)^2+ ... + (xn-mean)^2 ) / n

(defn variance 
  "A measure of how far a set of numbers is spread out."
  [data]
    (def sqr (fn [x] (* x x)))
    (let [mv (average-runtime)]
      (/
        (reduce +
          (map
            #(sqr (- % mv)) data))
              (count data))))

(defn movies-variance
 "Returns movies runtime variance." 
  []
  (Math/round (variance runtimes))) ; variance is 796

;; 2) Standard deviation

(defn standard-deviation
  "In statistics and probability theory, standard deviation (represented by the symbol sigma, σ)
   shows how much variation or dispersion exists from the average (mean, or expected value).
   A low standard deviation indicates that the data points tend to be very close to the mean,
   whereas high standard deviation indicates that the data points are spread out over a large
   range of values."
  [data]
  (Math/sqrt (variance data)))

(defn movies-standard-deviation
 "Returns movies duration standard deviation." 
  []
  (Math/round (standard-deviation runtimes)))

(movies-standard-deviation) ; the average deviation of the duration of the movies from the average value is 28


;;;;;;;;;;;; Mode ;;;;;;;;;;;;;;;

;; - which duration is most recurring, can be more than one value!

(defn mode 
  "Returns most frequent value in data."
  [data]
  (let [frequency-distribution (frequencies data)
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
(defn movies-mode-duration
 "Returns movies with mode duration." 
  []
  (filter #(= (:runtime %) (nth mode-duration 0)) 
          movies)) 

(movies-mode-duration)
(count (movies-mode-duration)) ;971 movies

;; which language is most recurring:

(def languages (map :language movies))
(def mode-language (mode languages)); its English (only english)

;; we can see which movies have language english
(defn movies-mode-language
  "Returns movies with most recur language."
  []
  (filter #(= (:language %) (nth mode-language 0)) 
          movies)) 

(movies-mode-language)
(count (movies-mode-language)) ; 10 955 movies on English

;; which genre is most recurring:

(def genres (map :genres movies))
(def mode-genre (mode genres)) ; its Drama (only Drama)

;; we can see which movies have genre drama
(defn movies-mode-genre
 "Returns movies with most recur genre." 
  []
  (filter #(= (:genres %) (nth mode-genre 0)) 
          movies)) 

(movies-mode-genre)
(count (movies-mode-genre)) ; 1341 movie Drama

;; which country is most recurring:

(def countries (map :country movies))
(def mode-country (mode countries)); its United States (only United States)

;; we can see which movies have country United States
(defn movies-mode-country
  "Returns movies with most recur country." 
  []
  (filter #(= (:country %) "United States") 
          movies)) 

(movies-mode-country)
(count (movies-mode-country)) ; 8776 movie from United States

;; search all movies or specific platform  
;; title, year, runtime, country, genre

(defn capitalize-words 
  "Capitalize every word in a string."
  [s]
  (->> (clojure.string/split (str s) #"\b") 
       (map clojure.string/capitalize)
       clojure.string/join))

(defn search-by-title 
  "Search movies by title."
  [title movies-passed] 
  (filter #(= (:title %) (capitalize-words title)) 
          movies-passed))

(defn try-convert-string 
  "Convert string to integer (if it is possible)."
  [str]
  (try 
  (Integer/valueOf str)
  (catch Exception e (identity 0))))

(defn search-by-year
 "Search movies by year." 
  [year movies-passed] 
  (filter #(= (:year %) (try-convert-string year)) 
          movies-passed))

(defn search-by-runtime
 "Search movies by runtime." 
  [runtime movies-passed] 
  (filter #(= (:runtime %) (try-convert-string runtime)) 
          movies-passed))

(defn search-by-country
 "Search movies by contry." 
  [country movies-passed] 
  (filter #(.contains (:country %) (capitalize-words country)) 
          movies-passed))

(defn search-by-genre
 "Search movies by genre." 
  [genre movies-passed] 
  (filter #(.contains (:genres %) (capitalize-words genre)) 
          movies-passed))
