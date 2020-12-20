(ns movies-stats
  (:require [template :refer [template-page]]
            [movies-statistics :refer [num-movies the-oldest-movie-year 
                                       get-the-oldest-movie the-newest-movie-year
                                       average-runtime
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
                                       average-imdb min-imdb-prime max-imdb-prime
                                       min-imdb-disney max-imdb-disney 
                                       min-imdb-hulu max-imdb-hulu
                                       min-imdb-netflix max-imdb-netflix
                                       min-imdb-movies max-imdb-movies
                                       mode-imdb mode-country mode-genre
                                       mode-language mode-duration imdb-standard-deviation]]))


(defn- statistics-one 
  "Show total count movies and the oldest movie(s)."
  []
  [:div.movie
   [:h2 "Statistics for movies from Netflix, Hulu, Disney+ and Prime Video"]
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
   "The newest movie(s) you can see here: " [:a {:href (str "/movies/" the-newest-movie-year "&1")
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
                  [:td [:div (str (max-duration-movies) " min")]]
                  [:td [:div (str (min-duration-movies) " min")]]])]]])

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
                  [:td [:div (str (max-duration-netflix) " min")]]
                  [:td [:div (str (max-duration-hulu) " min")]]
                  [:td [:div (str (max-duration-disney) " min")]]
                  [:td [:div (str (max-duration-prime) " min")]]])]]])

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
                  [:td [:div (str (min-duration-netflix) " min")]]
                  [:td [:div (str (min-duration-hulu) " min")]]
                  [:td [:div (str (min-duration-disney) " min")]]
                  [:td [:div (str (min-duration-prime) " min")]]])]]])

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

(defn- imdb-table
  "Show imdb standard deviation, max and min duration."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "IMDb numbers"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Average deviation from the mean value"]
      [:th "Max IMDb"]
      [:th "Min IMDb"] ]
       (identity [:tr
                  [:td [:div (imdb-standard-deviation)]]
                  [:td [:div (max-imdb-movies)]]
                  [:td [:div (min-imdb-movies)]]])]]])

(defn- statistics-three 
  "Show max/min imdb movies links."
  []
  [:div
  [:div {:style "width: 1000px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}  
   "Movies with max IMDb you can see here: " [:a {:href (str "/movies/" (max-imdb-movies) "&1")
                                                  :style "color: #ff9600 !important; 
                                                          text-decoration: underline !important;"} 
                                              "The best IMDb rating movies"]]
  [:div {:style "width: 1000px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}  
   "Movies with min IMDb you can see here: " [:a {:href (str "/movies/" (min-imdb-movies) "&1")
                                                 :style "color: #ff9600 !important; 
                                                         text-decoration: underline !important;"} 
                                             "The worst IMDb rating movies"]]])

(defn- max-imdb-table
  "Show max IMDb table."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Max IMDb rating per platform"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Netflix"]
      [:th "Hulu"]
      [:th "Disney+"]
      [:th "Prime Video"]]
       (identity [:tr
                  [:td [:div (max-imdb-netflix)]]
                  [:td [:div (max-imdb-hulu)]]
                  [:td [:div (max-imdb-disney)]]
                  [:td [:div (max-imdb-prime)]]])]]])

(defn- min-imdb-table
  "Show min IMDb table."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Min IMDb rating per platform"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Netflix"]
      [:th "Hulu"]
      [:th "Disney+"]
      [:th "Prime Video"]]
       (identity [:tr
                  [:td [:div (min-imdb-netflix)]]
                  [:td [:div (min-imdb-hulu)]]
                  [:td [:div (min-imdb-disney)]]
                  [:td [:div (min-imdb-prime)]]])]]])

(defn- mode-table
  "Show most repetitive values."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Most repetitive values"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Duration"]
      [:th "Language"]
      [:th "Genre"]
      [:th "Country"]
      [:th "IMDb"]]
       [:tr
        [:td [:div (str (nth (mode-duration) 0) " min")]]
        [:td [:div (nth (mode-language) 0)]]
        [:td [:div (nth (mode-genre) 0)]]
        [:td [:div (nth (mode-country) 0)]]
        [:td [:div (nth (mode-imdb) 0)]]]
     [:tr
        [:td 
         [:div 
          [:a {:href (str "/movies/" (nth (mode-duration) 0) "&1")
               :style "color: #ff9600 !important; text-decoration: underline !important;"} 
              "See movies"]]]
        [:td 
         [:div 
          [:a {:href (str "/movies/" (nth (mode-language) 0) "&1")
               :style "color: #ff9600 !important; text-decoration: underline !important;"} 
              "See movies"]]]
        [:td 
         [:div 
          [:a {:href (str "/movies/" (nth (mode-genre) 0) "&1")
               :style "color: #ff9600 !important; text-decoration: underline !important;"} 
               "See movies"]]]
        [:td 
         [:div 
          [:a {:href (str "/movies/" (nth (mode-country) 0) "&1")
               :style "color: #ff9600 !important; text-decoration: underline !important;"} 
              "See movies"]]]
        [:td 
         [:div 
          [:a {:href (str "/movies/" (nth (mode-imdb) 0) "&1")
               :style "color: #ff9600 !important; text-decoration: underline !important;"} 
              "See movies"]]]]]]])

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
   (average-imdb-table)
   (imdb-table)
   (statistics-three)
   (max-imdb-table)
   (min-imdb-table)
   (mode-table)])


(defn movies-stats-page
  "Show statistics." 
  ([uri] (template-page 
           "Movies statistics" 
           uri 
           (movies-layout (get-the-oldest-movie)))))