(ns netflix-stats
  (:require [template :refer [template-page]]
            [statistics-netflix :refer [sum-tvshows sum-movies sum-all
                                        get-max-duration-movie max-duration-movie
                                        get-min-duration-movie min-duration-movie
                                        get-max-duration-tvshows max-duration-tvshow
                                        min-duration-tvshow movies-duration-average
                                        tvshows-duration-average top-genre
                                        top-release-year top-rating top-country
                                        the-oldest-tvshows
                                        min-year-tvshow max-year-movies
                                        the-oldest-movies min-year-movies]]))


(defn- statistics-one
  "Show total count movies and the oldest movie(s)."
  []
  [:div.movie
   [:h2 "Statistics for Netflix platform"]
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600; "} "Total count"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "All"]
      [:th "Movies"]
      [:th "TV-Shows"]]
     [:tr
      [:td [:div (sum-all)]]
      [:td [:div (sum-movies)]]
      [:td [:div (sum-tvshows)]]]]]])

(defn- the-oldest-movies-table
  "Show the oldest movies."
  []
  [:div.movie
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "The oldest movie(s)"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}
    "The oldest movie is from: " [:b (min-year-movies)]]
   [:div
    [:table
     [:tr
      [:th {:style "width: 400px;"} "The oldest movie(s) :"]]]]])

(defn- the-oldest-tvshows-table
  "Show the oldest tvshows."
  []
  [:div.movie
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "The oldest TV-Show(s))"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}
    "The oldest TV-Show is from: " [:b (min-year-tvshow)]]
   [:div
    [:table
     [:tr
      [:th {:style "width: 400px;"} "The oldest TV-Show(s) :"]]]]])

(defn- the-newest-table
  "Show the newest on netflix"
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "New on Netflix"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}
    "The newest Movies and TV-Shows are from: " [:b (max-year-movies)]]
   [:div {:style "width: 1000px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}
    "The newest on Netflix you can see here: " [:a {:href (str "/netflix-portfolio/" (max-year-movies) "&1")
                                                    :style "color: #ff9600 !important; 
                                                               text-decoration: underline !important;"}
                                                "The newest"]]])

(defn- statistics-two
  "Show min duration for movies."
  []
  [:div.movie
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Min duration for Movies"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}
    "Min duration for movies: " [:b (str min-duration-movie " min")]]
   [:div
    [:table
     [:tr
      [:th {:style "width: 400px;"} "Movie(s) with min duration:"]]]]])

(defn- statistics-three
  "Show max duration for movies."
  []
  [:div.movie
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Max duration for Movies"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}
    "Max duration for movies: " [:b (str max-duration-movie " min")]]
   [:div
    [:table
     [:tr
      [:th {:style "width: 400px;"} "Movie(s) with max duration:"]]]]])

(defn- statistics-four
  "Show min duration tv show."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Min duration for TV-Shows"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}
    "Min duration for TV-Shows: " [:b (str min-duration-tvshow " season(s)")]]
   [:div {:style "width: 1000px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 8px !important;
	              margin-left: 30px !important;"}
    "TV-Shows with min duration you can see here: " [:a {:href (str "/netflix-portfolio/" min-duration-tvshow "&1")
                                                         :style "color: #ff9600 !important; 
                                                               text-decoration: underline !important;"}
                                                     "TV-Show(s) with min duration"]]])

(defn- statistics-five
  "Show max duration for tv show."
  []
  [:div.movie
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Max duration for TV-Shows"]
   [:div {:style "width: 400px !important; 
	              color: #b9b9b9 !important; 
	              font-family: 'Forum-Regular' !important; 
                  font-size: 18px !important;
	              font-weight: normal !important;
	              margin-top: 20px !important;
	              margin-left: 30px !important;"}
    "Max duration for TV-Shows: " [:b (str max-duration-tvshow " seasons")]]
   [:div
    [:table
     [:tr
      [:th {:style "width: 400px;"} "TV-Show(S) with max duration:"]]]]])

(defn- statistics-six
  "Show average runtimes."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Average runtime"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Movies"]
      [:th "TV-Show"]]
     [:tr
      [:td [:div (str (movies-duration-average) " min")]]
      [:td [:div (str (tvshows-duration-average) " seasons")]]]]]])

(defn- statistics-seven
  "Show most repetitive values."
  []
  [:div
   [:h2 {:style "margin-top: 20px; font-size: 25px; color:#ff9600;"} "Most repetitive values"]
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "Year"]
      [:th "Genre"]
      [:th "Country"]
      [:th "Rating system"]]
     [:tr
      [:td [:div (nth (top-release-year) 0)]]
      [:td [:div (nth (top-genre) 0)]]
      [:td [:div (nth (top-country) 0)]]
      [:td [:div (nth (top-rating) 0)]]]]]])

(defn- list-movies
  "List Movies/TVshow."
  [movies]
  [:div
   [:div.form {:style "min-height: 50px !important;"}
    [:table {:frame "box"}
     [:tr
      [:th "No."]
      [:th "Title"]
      [:th "Type"]
      [:th "Year"]
      [:th "Duration"]
      [:th "Country"]]
     (let [x (atom {})]
       (swap! x assoc :no 0)
       (for [movie movies]
         (identity [:tr
                    [:td [:div (:no (swap! x update :no inc))]]
                    [:td [:div (movie :title)]]
                    [:td [:div (movie :type)]]
                    [:td [:div (movie :release_year)]]
                    [:td [:div (movie :duration)]]
                    [:td [:div (movie :country)]]])))]]])



(defn- layout
  "Show statistics."
  []
  [:div.body
   (statistics-one)
   (the-oldest-movies-table)
   (list-movies (the-oldest-movies))
   (the-oldest-tvshows-table)
   (list-movies (the-oldest-tvshows))
   (the-newest-table)
   (statistics-two)
   (list-movies (get-min-duration-movie))
   (statistics-three)
   (list-movies (get-max-duration-movie))
   (statistics-four)
   (statistics-five)
   (list-movies (get-max-duration-tvshows))
   (statistics-six)
   (statistics-seven)])

(defn netflix-stats-page
  "Show Netflix statistics page."
  ([uri] (template-page
          "Netflix statistics"
          uri
          (layout))))
