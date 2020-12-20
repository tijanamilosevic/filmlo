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
(defn no-nil
  "Returns movies with no nil values from movies for specific column."
  [movies column]
  (filter #(not= (column %) "") 
          movies))

(defn get-values
  "Returns values from no nil movies for specific column."
  [no-nil-movies column]
  (map column no-nil-movies))

(defn average
  "Returns average for specific column and movies."
  [no-nil-movies column-values]
 (double 
  (/ 
   (reduce + column-values) 
   (count no-nil-movies))))

(defn average-runtime 
  "Returns average runtime."
  []
  (Math/round 
   (average
    (no-nil movies :runtime)
    (get-values 
     (no-nil movies :runtime) 
     :runtime)))) ; 93.41, rounded: 93

(defn netflix-average-runtime 
  "Returns Netflix average runtime."
  []
  (Math/round 
   (average
    (no-nil movies-netflix :runtime)
    (get-values 
     (no-nil movies-netflix :runtime) 
     :runtime)))) 


(defn hulu-average-runtime 
  "Returns Hulu average runtime."
  []
  (Math/round 
   (average
    (no-nil movies-hulu :runtime)
    (get-values 
     (no-nil movies-hulu :runtime) 
     :runtime)))) 

(defn disney-average-runtime 
  "Returns Disney+ average runtime."
  []
  (Math/round 
   (average
    (no-nil movies-disney :runtime)
    (get-values 
     (no-nil movies-disney :runtime) 
     :runtime))))

(defn prime-average-runtime 
  "Returns Prime Video average runtime."
  []
  (Math/round 
   (average
    (no-nil movies-prime :runtime)
    (get-values 
     (no-nil movies-prime :runtime) 
     :runtime))))


(defn max-value
 "Returns max value in the list." 
  [values]
  (apply max values))

(defn min-value
 "Returns min value in the list." 
  [values]
  (apply min values))

(defn max-duration-movies
 "Returns max duration for all movies." 
  []
  (max-value
   (get-values 
    (no-nil movies :runtime) 
    :runtime)))

(defn min-duration-movies
 "Returns min duration for all movies." 
  []
  (min-value
   (get-values 
    (no-nil movies :runtime) 
    :runtime)))

(defn max-duration-netflix
 "Returns max duration for Netflix movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-netflix :runtime) 
    :runtime)))

(defn min-duration-netflix
 "Returns min duration for Netflix movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-netflix :runtime) 
    :runtime)))


(defn max-duration-hulu
 "Returns max duration for Hulu movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-hulu :runtime) 
    :runtime)))

(defn min-duration-hulu
 "Returns min duration for Hulu movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-hulu :runtime) 
    :runtime)))


(defn max-duration-disney
 "Returns max duration for Disney movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-disney :runtime) 
    :runtime)))

(defn min-duration-disney
 "Returns min duration for Disney movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-disney :runtime) 
    :runtime)))


(defn max-duration-prime
 "Returns max duration for Prime Video movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-prime :runtime) 
    :runtime)))

(defn min-duration-prime
 "Returns min duration for Prime Video movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-prime :runtime) 
    :runtime)))


;; Average, max, min IMDb rate:

(defn try-convert-string 
  "Convert string to integer (if it is possible)."
  [str]
  (try 
  (Integer/valueOf str)
  (catch Exception e (identity 0))))

(defn try-convert-string-double 
  "Convert string to double (if it is possible)."
  [str]
  (try 
  (Double/valueOf str)
  (catch Exception e (identity 0))))

(defn try-convert-string-string 
  "Convert string to integer (if it is possible), if not possible returns string."
  [str]
  (try 
  (Integer/valueOf str)
  (catch Exception e (identity "any text"))))

(defn try-convert-string-double-string
  "Convert string to double (if it is possible),  if not possible returns string."
  [str]
  (try 
  (Double/valueOf str)
  (catch Exception e (identity "any text"))))

(defn one-decimal
  "Round number to one decimal place."
  [value]
  (try-convert-string-double (format "%.1f" value)))

(defn average-imdb
  "Returns average imdb."
  []
  (one-decimal
   (average
   (no-nil movies :imdb)
   (get-values 
    (no-nil movies :imdb) 
    :imdb))))

(defn average-imdb-netflix
  "Returns average imdb for Netflix movies."
  []
  (one-decimal
   (average
   (no-nil movies-netflix :imdb)
   (get-values 
    (no-nil movies-netflix :imdb) 
    :imdb))))

(defn average-imdb-hulu
  "Returns average imdb for Hulu movies."
  []
  (one-decimal
   (average
   (no-nil movies-hulu :imdb)
   (get-values 
    (no-nil movies-hulu :imdb) 
    :imdb))))

(defn average-imdb-disney
  "Returns average imdb dor Disney+ movies."
  []
  (one-decimal
   (average
   (no-nil movies-disney :imdb)
   (get-values 
    (no-nil movies-disney :imdb) 
    :imdb))))

(defn average-imdb-prime
  "Returns average imdb for Prime Video movies."
  []
  (one-decimal
   (average
   (no-nil movies-prime :imdb)
   (get-values 
    (no-nil movies-prime :imdb) 
    :imdb))))

(defn max-imdb-movies
 "Returns max IMDb all movies." 
  []
  (max-value
   (get-values 
    (no-nil movies :imdb) 
    :imdb)))

