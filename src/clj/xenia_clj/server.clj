(ns xenia-clj.server
  (:gen-class)
  (:require [xenia-clj.system :as system]
            [clojure.tools.logging :as log]
            [clojure.tools.cli :refer [parse-opts]]))

(def cli-options
  ;; An option with a required argument
  [["-p" "--port PORT" "Port number"
    :default 8080
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
   ["-e" "--env ENV" "environment"
    :default "local"]])

(defn -main [& args]
  (let [options (:options (parse-opts args cli-options))
        {:keys [env port]} options]

    (system/run {:env env :port port})

    (log/info "Started successfully with ENV" env)
    (log/info "Visit" (str "http://localhost:" port "/"))))
