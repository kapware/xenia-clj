(set-env!
  :name "xenia-clj"
  :source-paths #{"src/clj"}
  :resource-paths #{"resources"}

  :dependencies '[[org.clojure/clojure "1.8.0"]
                  [boot/core "2.5.5" :scope "provided"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "provided"]
                  [org.clojure/tools.namespace "0.2.11" :scope "provided"]
                  [org.clojure/tools.cli "0.3.3"]
                  [org.clojure/tools.logging "0.3.1"]
                  [org.clojure/data.json "0.2.6"]

                  [mount "0.1.10"]
                  [helpful-loader "0.1.1"]

                  [adzerk/boot-reload "0.4.7" :scope "provided"]
                  [mbuczko/boot-build-info "0.1.1" :scope "provided"]
                  [org.slf4j/slf4j-nop "1.7.12"]

                  [metosin/compojure-api "1.1.0"]
                  [prismatic/schema "1.1.1"]

                  [http-kit "2.1.19"]
                  [ring-server "0.4.0"]
                  [ring/ring-defaults "0.2.0"]

                  [jeluard/boot-notify "0.2.1" :scope "test"]
                  [zilti/boot-midje "0.2.1-SNAPSHOT" :scope "test"]
                  [midje "1.8.3" :scope "test"]]

  :version-config {:tag-prefix          "xenia-clj"
                   :version-creator     "versionWithBranch"})

(require
  '[xenia-clj.system]
  '[adzerk.boot-reload :refer [reload]]
  '[zilti.boot-midje    :refer [midje]]
  '[mbuczko.boot-build-info :refer [build-info]])



(deftask dev []
         (comp (watch)
               (speak)))

(deftask package
         "Package project"
  []
  (comp (build-info)
     (aot)
     (pom)
     (jar)
     (target)))

(deftask reset []
         (xenia-clj.system/reset))

(deftask go
         [e env ENVIRONMENT str "Environment to use while starting application up."]
         (xenia-clj.system/run {:env (or env "local")}))

(task-options! midje {:test-paths #{"test/clj"}}
               aot {:all true})

