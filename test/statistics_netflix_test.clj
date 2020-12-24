(ns statistics-netflix-test
  (:require [midje.sweet :refer [=> facts]]
            [statistics-netflix :refer [most-frequent most-frequent-n convertor
                                        substring-to-integer transfom-string]]))

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
 
 (facts "Converts number in string to integer"
        (convertor "41 ss")
        => 41
        (convertor "41")
        => 41)
 
 (facts "Converts number from string to integer, if not, return 0"
        (substring-to-integer "1 season")
        => 1
        (substring-to-integer "season")
        => 0)
  
 (facts "Transform string to get TV Show, if string has less than 3 characters, returns aaa"
        (transfom-string "hi you")
        => "HI You"
        (transfom-string "hello")
        => "HE Lo"
        (transfom-string "tv show")
        => "TV Show"
        (transfom-string "hi")
        => "aaa")
