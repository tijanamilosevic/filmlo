(ns popularity-prediction-test
  (:require [midje.sweet :refer [=> facts]]
            [clojure.test :refer :all]
            [popularity-prediction :refer [to-keyword keywordise insert-score to-int zero score-to-category
                                           contains directors-points contains-imdb imdb-score sum popularity]]))


(facts "Modified string to key value( one string => :one-string)"
  (to-keyword "some Text")
     => :some-text
  (to-keyword "Some text TeXT")
     => :some-text-text)

(def movies-fake [{"Name" "Neki 1"
                   "IMDB rating" 9.1
                  }
                  {"Name" "Neki 2"
                   "IMDB rating" 8.5}])

(def movies-expected [{:name "Neki 1"
                       :imdb-rating 9.1
                      }
                      {:name "Neki 2"
                       :imdb-rating 8.5}])

(def movies-not-expected [{:Name "Neki 1"
                          :IMDB-rating 9.1
                          }
                          {:Name "Neki 2"
                          :IMDB-rating 8.5}])

(facts "Applying to-keyword function to vector"
  (keywordise movies-fake)
     => movies-expected
   (keywordise nil)
     => ()
   (keywordise [])
     => ())


(def movie-fake {:name "Neki 1"
                  :imdb-rating 9.1})

(def movie-expected {:name "Neki 1"
                      :imdb-rating 9.1
                      :score 1})

(facts "Inserting :score in movie"
  (insert-score movie-fake 1)
     => movie-expected
   (insert-score nil 1)
     => {:score 1}
   (insert-score {} 1)
     => {:score 1})

(facts "Returns number from string"
  (to-int "25%")
     => 25
   (to-int "1a158")
     => 1
   (to-int "a555kl123")
     => 555
   (to-int "aaaaa555")
     => 555)

(facts "Returns 0 if string is empty"
  (zero "25%")
     => "25%"
   (zero "")
     => 0)

(facts "Returns category 0, 1, 2"
       (score-to-category 62)
       => 2
       (score-to-category 100)
       => 2
       (score-to-category 32)
       => 1
       (score-to-category 60)
       => 2
       (score-to-category 29)
       => 0)

(facts "Checking if directors json contains directors name"
       (contains "Spike Jonze")
       => true
       (contains "Some director")
       => false)

(facts "If directors name exists -> 1, otherwise 0 "
       (directors-points "Spike Jonze")
       => 1
       (directors-points "Some director")
       => 0)

(facts "Checking if iMDb sequence contains any iMDb"
       (contains-imdb '(1 2 34 5) 5)
       => true
       (contains-imdb '(1 2 34 5) 11)
       => false)

(facts "Returns iMDb score 0 or 1"
       (imdb-score 5.6)
       => 1
       (imdb-score 0)
       => 0)

(facts "Returns sum four numbers"
       (sum 1 2 3 4)
       => 10
       (sum 0 0 0 0)
       => 0)

(facts "Returns popular/ not popular"
       (popularity 5)
       => "popular"
       (popularity 8)
       => "popular"
       (popularity 3)
       => "not popular")
