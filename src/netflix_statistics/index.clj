(ns index
  (:require [template :refer [template-page]]
            [popularity-prediction :refer [top-movies]]))

(defn- top-list-movies
  "Show movies top list."
  [movies]
  [:div.body
       [:h1 {:style "border-bottom: 1px solid #f3943b; padding: 10px;"} 
        "FilmLo- movies statistics and predictions, all in one place."]
   [:div {:style "float: right;"}
         [:p
          [:span (str (count movies)" top " "movies")]]]
   [:div
   [:h2 "Top list popular movies"]
   [:div.form
    [:table {:frame "box"}
     [:tr
      [:th "No."]
      [:th {:style "width: 70% !important;"} "Title"]
      [:th "Genre"]
      [:th "Year"]
      [:th "Duration"]
      [:th {:style "width: 30% !important;"}"Country"]
      [:th "IMDb Rating"]
      [:th "Platform"]]
     (let [x (atom {})]
       (swap! x assoc :no 0)
       (for [movie movies]
       (identity [:tr
                  [:td [:div (:no (swap! x update :no inc))]]
                  [:td [:div (movie :title)]]
                  [:td [:div (movie :genres)]]
                  [:td [:div (movie :year)]]
                  [:td [:div (movie :runtime)]]
                  [:td [:div (movie :country)]]
                  [:td [:div (movie :imdb)]]
                  [:td [:div (movie :platform)]]])))]]]])

(defn- movies-layout 
  "Show top list movies."
  [movies-list]
  [:div.body
       [:div
        (top-list-movies movies-list)]])

(defn index-page 
  "Show first (home) page."
  [uri]
  (template-page 
    "Home page"
    uri
   (movies-layout (top-movies))))

