(ns movies-statistics-test
  (:require [midje.sweet :refer [=> facts]]
            [clojure.test :refer :all]
            [movies-statistics :refer [mode no-nil get-values average
                                       max-value min-value one-decimal
                                       try-convert-string-double-string
                                       try-convert-string-string
                                       try-convert-string-double
                                       try-convert-string calculate-mode
                                       capitalize-words check-contains
                                       search-by-language search-by-imdb
                                       search-by-genre search-by-country
                                       search-by-runtime search-by-year
                                       search-by-title]]))

(facts "Returns mode"
       (mode '(1 1 1 5 4 7))
       => '(1)
       (mode '(1 1 2 2 6))
       => '(1 2))

(def fake-movies [{:name "Movie 1"
                   :duration ""
                  }
                  {:name "Movie 2"
                   :duration ""
                   }
                  {:name "Movie 3"
                   :duration 120
                   }])

(def fake-movies-no-nil  [{:name "Movie 1"
                           :duration 155
                          }
                          {:name "Movie 2"
                           :duration 120}])

(facts "Returns movies with no nil duration value"
       (no-nil fake-movies :duration)
       => [{:name "Movie 3"
            :duration 120}]
       (no-nil () :column)
       => ()
       (no-nil
        [{:name "Movie 1"
          :duration ""}]
        :duration)
       => ())

(facts "Returns values for specific column."
       (get-values
        fake-movies-no-nil
        :duration)
       => '(155 120))

(facts "Calculates average value in list for specific column"
       (average 
        fake-movies-no-nil
        '(155 120))
       => 137.5)

(facts "Returns max value in the list."
       (max-value '(100 1 11 2 111))
       => 111
       (max-value '(0 1 0 0 0))
       => 1
       (max-value '(100))
       => 100)

(facts "Returns min value in the list."
       (min-value '(100 1 11 2 111))
       => 1
       (min-value '(0 1 0 0 0))
       => 0
       (min-value '(100))
       => 100)

(facts "Rounded decimal number with one decimal place."
       (one-decimal 1.22)
       => 1.2
       (one-decimal 2.588881)
       => 2.6
       (one-decimal 5.5)
       => 5.5)

(facts "Try convert string to dobule, if not, returns string"
       (try-convert-string-double-string "2.56")
       => 2.56
       (try-convert-string-double-string "2")
       => 2.0
       (try-convert-string-double-string "25.748")
       => 25.748
       (try-convert-string-double-string "text")
       => "any text"
       (try-convert-string-double-string "")
       => "any text")


(facts "Try convert string to integer, if not, returns string"
       (try-convert-string-string "2")
       => 2
       (try-convert-string-string "2.45")
       => "any text"
       (try-convert-string-string "25.748")
       => "any text"
       (try-convert-string-string "text")
       => "any text"
       (try-convert-string-string "")
       => "any text")


(facts "Try convert string to doule, if not, returns 0"
       (try-convert-string-double "2")
       => 2.0
       (try-convert-string-double "2.45")
       => 2.45
       (try-convert-string-double "25.748")
       => 25.748
       (try-convert-string-double "text")
       => 0
       (try-convert-string-double "")
       => 0)

(facts "Try convert string to integer, if not, returns 0"
       (try-convert-string "2")
       => 2
       (try-convert-string "2.45")
       => 0
       (try-convert-string "25.748")
       => 0
       (try-convert-string "text")
       => 0
       (try-convert-string "")
       => 0)

(def fake-movies-mode [{:name "Movie 1"
                       :duration 120
                       }
                      {:name "Movie 2"
                       :duration 99
                       }
                      {:name "Movie 3"
                       :duration 120
                       }])

(facts "Returns mode in vector for specific column."
       (calculate-mode fake-movies-mode :duration)
       => '(120)
       (calculate-mode () :duration)
       => ())

(facts "Capitalize every word in string"
       (capitalize-words "hello clojure user!")
       => "Hello Clojure User!"
       (capitalize-words "hello!")
       => "Hello!"
       (capitalize-words "")
       => "")

(facts "Check if column/string contains string (if string is a number string is any text).
        Its specific contains function, because in :language exists values withs number,
        like: English, Italian, Hindi (4235), and I don't want to return them. "
       (check-contains "English, Italian, Hindi (4235)" "5")
       => false
       (check-contains "English, Italian, Hindi (4235)" "Hindi")
       => true )

(def fake-movies-search [{:title "Movie 1"
                          :runtime 120
                          :language "English, French"
                          :genres "Comedy, Action"
                          :imdb 5.9
                          :year 2019
                          :country "United States, France"
                         }
                         {:title "Movie 2"
                          :runtime 90
                          :language "Serbian, Croatian"
                          :genres "Drama, Romance"
                          :imdb 6.7
                          :year 2008
                          :country "Serbia"
                         }
                         {:title "Movie 3"
                          :runtime 111
                          :language "Italian, Spanish, English, French"
                          :genres "Documentary"
                          :imdb 9
                          :year 2020
                          :country "United States"
                         }])

(facts "Search movies by title"
       (search-by-title "Movie 1" fake-movies-search)
       => [{:title "Movie 1"
            :runtime 120
            :language "English, French"
            :genres "Comedy, Action"
            :imdb 5.9
            :year 2019
            :country "United States, France"}]
       (search-by-title "movie 1" fake-movies-search)
       => [{:title "Movie 1"
            :runtime 120
            :language "English, French"
            :genres "Comedy, Action"
            :imdb 5.9
            :year 2019
            :country "United States, France"}]
       (search-by-title "Movie" fake-movies-search)
       => ())

(facts "Search movies by year"
       (search-by-year "2020" fake-movies-search)
       => [{:title "Movie 3"
            :runtime 111
            :language "Italian, Spanish, English, French"
            :genres "Documentary"
            :imdb 9
            :year 2020
            :country "United States"}]
       (search-by-year 2020 fake-movies-search)
       => [{:title "Movie 3"
            :runtime 111
            :language "Italian, Spanish, English, French"
            :genres "Documentary"
            :imdb 9
            :year 2020
            :country "United States"}]
       (search-by-year 2011 fake-movies-search)
       => ()
       (search-by-year "text" fake-movies-search)
       => ())

(facts "Search movies by language"
       (search-by-language "serbian" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-language "Serbian" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-language "english" fake-movies-search)
       => [{:title "Movie 1"
            :runtime 120
            :language "English, French"
            :genres "Comedy, Action"
            :imdb 5.9
            :year 2019
            :country "United States, France"}
           {:title "Movie 3"
            :runtime 111
            :language "Italian, Spanish, English, French"
            :genres "Documentary"
            :imdb 9
            :year 2020
            :country "United States"}])

(facts "Search movies by IMDb."
       (search-by-imdb "6.7" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-imdb "9" fake-movies-search)
       => [{:title "Movie 3"
            :runtime 111
            :language "Italian, Spanish, English, French"
            :genres "Documentary"
            :imdb 9
            :year 2020
            :country "United States"}]
       (search-by-imdb "8" fake-movies-search)
       => ()
       (search-by-imdb "text" fake-movies-search)
       => ())

(facts "Search movies by genre"
       (search-by-genre "drama" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-genre "Drama" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-genre "action" fake-movies-search)
       => [{:title "Movie 1"
            :runtime 120
            :language "English, French"
            :genres "Comedy, Action"
            :imdb 5.9
            :year 2019
            :country "United States, France"}]
       (search-by-genre "text" fake-movies-search)
       => ())

(facts "Search movies by country"
       (search-by-country "Serbia" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-country "serbia" fake-movies-search)
       => [{:title "Movie 2"
            :runtime 90
            :language "Serbian, Croatian"
            :genres "Drama, Romance"
            :imdb 6.7
            :year 2008
            :country "Serbia"}]
       (search-by-country "france" fake-movies-search)
       => [{:title "Movie 1"
            :runtime 120
            :language "English, French"
            :genres "Comedy, Action"
            :imdb 5.9
            :year 2019
            :country "United States, France"}]
       (search-by-country "text" fake-movies-search)
       => ())

(facts "Search movies by runtime"
       (search-by-runtime "111" fake-movies-search)
       => [{:title "Movie 3"
            :runtime 111
            :language "Italian, Spanish, English, French"
            :genres "Documentary"
            :imdb 9
            :year 2020
            :country "United States"}]
       (search-by-runtime 111 fake-movies-search)
       => [{:title "Movie 3"
            :runtime 111
            :language "Italian, Spanish, English, French"
            :genres "Documentary"
            :imdb 9
            :year 2020
            :country "United States"}]
       (search-by-runtime 150 fake-movies-search)
       => ()
       (search-by-runtime "text" fake-movies-search)
       => ())
