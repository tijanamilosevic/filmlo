(ns index
  (:require [template :refer [template-page]]
            [popularity-prediction :refer [top-movies]]))

(defn- top-list-movies
  "Show movies top list."
  [movies]
  [:div.body
       [:h1 {:style "border-bottom: 1px solid #f3943b; padding: 10px;"} 
        "FilmLo Web App- movies statistics and predictions, all in one place."]
   [:div {:style "float: right;"}
         [:p
          [:span (str (count movies)" top " "movies")]]]
   [:div
   [:h2 "Top list popular movies"]
   [:div.form
    [:table {:frame "box"}
     [:tr
      [:th "No."]
      [:th {:style "width: 50% !important;"} "Title"]
      [:th "Genre"]
      [:th "Year"]
      [:th "Duration"]
      [:th "Country"]
      [:th "IMDb Rating"]]
     (let [x (atom {})]
       (swap! x assoc :no 0)
       (for [movie movies]
       (identity [:tr
                  [:td [:div (:no (swap! x update :no inc))]]
                  [:td [:div.justy (movie :title)]]
                  [:td [:div.justy (movie :genres)]]
                  [:td [:div (movie :year)]]
                  [:td [:div (movie :runtime)]]
                  [:td [:div.justy (movie :country)]]
                  [:td [:div (movie :imdb)]]])))]]]])

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

