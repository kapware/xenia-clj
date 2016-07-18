(ns xenia-clj.config
  (:require [clojure.tools.logging :as log]
            [helpful-loader.edn :as edn-loader]
            [mount.core :refer [defstate] :as mount]))

(defn load-config
  "Loads configuration file depending on environment"
  [env]

  (log/info "Loading\033[1;31m" env "\033[0mconfig")
  (merge-with conj
              {:is-prod? (= env "prod")
               :is-test? (= env "test")
               :is-dev?  (or (= env "dev") (= env "local"))}
              (edn-loader/load-one-or-nil "app.edn")
              (edn-loader/load-one-or-nil (str "app-" env ".edn"))))

(defstate app-config
          :start (load-config (or (:env (mount/args)) "local")))