(defn min-imdb-movies
 "Returns min IMDb all movies." 
  []
  (min-value
   (get-values 
    (no-nil movies :imdb) 
    :imdb)))

(defn max-imdb-netflix
 "Returns max IMDb for Netflix movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-netflix :imdb) 
    :imdb)))

(defn min-imdb-netflix
 "Returns min IMDb for Netflix movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-netflix :imdb) 
    :imdb)))


(defn max-imdb-hulu
 "Returns max IMDb for Hulu movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-hulu :imdb) 
    :imdb)))

(defn min-imdb-hulu
 "Returns min IMDb for Hulu movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-hulu :imdb) 
    :imdb)))


(defn max-imdb-disney
 "Returns max IMDb for Disney movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-disney :imdb) 
    :imdb)))

(defn min-imdb-disney
 "Returns min IMDb for Disney movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-disney :imdb) 
    :imdb)))


(defn max-imdb-prime
 "Returns max IMDb for Prime Video movies." 
  []
  (max-value
   (get-values 
    (no-nil movies-prime :imdb) 
    :imdb)))

(defn min-imdb-prime
 "Returns max IMDb for Prime Video movies." 
  []
  (min-value
   (get-values 
    (no-nil movies-prime :imdb) 
    :imdb)))


;; I want to check movie deviation from mean, so we need variance and standard deviation

;; 1) Variance
;; variance is the expectation of the squared deviation of a random variable from its mean
;; v= ( (x1-mean)^2+(x2-mean)^2+ ... + (xn-mean)^2 ) / n

(defn variance 
  "A measure of how far a set of numbers is spread out."
  [data average-data]
    (def sqr (fn [x] (* x x)))
    (let [mv (average-data)]
      (/
        (reduce +
          (map
            #(sqr (- % mv)) data))
              (count data))))

(defn movies-variance
 "Returns movies runtime variance." 
  []
  (Math/round 
   (double 
    (variance 
     (get-values 
      (no-nil movies :runtime) 
      :runtime)
     average-runtime))))
(movies-variance); variance is 796

;; 2) Standard deviation

(defn standard-deviation
  "In statistics and probability theory, standard deviation (represented by the symbol sigma, σ)
   shows how much variation or dispersion exists from the average (mean, or expected value).
   A low standard deviation indicates that the data points tend to be very close to the mean,
   whereas high standard deviation indicates that the data points are spread out over a large
   range of values."
  [data average-data]
  (Math/sqrt (variance data average-data)))

(defn movies-standard-deviation
 "Returns movies duration standard deviation." 
  []
  (Math/round 
   (double 
    (standard-deviation 
     (get-values 
      (no-nil movies :runtime) 
      :runtime)
     average-runtime))))

(movies-standard-deviation) ; the average deviation of the duration of the movies from the average value is 28

(defn imdb-standard-deviation
 "Returns movies IMDb standard deviation." 
  []
  (one-decimal
   (double 
    (standard-deviation 
     (get-values 
      (no-nil movies :imdb) 
      :imdb)
     average-imdb))))

;;;;;;;;;;;; Mode ;;;;;;;;;;;;;;;

;; - which duration is most recurring, can be more than one value!

(defn mode 
  "Returns most frequent value in data."
  [data]
  (let [frequency-distribution 
        (frequencies data)
        sorted 
        (sort-by
         (comp - second) 
          frequency-distribution)
        mxfreq 
        (second 
         (first sorted))]
    (map 
     first 
     (take-while 
      (fn [[val freq]]
        (= mxfreq freq)) 
      sorted))))

(defn calculate-mode 
  "Calculates mode for specific platform and column."
  [movies-list column]
  (mode 
   (get-values 
      (no-nil movies-list column) 
      column)))

(defn mode-duration
  "Returns mode duration."
 []
  (calculate-mode movies :runtime)); its 90


;; which language is most recurring:
(defn mode-language 
  "Returns mode language."
  []
   (calculate-mode movies :language)); its English (only english)

;; which genre is most recurring:

(defn mode-genre
  "Returns mode genre."
  []
  (calculate-mode movies :genres)) ; its Drama (only Drama)

;; which country is most recurring:

(defn mode-country 
   "Returns mode country."
  []
  (calculate-mode movies :country)); its United States (only United States)

(defn mode-imdb
   "Returns mode IMDb."
  []
  (calculate-mode movies :imdb))


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


(defn search-by-year
 "Search movies by year." 
  [year movies-passed] 
  (filter #(= (:year %) (try-convert-string year)) 
          movies-passed))
(search-by-year "2020" movies-hulu)

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

(defn check-contains
  "Check if column contains value if value is not integer."
  [column value]
  (if (= (integer? (try-convert-string-string value)) true)
    (.contains column "any text")
    (.contains column value)))

(defn search-by-genre
 "Search movies by genre." 
  [genre movies-passed] 
  (filter #(.contains (:genres %) (capitalize-words genre)) 
          movies-passed))

(defn search-by-imdb
 "Search movies by IMDb rating." 
  [imdb movies-passed] 
  (filter #(= (:imdb %) (if (< (count imdb)  3) 
                          (try-convert-string-string imdb) 
                          (try-convert-string-double-string imdb))) 
          movies-passed))

(defn search-by-language
 "Search movies by language." 
  [language movies-passed] 
  (filter #(check-contains (:language %) (capitalize-words language)) 
          movies-passed))

