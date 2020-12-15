(ns netflix-statistics.core
  (:require [compojure.route :as route]
            [noir.session :as session]
            [ring.util.response :as response]
            [compojure.core :refer [defroutes GET POST]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [ring.middleware.params :refer [wrap-params]]
            [index :refer [index-page]]))

   
(defroutes handler
  (GET "/" [] (index-page "/"))
  (route/resources "/")
  (route/not-found "404 Page not found"))


(def app
  (-> #'handler
    (session/wrap-noir-flash)
    (wrap-stacktrace)))

(defn start-server [port] 
  (run-jetty #'app {:port port :join? false})
  (println (str "\nWelcome to FilmLo- movies statistics and predictions! Browse to http://localhost:" 
                port 
                " to see statistics and predictions now!")))

(defn -main [port]
  (start-server port))
