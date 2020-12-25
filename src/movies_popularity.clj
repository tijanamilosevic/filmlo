(ns movies-popularity
  (:require [template :refer [template-page]]
            [movies-statistics :refer [search-by-genre search-by-country search-by-runtime
                                       search-by-year search-by-title search-by-imdb search-by-language]]
            [popularity-prediction :refer [popular-platform-movies search-by-platform]]
            [hiccup.form :refer [form-to text-field submit-button]]))


(defn- movie-search-box
  "Show movie search form."
  []
  [:div.movie
   [:h2 "Search popular movies on Netflix, Hulu, Disney+ and Prime Video:"]
   (form-to [:post "/movies-popularity"]
            [:table
             [:tr
              [:th {:style "width: 400px;"} "Search by Movie title, release year, country, IMDb rating, language, duration, genre and platform: "]]
             [:tr
              [:td
               (text-field :criteria)
               (submit-button "Search")]]
             [:tr
              [:td [:div "*type 'all' to get full list"]]]])])


(defn- list-movies
  "List popular movies."
  [movies]
  [:div
   [:h2 "Popular movies"]
   [:div.form
    [:table {:frame "box"}
     [:tr
      [:th "No."]
      [:th "Title"]
      [:th "Genre"]
      [:th "Year"]
      [:th "Duration"]
      [:th "Country"]
      [:th "IMDb Rating"]
      [:th "Language"]
      [:th "Rotten Tomatoes"]
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
                    [:td [:div (movie :language)]]
                    [:td [:div (movie :rotten-tomatoes)]]
                    [:td [:div (movie :platform)]]])))]]])


(defn- pagination
  "Creates pagination part on page."
  [criteria page last]
  (if-not (= 0 last)
    [:p
     (if-not (= 1 page)
       [:span
        [:a {:href (str "/movies-popularity/" criteria "&1")} "<< First"] " "
        (if-not (= 2 page)
          [:a {:href (str "/movies-popularity/" criteria "&" (- page 1))} "< Previous"])])
     (if-not (= 1 last)
       [:span " " [:b (str page " of " last " pages")] " "])
     (if-not (= last page)
       [:span
        [:a {:href (str "/movies-popularity/" criteria "&" (+ page 1))} "Next >"] " "
        (if-not (= (- last 1) page)
          [:a {:href (str "/movies-popularity/" criteria "&" last)} "Last >>"])])]))


(defn- movies-layout
  "Show movies search form, pagination and list movies."
  [movies-list criteria page]
  [:div.body
   (movie-search-box)
   (let [movies (take 10 (drop (* 10 (- page 1)) movies-list))]
     (if-not (or (= criteria "") (clojure.string/blank? criteria))
       (if-not (empty? movies)
         [:div
          [:div {:style "float: right;"}
           (pagination criteria page
                       (let [number-of-pages (/ (count movies-list) 10)]
                         (if (ratio? number-of-pages)
                           (int (inc (Math/floor (double number-of-pages))))
                           number-of-pages)))]
          [:div {:style "float: right;"}
           [:p
            [:span (str (count movies-list) " results " "found")]]]
          (list-movies movies)]
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
  "Search by Movie title, release year, country, IMDb rating, languages, duration, genre and platform."
  [criteria]
  (cond
    (not-empty (search-by-year criteria (popular-platform-movies))) (search-by-year criteria (popular-platform-movies))
    (not-empty (search-by-title criteria (popular-platform-movies))) (search-by-title criteria (popular-platform-movies))
    (not-empty (search-by-country criteria (popular-platform-movies))) (search-by-country criteria (popular-platform-movies))
    (not-empty (search-by-genre criteria (popular-platform-movies))) (search-by-genre criteria (popular-platform-movies))
    (not-empty (search-by-platform criteria (popular-platform-movies))) (search-by-platform criteria (popular-platform-movies))
    (not-empty (search-by-language criteria (popular-platform-movies))) (search-by-language criteria (popular-platform-movies))
    (not-empty (search-by-imdb criteria (popular-platform-movies))) (search-by-imdb criteria (popular-platform-movies))
    (not-empty (search-by-runtime criteria (popular-platform-movies))) (search-by-runtime criteria (popular-platform-movies))
    :else nil))

(defn movies-popularity-page
  "Show Movies-popularity page depending on search criteria."
  ([uri] (template-page
          "Popular movies"
          uri
          (movies-layout (popular-platform-movies) "all" 1)))
  ([uri criteria page] (template-page
                        "Popular movies"
                        uri
                        (movies-layout (if (= criteria "all")
                                         (popular-platform-movies)
                                         (get-data-by-search-criteria criteria))
                                       criteria page))))
