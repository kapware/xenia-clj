(ns xenia-clj.handler
  (:require [compojure.api.exception :as ex]
            [compojure.api.sweet :refer :all]
            [compojure.route :refer [not-found resources]]
            [mount.core :as mount :refer [defstate]]
            [xenia-clj.config :refer [app-config]]
            [xenia-clj.json :as json]
            [org.httpkit.server :as web]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.http-response :as res]
            [ring.util.response :refer [response]]))

(defn custom-handler [^Exception e data request]
  (res/internal-server-error {:message (.getMessage e)}))

(defn routes-api []
  (api
    {:exceptions {:handlers {::ex/default (ex/with-logging custom-handler)}}

     :swagger    {:ui   "/api-docs"
                  :spec "/swagger.json"
                  :data {:info {:version "0.1.0"
                                :title   "xenia-clj API"}
                         :tags [{:name "api"}
                                {:name "status"}]}}}
    (context "/api" []
             :tags ["api"]
             (GET "/todos" []
                  (json/response (:todos app-config))))
    (context "/status" []
             :tags ["status"]
             (GET "/info" [] (json/response {:status "OK"}))
             (GET "/ping" [] (response "pong")))
    (OPTIONS "/" [] (response ""))

    (resources "/scripts" {:root "scripts"})
    (resources "/")
    (not-found "Not Found")))

(defn init-server [conf]
  {:http (web/run-server
           (routes-api)
           (merge (:server conf) (mount/args)))})

(defn shutdown-server [server]
  ((:http server)))

(defstate http-server
          :start (init-server app-config)
          :stop (shutdown-server http-server))
