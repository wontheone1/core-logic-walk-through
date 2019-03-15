(ns verbal-arithmetic
  (:require
    [clojure.core.logic :refer [all run* everyg lvar == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))

(comment
  " SEND
  + MORE
  ______
   MONEY")

(defn send-more-money-solutions []
  (run* [s e n d m o r y]
        (fd/in s e n d m o r y (fd/interval 0 9))
        (fd/!= s 0)
        (fd/!= m 0)
        (fd/distinct [s e n d m o r y])
        (fd/eq (= (+ (* 1000 s) (* 100 e) (* 10 n) d
                     (* 1000 m) (* 100 o) (* 10 r) e)
                  (+ (* 10000 m) (* 1000 o) (* 100 n) (* 10 e) y)))))

(defn pprint-sendmory [[s e n d m o r y]]
  (println (str "  " s e n d))
  (println (str "+ " m o r e))
  (println (str "______"))
  (println (str " " m o n e y)))

; (= 10652 (+ 9567 1085))
