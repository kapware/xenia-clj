(ns xenia-clj.system
  (:require [clojure.tools.logging :as log]
            [clojure.tools.namespace.repl :as tn]
            [mount.core :refer [defstate] :as mount]
            [xenia-clj.handler]))

(defn run
  ([] (run {}))
  ([args] (mount/start-with-args args)
   :ready-with-args))

(defn stop []
  (mount/stop))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn reset
  "Stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  (tn/refresh :after 'xenia-clj.system/run))
