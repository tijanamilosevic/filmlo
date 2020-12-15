(ns index
  (:require [template :refer [template-page]]
            [movies-statistics :refer [num-movies]]))

(defn index-page [uri]
  (template-page 
    "Home page"
    uri
    [:div.body
       [:h1 {:style "border-bottom: 1px solid #f3943b; padding: 10px;"} 
        "FilmLo Web App- movies statistics and predictions, all in one place"]
       [:p "Number of movies: "
        [:b num-movies]]]))

