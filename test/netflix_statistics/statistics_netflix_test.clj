(ns statistics-netflix-test
  (:require [midje.sweet :refer [=> facts]]
            [clojure.test :refer :all]
            [statistics-netflix :refer [most-frequent most-frequent-n average-movies convertor]]))

 (facts "Returns most frequent value"
        (most-frequent [1 2 3 4 1] )
        => '(1)
        (most-frequent [0 1 0 0 1] )
        => '(0))
 
  (facts "Returns n most frequent values"
        (most-frequent-n 2 [1 2 3 4 1 3])
        => '(3 1)
        (most-frequent-n 3 [0 1 0 0 1 11 11 5])
        => '(0 11 1))
