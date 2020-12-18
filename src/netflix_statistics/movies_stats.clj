(ns movies-stats
  (:require [template :refer [template-page]]
            [movies-statistics :refer [num-movies the-oldest-movie-year 
                                       get-the-oldest-movie the-newest-movie-year
                                       get-the-newest-movie average-runtime
                                       prime-average-runtime disney-average-runtime
                                       hulu-average-runtime netflix-average-runtime
                                       movies-standard-deviation
                                       min-duration-prime max-duration-prime
                                       min-duration-disney max-duration-disney
                                       min-duration-hulu max-duration-hulu
                                       min-duration-netflix max-duration-netflix
                                       min-duration-movies max-duration-movies
                                       average-imdb-prime average-imdb-disney
                                       average-imdb-hulu average-imdb-netflix
                                       average-imdb]]))


(defn- statistics-one 
  "Show total count movies and the oldest movie(s)."
  []
  [:div.movie
   [:h2 "Statistics for movies from Netflix, Hulu, Disney+ and prime video."]
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600; "} "Total count"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}  
    "Total count movies: " [:b num-movies]]
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "The oldest/newest movie(s)"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}  
    "The oldest movie is from: " [:b the-oldest-movie-year]]
   [:div 
    [:table
        [:tr
          [:th {:style "width: 400px;"} "The oldest movie(s):"]]]]])


(defn- statistics-two 
  "Show the newest movie(s)."
  []
  [:div
  [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}  
   "The newest movie is from: " [:b the-newest-movie-year]]
  [:div {:style "width: 1000px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}  
   "The newest movie(s) you can see here: " [:a {:href "/movies/2020&1"
                                                 :style "color: #ff9600 !important; 
                                                         text-decoration: underline !important;"} 
                                             "The newest movies"]]])

(defn- average-duration-table
  "Show average duration table."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Average duration"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "All movies"]
      [:th "Netflix"]
      [:th "Hulu"]
      [:th "Disney+"]
      [:th "Prime Video"]]
       (identity [:tr
                  [:td [:div (str (average-runtime) " min")]]
                  [:td [:div (str (netflix-average-runtime) " min")]]
                  [:td [:div (str (hulu-average-runtime) " min")]]
                  [:td [:div (str (disney-average-runtime) " min")]]
                  [:td [:div (str (prime-average-runtime) " min")]]])]]])


(defn- duration-table
  "Show duration standard deviation, max and min duration."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Duration numbers"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Average deviation from the mean value"]
      [:th "Max duration"]
      [:th "Min duration"] ]
       (identity [:tr
                  [:td [:div (str (movies-standard-deviation) " min")]]
                  [:td [:div (str max-duration-movies " min")]]
                  [:td [:div (str min-duration-movies " min")]]])]]])

(defn- max-duration-table
  "Show max duration table."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Max duration per platform"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Netflix"]
      [:th "Hulu"]
      [:th "Disney+"]
      [:th "Prime Video"]]
       (identity [:tr
                  [:td [:div (str max-duration-netflix " min")]]
                  [:td [:div (str max-duration-hulu " min")]]
                  [:td [:div (str max-duration-disney " min")]]
                  [:td [:div (str max-duration-prime " min")]]])]]])

(defn- min-duration-table
  "Show min duration table."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Min duration per platform"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Netflix"]
      [:th "Hulu"]
      [:th "Disney+"]
      [:th "Prime Video"]]
       (identity [:tr
                  [:td [:div (str min-duration-netflix " min")]]
                  [:td [:div (str min-duration-hulu " min")]]
                  [:td [:div (str min-duration-disney " min")]]
                  [:td [:div (str min-duration-prime " min")]]])]]])

(defn- average-imdb-table
  "Show average imdbtable."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Average IMDb rate"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "All movies"]
      [:th "Netflix"]
      [:th "Hulu"]
      [:th "Disney+"]
      [:th "Prime Video"]]
       (identity [:tr
                  [:td [:div (average-imdb)]]
                  [:td [:div (average-imdb-netflix)]]
                  [:td [:div (average-imdb-hulu)]]
                  [:td [:div (average-imdb-disney)]]
                  [:td [:div (average-imdb-prime)]]])]]])

(defn- list-movies
  "List Movies."
  [movies]
  [:div
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "No."]
      [:th "Title"]
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
                  [:td [:div (movie :title)]]
                  [:td [:div (movie :genres)]]
                  [:td [:div (movie :year)]]
                  [:td [:div (movie :runtime)]]
                  [:td [:div (movie :country)]]
                  [:td [:div (movie :imdb)]]])))]]])

(defn- movies-layout 
  "Show list movies."
  [movies-list]
  [:div.body
   (statistics-one)
   (list-movies movies-list)
   (statistics-two)
   (average-duration-table)
   (duration-table)
   (max-duration-table)
   (min-duration-table)
   (average-imdb-table)])


(defn movies-stats-page
  "Show statistics." 
  ([uri] (template-page 
           "Movies statistics" 
           uri 
           (movies-layout (get-the-oldest-movie)))))