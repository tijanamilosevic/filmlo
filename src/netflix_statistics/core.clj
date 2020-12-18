(ns netflix-statistics.core
  (:require [compojure.route :as route]
            [noir.session :as session]
            [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.middleware.params :refer [wrap-params]]
            [index :refer [index-page]]
            [netflix-portfolio :refer [netflix-portfolio-page]]
            [movies :refer [movies-page]]
            [hulu :refer [hulu-page]]
            [disney :refer [disney-page]]
            [prime :refer [prime-page]]
            [movies-popularity :refer [movies-popularity-page]]
            [imdb-ranking :refer [imdb-ranking-page]]
            [top-ranking :refer [top-page]]
            [average-ranking :refer [average-page]]
            [bad-ranking :refer [bad-page]]
            [movies-stats :refer [movies-stats-page]]))
  
(defroutes handler
  (GET "/" [] (index-page "/"))
  (GET "/netflix-portfolio" [] (netflix-portfolio-page "/netflix-portfolio"))
  (POST "/netflix-portfolio" [criteria] 
        (netflix-portfolio-page "/netflix-portfolio" criteria 1))
  (GET "/netflix-portfolio/:criteria&:page" [criteria page]
      (netflix-portfolio-page "/netflix-portfolio" criteria (Integer/valueOf page)))
  (GET "/movies" [] (movies-page "/movies"))
  (POST "/movies" [criteria] 
        (movies-page "/movies" criteria 1))
  (GET "/movies/:criteria&:page" [criteria page]
      (movies-page "/movies" criteria (Integer/valueOf page)))
  (GET "/movies/hulu" [] (hulu-page "/movies/hulu"))
  (POST "/movies/hulu" [criteria] 
        (hulu-page "/movies/hulu" criteria 1))
  (GET "/movies/hulu/:criteria&:page" [criteria page]
      (hulu-page "/movies/hulu" criteria (Integer/valueOf page)))
  (GET "/movies/disney" [] (disney-page "/movies/disney"))
  (POST "/movies/disney" [criteria] 
        (disney-page "/movies/disney" criteria 1))
  (GET "/movies/disney/:criteria&:page" [criteria page]
      (disney-page "/movies/disney" criteria (Integer/valueOf page))) 
  (GET "/movies/prime" [] (prime-page "/movies/prime"))
  (POST "/movies/prime" [criteria] 
        (prime-page "/movies/prime" criteria 1))
  (GET "/movies/prime/:criteria&:page" [criteria page]
      (prime-page "/movies/prime" criteria (Integer/valueOf page)))
  (GET "/movies-popularity" [] (movies-popularity-page "/movies-popularity"))
  (POST "/movies-popularity" [criteria] 
        (movies-popularity-page "/movies-popularity" criteria 1))
  (GET "/movies-popularity/:criteria&:page" [criteria page]
      (movies-popularity-page "/movies-popularity" criteria (Integer/valueOf page)))
  (GET "/imdb-ranking-groups" [] (imdb-ranking-page "/imdb-ranking-groups"))  
  (GET "/imdb-ranking-groups/top" [] (top-page "/imdb-ranking-groups/top"))
  (POST "/imdb-ranking-groups/top" [criteria] 
        (top-page "/imdb-ranking-groups/top" criteria 1))
  (GET "/imdb-ranking-groups/top/:criteria&:page" [criteria page]
      (top-page "/imdb-ranking-groups/top" criteria (Integer/valueOf page))) 
  (GET "/imdb-ranking-groups/average" [] (average-page "/imdb-ranking-groups/average"))
  (POST "/imdb-ranking-groups/average" [criteria] 
        (average-page "/imdb-ranking-groups/average" criteria 1))
  (GET "/imdb-ranking-groups/average/:criteria&:page" [criteria page]
      (average-page "/imdb-ranking-groups/top" criteria (Integer/valueOf page))) 
  (GET "/imdb-ranking-groups/bad" [] (bad-page "/imdb-ranking-groups/bad"))
  (POST "/imdb-ranking-groups/bad" [criteria] 
        (bad-page "/imdb-ranking-groups/bad" criteria 1))
  (GET "/imdb-ranking-groups/bad/:criteria&:page" [criteria page]
      (bad-page "/imdb-ranking-groups/bad" criteria (Integer/valueOf page)))
  (GET "/movies-stats" [] (movies-stats-page "/movies-stats"))
  
  (route/resources "/")
  (route/not-found "404 Page not found"))

(def app
  (-> #'handler
    (wrap-params)
    (session/wrap-noir-flash)
    (wrap-stacktrace)))

(defn start-server [port] 
  (run-jetty #'app {:port port :join? false})
  (println (str "\nWelcome to FilmLo- movies statistics and predictions! Browse to http://localhost:" 
                port 
                " to see statistics and predictions now!")))

(defn -main [port]
  (start-server port))
