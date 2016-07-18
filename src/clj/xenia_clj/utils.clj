(ns xenia-clj.utils
  (:require [clojure.data.json :as json]))

(defn json-request-params
  ([]
   {:headers {"Content-Type" "application/json"
              "Accept"       "application/json"}})
  ([body]
   (assoc (json-request-params) :body (json/write-str body))))

(def find-first (comp first filter))

(defn find-by [field val coll]
  (find-first #(= (field %) val) coll))

(defn merge-by [[left-key right-key] left right]
  (let [lefts  (group-by left-key left)
        rights (group-by right-key right)]
    (for [[k v] lefts]
      [k (concat v (get rights k))])))

(defn join-by [[left-key right-key] left right]
  (let [lefts  (group-by left-key left)
        rights (group-by right-key right)]
    (for [[k v] lefts]
      [k (concat v (get rights k))])))
