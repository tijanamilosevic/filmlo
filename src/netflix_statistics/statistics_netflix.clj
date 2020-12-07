(ns statistics-netflix
  (:require [clojure.data.json :as json]))

; load data from json file

(def json-file (slurp "resources/netflix_titles.json"))

(def netflix-portfolio (json/read-str json-file :key-fn keyword))

;;;;;;;;;Movies
(def movies (filter #(= (:type %) "Movie") netflix-portfolio))
;;;;;;;;;Tv-shows
(def tvshows (filter #(= (:type %) "TV Show") netflix-portfolio))

;;;;;;;;;count all, moves&tv-shows
(def sum-all (count netflix-portfolio))

;;;;;;;;;count movies
(def sum-movies (count (filter #(= (:type %) "Movie") netflix-portfolio)))

;;;;;;;;;;count Tv-shows
(def sum-tvshows (count (filter #(= (:type %) "TV Show") netflix-portfolio)))

;;;;;;;;;;min duration movie
(def movies-duration (map :duration movies))
(def movies-duration-int (map (fn [e] (Integer. (re-find  #"\d+" e))) movies-duration))

(def min-duration-movie (apply min movies-duration-int))
min-duration-movie ;3

; we need duration like "3 min":
(def get-min-duration-movie (filter #(= (:duration %) (str min-duration-movie " min")) netflix-portfolio))
get-min-duration-movie

;;;;;;;;;;;max duration movie
(def max-duration-movie (apply max movies-duration-int))
max-duration-movie ;312
(def get-max-duration-movie (filter #(= (:duration %) (str max-duration-movie " min")) netflix-portfolio))
get-max-duration-movie

;;;;;;;;;;average duration for movies (min)
((first netflix-portfolio) :duration)
(map :duration movies)
;all movies duration, like "90 min" etc...
;(def movies-duration (map :duration movies))
(type movies-duration) ;it is lazy sequence

; duration is like "90 min", but we want 90 (number):
(defn convertor [s] (Integer. (re-find  #"\d+" s)))

;average function:
(defn average-movies [s] (double (/ (reduce + s) sum-movies)))

(def movies-duration-average (Math/round (average-movies movies-duration-int)))
movies-duration-average ; average is 99 min

;;;;;;;;;;min duration tv shows
(def tvshows-duration (map :duration tvshows))
(def tvshows-duration-int (map (fn [e] (Integer. (re-find  #"\d+" e))) tvshows-duration))

(def min-duration-tvshow (apply min tvshows-duration-int))
min-duration-tvshow ;1 Season

; we need duration like "1 Season":
(def get-min-duration-tvshows (filter #(= (:duration %) (str min-duration-tvshow " Season")) netflix-portfolio))
get-min-duration-tvshows

;;;;;;;;;;;max duration tv shows
(def max-duration-tvshow (apply max tvshows-duration-int))
max-duration-tvshow ;15 seasons
(def get-max-duration-tvshows (filter #(= (:duration %) (str max-duration-tvshow " Seasons")) netflix-portfolio))
get-max-duration-tvshows

;;;;;;;;;;;;;;;;;average duration for tv-shows (seasons)
;(def tvshows-duration (map :duration tvshows))
tvshows-duration
;(def tvshows-duration-int (map (fn [e](Integer. (re-find  #"\d+" e ))) tvshows-duration))
tvshows-duration-int

;average function:
(defn average-tvshows [s] (Math/round (double (/ (reduce + s) sum-tvshows))))
(def tvshows-duration-average (average-tvshows tvshows-duration-int))
tvshows-duration-average ; average is 2 seasons

;;;;;;;;;;;;;;;;;;top country/countries
(def countries (map :country netflix-portfolio))
(sort-by val (frequencies countries))

(defn most-frequent-n [n items]
  (->> items
       frequencies
       (sort-by val)
       reverse
       (take n)
       (map first)))

(defn most-frequent [items]
  (->> items
       frequencies
       (sort-by val)
       reverse
       (take 1)
       (map first)))

(def top-country (most-frequent countries))
top-country ;united states

(def top-5-countries (most-frequent-n 5 countries))
(def top-3-countries (most-frequent-n 3 countries))
top-5-countries
top-3-countries
;;;;;;;;;;;;;;;;;;;;;;;;;;top genre(listed_in)
(def genres (map :listed_in netflix-portfolio))
(sort-by val (frequencies genres))
(def top-genre (most-frequent genres))
top-genre ; Documentaries

(def top-5-genres (most-frequent-n 5 genres))
(def top-3-genres (most-frequent-n 3 genres))
top-5-genres
top-3-genres

;;;;;;;;;;;;;;;;;;;;;;;;;;top rating
(def ratings (map :rating netflix-portfolio))
(sort-by val (frequencies ratings))
(def top-rating (most-frequent ratings))
top-rating ; TV-MA

(def top-5-rating (most-frequent-n 5 ratings))
(def top-3-rating (most-frequent-n 3 ratings))
top-3-rating
top-5-rating

;;;;;;;;;;;;;;;;;;;;;;;;;;top release year
(def years (map :release_year netflix-portfolio))
(sort-by val (frequencies years))
(def top-release-year (most-frequent years))
top-release-year ; 2018

(def top-5-release-years (most-frequent-n 5 years))
(def top-3-release-years (most-frequent-n 3 years))
top-5-release-years
top-3-release-years

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;search:
;;;;;;;;by release year
(defn search-by-year [year] (filter #(= (:release_year %) year) netflix-portfolio))
(search-by-year 1986)

;;;;;;;; less than year
(defn search-by-year-min [year] (filter #(< (:release_year %) year) netflix-portfolio))
(search-by-year-min 2000)

;;;;;;;; grater than year
(defn search-by-year-max [year] (filter #(> (:release_year %) year) netflix-portfolio))
(search-by-year-max 2018)

;;;;;;;;by name
(defn search-by-name [name] (filter #(= (:title %) name) netflix-portfolio))
(search-by-name "Dark")

;;;;;;;;by type
;I already defined movies and tv-shows
movies
tvshows
;(filter #(= (:type %) "Movie") netflix-portfolio)
;(filter #(= (:type %) "TV Show") netflix-portfolio)

;;;;;;;;by country
(defn search-by-country [country] (filter #(= (:country %) country) netflix-portfolio))
(search-by-country "Germany")

;;;;;;;;search by number of seasons for tv shows
(defn search-by-season [season] (filter #(= (:duration %) (str season " Seasons")) tvshows))
(search-by-season 11)

;;;;;;;;search by number of seasons for tv shows less than ...
(sort tvshows-duration-int)
(type (sort tvshows-duration-int))

(defn search-by-season-min [season] (filter #(< (:release_year %) season) tvshows))
(search-by-season-min 2018)
;;;;;;;;search by number of seasons for tv shows grater than ...
