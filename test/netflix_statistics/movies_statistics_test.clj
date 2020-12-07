(ns movies-statistics-test
  (:require [midje.sweet :refer [=> facts]]
            [clojure.test :refer :all]
            [movies-statistics :refer [mode]]))

(facts "Returns mode"
       (mode '(1 1 1 5 4 7))
       => '(1)
       (mode '(1 1 2 2 6))
       => '(1 2))
