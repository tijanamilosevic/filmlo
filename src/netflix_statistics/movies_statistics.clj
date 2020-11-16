(ns movies-statistics
(:require '[clojure.data.csv :as csv]
          '[clojure.java.io :as io]))

(with-open [reader (io/reader "in-file.csv")] (doall (csv/read-csv reader)))

;;;; naive bayes clasification
;;;; predicates movies popularity
(defn inc-class-total [model class]
(update-in model [class :total] (fnil inc 0)))

(defn inc-predictors-count-fn [row class]
(fn [model attr]
(let [val (get row attr)]
  (update-in model [class attr val] (fnil inc 0)))))

(defn assoc-row-fn [class-attr predictors]
(fn [model row]
(let [class (get row class-attr)]
(reduce (inc-predictors-count-fn row class)
(inc-class-total model class)
predictors))))

(defn bayes-classifier [data class-attr predictors]
(reduce (assoc-row-fn class-attr predictors) {} data))

(defn ex-4-26 []
(->> (load-data "resources/movies.csv")
(:rows)
(bayes-classifier :survived [:sex :pclass])
(clojure.pprint/pprint)))

(defn posterior-probability [model test class-attr]
(let [observed (get-in model [:classes class-attr])
prior (/ (:n observed)
(:n model))]
(apply * prior
(for [[predictor value] test]
(/ (get-in observed [:predictors predictor value])
(:n observed))))))

(defn bayes-classify [model test]
(let [probability (partial posterior-probability model test)
classes (keys (:classes model))]
(apply max-key probability classes)))