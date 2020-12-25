(ns popularity-prediction
  (:require [clojure.data.json :as json]
            [ultra-csv.core :as csv]
            [clojure.set :as set]
            [clojure.string :refer [lower-case blank?]]
            [kmeans-movies  :refer [bad-ranking-group average-ranking-group top-ranking-group]]))

;; the popularity of movies depends on several factors
;; 1) streaming platform popularity
;; 2) Rotten Tomatoes rating
;; 3) director popularity
;; 4) IMDb rating

;; movie popularity score is 8 points:
;; 1) streaming platform popularity-  0-not popular, 1-mid-popular, 2-popular, 3-very popular
;; 2) Rotten Tomatoes rating-  0-not popular, 1-mid-popular, 2-popular
;; 3) director popularity-  0-not popular, 1-popular
;; 4) IMDb rating-  0-not popular, 1-mid-popular, 2-popular

;; RANKING LIST:
;; 0,1,2,3,4 points - not popular movies
;; 5,6,7,8 points- popular movies

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; IMPLEMENTATION ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; load movies csv:
;(def movies-csv (csv/read-csv "resources/movies.csv"))
;movies-csv

;; representation is like [7748 7749 "March or Die" 1977 "7+" 6.3 nil 0 0 1 0 0 "Dick Richards" "Adventure,Drama,Romance,War" "United Kingdom" "English" 107]
;; so, its not so useful
;; better option is reading from json file:

;; load movies json
(def json-movies (slurp "resources/movies.json"))
(def movies-str (json/read-str json-movies))

;; movies atributes are in string form: "Name", "Actors", "Rotten Tomatoes" etc...
;; but, its easier to work with keys: :name, :actors, :rotten-tomatoes
;; so we need to transform movies-str persistent vector

;; "Rotten Tomatoes" to :rotten-tomatoes
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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 1. streaming platform popularity:
;; acording to statista.com most popular streaming platform are:
;; Netflix(3 points), 
;; Hulu(2 points), 
;; Prime video (1 point)
;; and Disney (0 points).


(defn insert-score-platform
  "Insert platform score keyvalue to one movie."
  [movie points]
  (assoc movie :score-platform points))


