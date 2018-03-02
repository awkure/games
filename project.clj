(defproject games "0.1.0-SNAPSHOT"
  :description "A collection of cli games I ended up making when learning clojure"
  :url "https://github.com/awkure/games"
  :license {:name "Eclipse Public License"
            :url "https://github.com/awkure/games/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :min-lein-version "2.0.0"
  :main ^:skip-aot games.core
  :target-path "target/%s"
  :mirrors {"clojure" {:url "https://build.clojure.org/releases/"}
            "clojure-snapshots" {:url "https://build.clojure.org/snapshots/"}}
  :profiles {:uberjar {:aot :all}})
