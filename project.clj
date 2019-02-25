(defproject c-logic-walkthrough "0.1.0-SNAPSHOT"
  :description "Core logic in Clojure"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.logic "0.8.11"]
                 ]
  :profiles {:dev {:dependencies [[midje "1.9.6"]]
                   :plugins      [[lein-ancient "0.6.15"]
                                  [lein-midje "3.2.1"]
                                  [lein-bikeshed "0.5.1"]
                                  [lein-nsorg "0.2.0"]]
                   :aliases      {"test-ancient" "midje"
                                  "upgrade-deps" ["ancient" "upgrade" ":all" ":check-clojure"]}
                   :bikeshed     {:max-line-length 120
                                  :docstrings      false}}})
