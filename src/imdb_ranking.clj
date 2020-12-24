(ns imdb-ranking
  (:require [template :refer [template-page]]))

(defn- groups-menu 
  "IMDb groups menu."
  []
  [:div.movie
   [:h2 "Movies from Netflix, Hulu, Disney+ and Prime Video ranking groups by IMDb rating:"]
            [:div {:class "dropdown"}
             [:button {:class "dropbtn"}
              "Choose ranking group"
              [:i {:class "fa fa-caret-down"}]]
             [:div {:class "dropdown-content"}
              [:a {:href "/imdb-ranking-groups/top"} "Top ranking group"]
              [:a {:href "/imdb-ranking-groups/average"} "Average ranking group"]
              [:a {:href "/imdb-ranking-groups/bad"} "Bad ranking group"]
              [:a {:href "/movies"} "All movies"]]]])


(defn- menu-layout 
  "Show IMDb groups menu."
  []
  [:div.body
   (groups-menu)])


(defn imdb-ranking-page
  "Show IMDb ranking page." 
  ([uri] (template-page 
           "IMDb ranking" 
           uri 
           (menu-layout))))
