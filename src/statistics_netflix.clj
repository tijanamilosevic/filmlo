(ns statistics-netflix
  (:require [clojure.data.json :as json]
            [movies-statistics :refer [no-nil get-values min-value max-value]]))

; load data from json file

(def json-file (slurp "resources/netflix_titles.json"))

(def netflix-portfolio (json/read-str json-file :key-fn keyword))

;;;;;;;;;Movies
(def movies (filter #(= (:type %) "Movie") netflix-portfolio))
;;;;;;;;;Tv-shows
(def tvshows (filter #(= (:type %) "TV Show") netflix-portfolio))

;;;;;;;;;count all, moves&tv-shows
(defn sum-all
  "Returns Netflix portfolio size."
  []
  (count netflix-portfolio))

;;;;;;;;;count movies
(defn sum-movies
  "Sumarize movies in Netflix portfolio."
  []
  (count
   (filter #(= (:type %) "Movie")
           netflix-portfolio)))

;;;;;;;;;;count Tv-shows
(defn sum-tvshows
  "Sumarize TV Shows in Netflix portfolio."
  []
  (count
   (filter #(= (:type %) "TV Show")
           netflix-portfolio)))

;;;;;;;;;;min duration movie
(def movies-duration (map :duration movies))

(defn movies-duration-int
  "Returns movies durations as Integer."
  []
  (map
   (fn [e]
     (Integer.
      (re-find  #"\d+" e)))
   movies-duration))

(def min-duration-movie (apply min (movies-duration-int)))
min-duration-movie ;3

; we need duration like "3 min":
(defn get-min-duration-movie
  "Returns movie(s) with min duration."
  []
  (filter #(= (:duration %) (str min-duration-movie " min"))
          netflix-portfolio))

(get-min-duration-movie) ; 1 movie ("Silent")

;;;;;;;;;;;max duration movie
(def max-duration-movie (apply max (movies-duration-int)))
max-duration-movie ;312

(defn get-max-duration-movie
  "Returns movie(s) with max duration."
  []
  (filter #(= (:duration %) (str max-duration-movie " min"))
          netflix-portfolio))

(get-max-duration-movie) ; 1 movie ("Black Mirror: Bandersnatch")

;;;;;;;;;;average duration for movies (min)

;all movies duration, like "90 min" etc...
;(def movies-duration (map :duration movies))
(type movies-duration) ;it is lazy sequence

; duration is like "90 min", but we want 90 (number):
(defn convertor
  "Converts string to integer."
  [s]
  (Integer.
   (re-find  #"\d+" s)))

;average function:
(defn average-movies
  "Calculates average movies duration."
  [s]
  (double
   (/ (reduce + s)
      (sum-movies))))

(defn movies-duration-average
  "Returns average movies duration."
  []
  (Math/round
   (average-movies (movies-duration-int))))

(movies-duration-average) ; average is 99 min

;;;;;;;;;;min duration tv shows
(def tvshows-duration (map :duration tvshows))

(defn tvshows-duration-int
  "Returns TV Shows durations(seasons) as Integer."
  []
  (map
   (fn [e]
     (Integer. (re-find  #"\d+" e)))
   tvshows-duration))

(def min-duration-tvshow (apply min (tvshows-duration-int)))
min-duration-tvshow ;1 Season

; we need duration like "1 Season":
(defn get-min-duration-tvshows
  "Returns Tv show(s) with min duration (season number)."
  []
  (filter #(= (:duration %)
              (str min-duration-tvshow " Season"))
          netflix-portfolio))

(get-min-duration-tvshows)

;;;;;;;;;;;max duration tv shows
(def max-duration-tvshow (apply max (tvshows-duration-int)))
max-duration-tvshow;15 seasons

(defn get-max-duration-tvshows
  "Returns Tv show(s) with max duration (season number)."
  []
  (filter #(= (:duration %)
              (str max-duration-tvshow " Seasons"))
          netflix-portfolio))

(get-max-duration-tvshows)

;;;;;;;;;;;;;;;;;average duration for tv-shows (seasons)

;average function:
(defn average-tvshows
  "Calculates average Tv Shows duration."
  [s]
  (Math/round
   (double
    (/ (reduce + s)
       (sum-tvshows)))))

(defn tvshows-duration-average
  "Returns average Tv Shows duration."
  []
  (average-tvshows (tvshows-duration-int)))

(tvshows-duration-average) ; average is 2 seasons

;;;;;;;;;; the newest/oldest movie(s)

(defn min-year-movies
  "Returns min year for movies."
  []
  (min-value
   (get-values
    (no-nil movies :release_year)
    :release_year)))

(defn the-oldest-movies
  "Returns the oldest movies."
  []
  (filter #(= (:release_year %) (min-year-movies))
          movies))

(defn max-year-movies
  "Returns max year for movies."
  []
  (max-value
   (get-values
    (no-nil movies :release_year)
    :release_year)))

;;;;;;;;;; the newest/oldest tv-show(s)

(defn min-year-tvshow
  "Returns min year for tvshows."
  []
  (min-value
   (get-values
    (no-nil tvshows :release_year)
    :release_year)))

(defn the-oldest-tvshows
  "Returns the oldest tvshows."
  []
  (filter #(= (:release_year %) (min-year-tvshow))
          tvshows))

(defn max-year-tvshows
  "Returns max year for tvshows."
  []
  (max-value
   (get-values
    (no-nil tvshows :release_year)
    :release_year)))
;;;;;;;;;;;;;;;;;;top country/countries

(def countries (map :country netflix-portfolio))

(defn countries-with-frequencies
  "Returns countries with frequencies."
  []
  (sort-by val
           (frequencies countries)))

(defn most-frequent-n
  "Returns n most frequent values."
  [n items]
  (->> items
       frequencies
       (sort-by val)
       reverse
       (take n)
       (map first)))

(defn most-frequent
  "Returns most frequent value."
  [items]
  (->> items
       frequencies
       (sort-by val)
       reverse
       (take 1)
       (map first)))

(defn top-country
  "Returns most frequent country."
  []
  (most-frequent countries))

(top-country) ;united states

(defn top-5-countries
  "Returns 5 top frequent countries."
  []
  (most-frequent-n 5 countries))

(defn top-3-countries
  "Returns 5 top frequent countries."
  []
  (most-frequent-n 3 countries))

(top-5-countries)
(top-3-countries)

;;;;;;;;;;;;;;;;;;;;;;;;;;top genre(listed_in)
(def genres (map :listed_in netflix-portfolio))

(defn genres-with-frequencies
  "Returns genres with frequencies."
  []
  (sort-by val
           (frequencies genres)))

(defn top-genre
  "Returns most frequent genre"
  []
  (most-frequent genres))

(top-genre) ; Documentaries

(defn top-5-genres
  "Returns 5 most frequent genres."
  []
  (most-frequent-n 5 genres))

(defn top-3-genres
  "Returns 3 most frequent genres."
  []
  (most-frequent-n 3 genres))

(top-5-genres)
(top-3-genres)

;;;;;;;;;;;;;;;;;;;;;;;;;;top rating
(def ratings (map :rating netflix-portfolio))

(defn ratings-with-frequencies
  "Returns ratings with frequencies."
  []
  (sort-by val
           (frequencies ratings)))

(defn top-rating
  "Returns most frequent rating."
  []
  (most-frequent ratings))

(top-rating) ; TV-MA

(defn top-5-rating
  "Returns 5 most frequent ratings."
  []
  (most-frequent-n 5 ratings))

(defn top-3-rating
  "Returns 3 most frequent ratings."
  []
  (most-frequent-n 3 ratings))

(top-3-rating)
(top-5-rating)

;;;;;;;;;;;;;;;;;;;;;;;;;;top release year

(def years (map :release_year netflix-portfolio))

(defn years-with-frequencies
  "Returns years with frequencies."
  []
  (sort-by val
           (frequencies years)))

(defn top-release-year
  "Returns most frequent year."
  []
  (most-frequent years))

(top-release-year) ; 2018

(defn top-5-release-years
  "Returns 5 most frequent years."
  []
  (most-frequent-n 5 years))

(defn top-3-release-years
  "Returns 3 most frequent ratings."
  []
  (most-frequent-n 3 years))

(top-5-release-years)
(top-3-release-years)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;search:

;;;;;;;;by release year
(defn search-by-year
  "Search Netflix portfolio by year."
  [year]
  (filter #(= (:release_year %) year)
          netflix-portfolio))


;;;;;;;; less than year
(defn search-by-year-min
  "Search Netflix portfolio by year less than the year forwarded."
  [year]
  (filter #(< (:release_year %) year)
          netflix-portfolio))

;;;;;;;; greater than year
(defn search-by-year-max
  "Search Netflix portfolio by year greater than the year forwarded."
  [year]
  (filter #(> (:release_year %) year) netflix-portfolio))


;;;;;;;;by name

(defn capitalize-words
  "Capitalize every word in a string"
  [s]
  (->> (clojure.string/split (str s) #"\b")
       (map clojure.string/capitalize)
       clojure.string/join))

(defn search-by-name
  "Search Netflix portfolio by name(title)."
  [name]
  (filter #(= (:title %) (capitalize-words name))
          netflix-portfolio))

;;;;;;;;by type
(defn search-by-type-movie
  "Prepared to search Netflix portfolio by type Movie."
  [type]
  (filter #(= (:type %) (clojure.string/capitalize type))
          netflix-portfolio))

(defn transfom-string
  "Returns specific string ( like TV Show), because type is TV Show, not tv show etc..."
  [str-type]
  (try
    (str
     (clojure.string/upper-case (subs str-type 0 2))
     " "
     (clojure.string/capitalize (subs str-type 3)))
    (catch Exception e (identity "aaa"))))

(defn search-by-type-tv-show
  "Prepared to search Netflix portfolio by type TV Show."
  [type]
  (filter #(= (:type %) (transfom-string type))
          netflix-portfolio))

;;;;;;;;by country
(defn search-by-country
  "Search Netflix portfolio by country."
  [country]
  (filter #(.contains (:country %) (capitalize-words country))
          netflix-portfolio))

;;;;;;;;search by number of seasons for tv shows
(defn search-by-season
  "Search TV Shows by season number."
  [season]
  (filter #(= (:duration %) (if (= season 1)
                              (str season " Season")
                              (str season " Seasons")))
          tvshows))
(search-by-season 15)
;;;;;;;;search by number of seasons for tv shows less than ...

(defn substring-to-integer
  "Converts numbers from string to integer."
  [str]
  (try
    (Integer.
     (re-find  #"\d+" str))
    (catch Exception e (identity 0))))

(defn search-by-season-min
  "Search TV Shows by season number less than number forwarded."
  [season]
  (filter #(< (substring-to-integer (:duration %))
              season)
          tvshows))

;;;;;;;;search by number of seasons for tv shows greater than ...

(defn search-by-season-max
  "Search TV Shows by season number greater than number forwarded."
  [season]
  (filter #(> (substring-to-integer (:duration %))
              season)
          tvshows))

;;;;;;;;search by duration for movies

(defn search-by-duration
  "Search Movies by duration."
  [duration]
  (filter #(= (:duration %) (str duration " min"))
          movies))

;;;;;;;;search movies by duration less than ...

(defn search-by-duartion-min
  "Search Movies by duration less than number forwarded."
  [duration]
  (filter #(< (substring-to-integer (:duration %))
              duration)
          movies))

;;;;;;;;search movies by duration greater than ...

(defn search-by-duration-max
  "Search Movies by duration greater than number forwarded."
  [duration]
  (filter #(> (substring-to-integer (:duration %))
              duration)
          movies))