(ns xenia-clj.json
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [ring.util.response :as r]))


(defn- parse-request [{:keys [body status] :as r}]
  (if (< 299 status)
    (println r))
  (if (empty? body) {}
                    (json/read-str body :key-fn keyword)))

(defn fetch
      ([url]
        (parse-request @(http/get url)))
      ([url headers]
        (parse-request @(http/get url {:headers headers}))))

(defn json-request-params
  ([]
   {:headers {"Content-Type" "application/json"
              "Accept"       "application/json"}})
  ([body]
   (assoc (json-request-params) :body (json/write-str body))))


(defn post [url body]
  (let [response @(http/post url (json-request-params body))]
    response))

(defn response [body]
  (-> (r/response body)
      (r/header "Content-Type" "application/json")))
