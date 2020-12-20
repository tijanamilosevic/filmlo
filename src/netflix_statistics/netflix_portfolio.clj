(ns netflix-portfolio
  (:require [template :refer [template-page]]
            [statistics-netflix :refer [netflix-portfolio search-by-year search-by-name
                                        search-by-country search-by-season search-by-type-tv-show
                                        search-by-type-movie search-by-duration]]
            [hiccup.form :refer [form-to text-field submit-button]]))


(defn- movie-search-box 
  "Show movie search form."
  []
  [:div.movie
   [:h2 "Search Netflix portfolio:"]
   (form-to [:post "/netflix-portfolio"]
            [:table
             [:tr
              [:th {:style "width: 400px;"} "Search by Movie/TV Show title, release year, country, type (Movie/TV Show), duration: "]]
             [:tr
              [:td
               (text-field :criteria)
               (submit-button "Search")]]
             [:tr
              [:td [:div "*type 'all' to get full list"]]]])])


(defn- list-portfolio 
  "List Netflix portfolio."
  [movies]
  [:div
   [:h2 "Netflix portfolio"]
   [:div.form
    [:table {:frame "box"}
     [:tr
      [:th "No."]
      [:th "Name"]
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


(defn- pagination
  "Creates pagination part on page."
  [criteria page last]
  (if-not (= 0 last) 
    [:p
     (if-not (= 1 page)
       [:span
        [:a {:href (str "/netflix-portfolio/" criteria "&1")} "<< First"] " "
        (if-not (= 2 page)
          [:a {:href (str "/netflix-portfolio/" criteria "&" (- page 1))} "< Previous"])])  
     (if-not (= 1 last)
       [:span " " [:b (str page " of " last " pages")] " "])    
     (if-not (= last page)
       [:span
        [:a {:href (str "/netflix-portfolio/" criteria "&" (+ page 1))} "Next >"] " "
        (if-not (= (- last 1) page)
          [:a {:href (str "/netflix-portfolio/" criteria "&" last)} "Last >>"])])]))  


(defn- portfolio-layout 
  "Show portfolio search form, pagination and list portfolio."
  [portfolio criteria page]
  [:div.body
   (movie-search-box)
   (let [movies (take 10 (drop (* 10 (- page 1)) portfolio))]
    (if-not (or (= criteria "") (clojure.string/blank? criteria))
       (if-not (empty? movies) 
       [:div
        [:div {:style "float: right;"}
         (pagination criteria page
                     (let [number-of-pages (/ (count portfolio) 10)]
                       (if (ratio? number-of-pages)
                         (int (inc (Math/floor (double number-of-pages))))
                         number-of-pages)))]
        [:div {:style "float: right;"}
         [:p
          [:span (str (count portfolio)" results " "found")]]]
        (list-portfolio movies)]
       (if (= criteria "all")
         [:p "List is empty."]
         [:p "No matching data."]))
      [:div {:class "message"} "You must enter search criteria!"]))])

(defn try-convert-string 
  "Converts string to integer."
  [str]
  (try 
  (Integer/valueOf str)
  (catch Exception e (identity 0))))

(defn- get-data-by-search-criteria 
  "Search by movie-tv-show title, release year, country, type (movie/tv-show), number of seasons for tv shows."
  [criteria]
  (cond 
    (not-empty (search-by-year (try-convert-string criteria))) (search-by-year (try-convert-string criteria))
    (not-empty (search-by-name criteria)) (search-by-name criteria)
    (not-empty (search-by-country criteria)) (search-by-country criteria)
    (not-empty (search-by-type-movie criteria)) (search-by-type-movie criteria)
    (not-empty (search-by-type-tv-show criteria)) (search-by-type-tv-show criteria)
    (not-empty (search-by-season (try-convert-string criteria))) (search-by-season (try-convert-string criteria))
    (not-empty (search-by-duration criteria)) (search-by-duration criteria)
    :else nil))

(defn netflix-portfolio-page
  "Show Netflix-portfolio page depending on search criteria." 
  ([uri] (template-page 
           "Netflix portfolio" 
           uri 
           (portfolio-layout netflix-portfolio "all" 1)))
  ([uri criteria page] (template-page 
                         "Netflix portfolio" 
                         uri 
                         (portfolio-layout (if (= criteria "all")
                                              netflix-portfolio
                                             (get-data-by-search-criteria criteria)) 
                                           criteria page))))
