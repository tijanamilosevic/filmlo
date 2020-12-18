(ns index
  (:require [template :refer [template-page]]
            [popularity-prediction :refer [top-movies]]))

(defn- top-list-movies
  "Show movies top list."
  [movies]
  [:div
   [:h1 {:style " padding: 10px; text-align: center;"} 
    "FilmLo- movies statistics and predictions, all in one place"]
   [:h2 {:style "border-bottom: 1px solid #f3943b; padding: 10px; text-align: center; font-size: 25px;"} 
    "Movies on Netflix, Hulu, Disney+ and Prime Video platform"]
   [:div {:style "float: right; margin-top: 40px !important;"}
         [:p
          [:span (str (count movies)" top " "movies")]]]
   [:div {:style "margin-top: 20px !important;"}
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

