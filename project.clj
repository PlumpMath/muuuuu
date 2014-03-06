(defproject muuuuu "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2156"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.5.0-rc1"]
                 [sablono "0.2.6"]
                 [com.facebook/react "0.8.0.1"]]

  :plugins [[lein-cljsbuild "1.0.2"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {
                :output-to "out/muuuuu.js"
                :output-dir "out"
                :optimizations :none
                :source-map true }
              :notify-command ["growlnotify" "-n" "ClojureScript compiler says:" "-m"]}
             {:id "release"
              :source-paths ["src"]
              :compiler {
                :output-to "out2/muuuuu.js"
                :output-dir "out2"
                :optimizations :advanced
                :source-map true}}]})