(def netflix-movies
  (map (fn [e] (insert-score-platform e 3))
       (filter #(= (:netflix %) 1) movies)))
netflix-movies

(count netflix-movies)

(def hulu-movies
  (map (fn [e] (insert-score-platform e 2))
       (filter #(= (:hulu %) 1) movies)))
hulu-movies
(count hulu-movies)

(def prime-movies
  (map (fn [e] (insert-score-platform e 1))
       (filter #(= (:prime-video %) 1) movies)))
prime-movies
(count prime-movies)

(def disney-movies
  (map (fn [e] (insert-score-platform e 0))
       (filter #(= (:disney+ %) 1) movies)))

disney-movies
(count disney-movies)

;; merging movies with platforme scores:
(def netflix-hulu (into netflix-movies hulu-movies))
(def disney-prime (into disney-movies prime-movies))
(def movies-with-platform-score (into netflix-hulu disney-prime))

movies-with-platform-score ;; from now, we use this movie data, and we will add other scores :)
(count movies-with-platform-score)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 2. Rotten Tomatoes rating:
;; according to Rotten Tomatoes web site:
;; >60% - 2 points
;; 30% - 59% - 1 point
;; 0% - 29% - 0 points


(defn to-int [value]
  (Integer. (re-find  #"\d+" value)))


(defn insert-score-rotten-tomatoes [movie points]
  (assoc movie :score-rotten-tomatoes points))

;; be careful, Rotten tomatoes has empty values!

(def has-value (filter #(not= (:rotten-tomatoes %) "") movies-with-platform-score))
(def no-vaule (filter #(= (:rotten-tomatoes %) "") movies-with-platform-score))

;; no-value r.t will get 0% (I suposse its not popular, or data is missing...)

(defn zero [v]
  (if (empty? v) 0 v))

(def zero-no-value (map (fn [x]
                          (update x :rotten-tomatoes zero))
                        no-vaule))
zero-no-value



;; r.t with value into integer ("85% -> 85"):
(def int-has-value (map (fn [x]
                          (update x :rotten-tomatoes to-int))
                        has-value))
int-has-value

;; merge:
(def int-rt-movies-with-platform-score (into zero-no-value int-has-value))

(defn score-to-category [score]
  (cond
    (< score 30) 0
    (< score 60) 1
    true 2))

;; apply to all movies:

(def movies-with-platform-rotten-tomato-scores
  (map
   #(insert-score-rotten-tomatoes %
                                  (score-to-category
                                   (:rotten-tomatoes %)))
   int-rt-movies-with-platform-score))

movies-with-platform-rotten-tomato-scores ;; this movies data we use in next steps :)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 3. director popularity:
;; here is list 100 most popular directors in the world, so is movies director is on this list its 1 point, if not 0 poins

; load json file
(def json-directors (slurp "resources/directors.json"))
(def directors (json/read-str json-directors :key-fn keyword))
directors

; insert score function:
(defn insert-score-director [director points]
  (assoc director :score-director points))

; director names sequence:
(def names (map :Name directors))

; check null values in movies data:
(def nil-director-movies (filter #(= (:directors %) "") movies-with-platform-rotten-tomato-scores))

; that movies will get 0 points:
(def zero-score (into []
                      (map (fn [e] (insert-score-director e 0)) nil-director-movies)))
zero-score

;; no nil movies diretor:
(def no-nil-director-movies (filter #(not= (:directors %) "") movies-with-platform-rotten-tomato-scores))
no-nil-director-movies

;; if movies director name is in names sequence, that move gets 1 point :)

(defn contains [director]
  (.contains names director))

(defn directors-points [director]
  (cond
    (= (contains director) true) 1
    true 0))

(def has-value-with-score (map #(insert-score-director % (directors-points (:directors %)))
                               no-nil-director-movies))

;; merge zero-score and 
(def movies-with-platform-rotten-tomato-scores-director (into zero-score has-value-with-score))


movies-with-platform-rotten-tomato-scores-director ;; we use this movies for next steps :)

(count (filter #(= (:score-director %) 1) movies-with-platform-rotten-tomato-scores-director)) ;313 movies with popular director
(count (filter #(= (:score-director %) 0) movies-with-platform-rotten-tomato-scores-director))

;;;;;;;;;;;;;;;;;;;;;;;; 4. IMDb rating:
;; I already defined groups in kmeans-movies namespace, so:
;;  first-group-rating will get 0 points, 
;;  second-group-rating 1 point, 
;;  and third-group-rating 2 points.

;; check if sequence group contains imdb rating:
(defn contains-imdb [s imdb]
  (.contains s imdb))

;; checking nil values:
(count (filter #(= (:imdb %) "") movies-with-platform-rotten-tomato-scores-director))
;; we have 576 movies with nil imdb, these movies will get score-imdb 0

;; nil-value imdb movies:
(def nil-val-imdb-movies (filter #(= (:imdb %) "") movies-with-platform-rotten-tomato-scores-director))
;; no nill vaule imdb movies:
(def no-nil-imdb-movies (filter #(not= (:imdb %) "") movies-with-platform-rotten-tomato-scores-director))


(defn insert-score-imdb [imdb points]
  (assoc imdb :score-imdb points))

(def zero-score-imdb-movies (into []
                                  (map (fn [e] (insert-score-imdb e 0)) nil-val-imdb-movies)))

(defn imdb-score [imdb]
  (cond
    (= (contains-imdb bad-ranking-group imdb) true) 0
    (= (contains-imdb average-ranking-group imdb) true) 1
    true 2))


(def score-imdb-movies (map #(insert-score-imdb % (imdb-score (:imdb %)))
                            no-nil-imdb-movies))

;; merge nil and no nil imdb movies:
(def all-scores (into zero-score-imdb-movies score-imdb-movies))

;; movies in these three groups (ranking by imdb)
(defn bad-ranking
  "Returns movies in bad ranking group by IMDb (from list with IMDB value)."
  []
  (filter #(= (:score-imdb %) 0)
          score-imdb-movies))

(defn average-ranking
  "Returns movies in average ranking group by IMDb."
  []
  (filter #(= (:score-imdb %) 1)
          all-scores))

(defn top-ranking
  "Returns movies in top ranking group by IMDb."
  []
  (filter #(= (:score-imdb %) 2)
          all-scores))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; final score calculation
;;    final-score = score-platform + score-rotten-tomatoes + score-director + score-imdb

(defn insert-final-score [movie sum-points]
  (assoc movie :score-final sum-points))

(defn sum [s1 s2 s3 s4]
  (reduce + [s1 s2 s3 s4]))

(def movies-final-score (map #(insert-final-score % (sum (:score-imdb %) (:score-director %) (:score-rotten-tomatoes %) (:score-platform %)))
                             all-scores))


;; popular/not popular movies 

(defn popularity [final-score]
  (cond
    (< final-score 5) "not popular"
    true "popular"))

(defn insert-popularity [movie popularity]
  (assoc movie :popularity popularity))

(def movies-popularity (map #(insert-popularity % (popularity (:score-final %)))
                            movies-final-score))

(count (filter #(= (:popularity %) "popular") movies-popularity)) ; 2092 popular movies!

(defn popular-movies
  "Returns popular movies."
  []
  (filter #(= (:popularity %) "popular")
          movies-popularity))


(defn not-popular-movies
  "Returns not popular movies."
  []
  (filter #(= (:popularity %) "not popular")
          movies-popularity))

;; adding :platform key to popular movies

(defn insert-platform [movie name]
  (assoc movie :platform name))

(defn platform-name [netflix hulu disney]
  (cond
    (= netflix 1) "Netflix"
    (= hulu 1) "Hulu"
    (= disney 1) "Disney+"
    true "Prime Video"))


(defn popular-platform-movies
  "Returns popular movies with new key :platform and its value."
  []
  (map #(insert-platform % (platform-name
                            (:netflix %)
                            (:hulu %)
                            (:disney+ %)))
       (popular-movies)))

;;;;;;; working with popular movies

(defn num-popular-movies
  "Counts popular movies. "
  []
  (count (popular-platform-movies)))

(defn top-movies
  "Returns movies with highest score (8)"
  []
  (filter #(= (:score-final %) 8)
          (popular-platform-movies)))

(defn num-top-movies
  "Counts top movies. "
  []
  (count (popular-platform-movies)))

(defn capitalize-words
  "Capitalize every word in a string."
  [s]
  (->> (clojure.string/split (str s) #"\b")
       (map clojure.string/capitalize)
       clojure.string/join))

(defn search-by-platform
  "Search popular movies by platform."
  [platform movies-passed]
  (filter #(.contains (:platform %) (capitalize-words platform))
          movies-passed))
