(ns popularity-prediction
  (:require [clojure.data.json :as json]))

;; the popularity of movies depends on several factors
;; 1) actors popularity
;; 2) streaming platform popularity
;; 3) director popularity
;; 4) IMDb rating
;; 5) Rotten Tomatoes rating

;; movie popularity score is 10 points:
;; 1) actors popularity - 0-not popular, 1-mid-popular, 2-popular
;; 2) streaming platform popularity-  0-not popular, 1-mid-popular, 2-popular
;; 3) director popularity-  0-not popular, 1-mid-popular, 2-popular
;; 4) IMDb rating-  0-not popular, 1-mid-popular, 2-popular
;; 5) Rotten Tomatoes rating-  0-not popular, 1-mid-popular, 2-popular

;; RANKING LIST:
;; 0,1,2,3,4,5 - not popular movies
;; 6,7,8,9,10 - popular movies

;; actors points:


;; streaming platform popularity:
;; acording to statista.com most popular streaming platform are Netflix and Hulu(2 points), Prime video (1 point) and Disney (0 points)


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


