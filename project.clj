(defproject netflix_statistics "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/data.csv "1.0.0"]
                 [ultra-csv "0.2.3"]
                 [compojure "1.6.2"]
                 [ring "1.8.2"]
                 [hiccup "1.0.5"]
                 [lib-noir "0.9.9"]
                 [hickory "0.7.1"]]
  :profiles {:dev {:plugins [[lein-midje "3.2.1"]]
                   :dependencies [[midje "1.9.9"]]}}
  :main netflix-statistics.core)


