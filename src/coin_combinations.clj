(ns coin-combinations
  (:require
    [clojure.core.logic :refer [run* == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))


(comment
  "Coming back to the motivating example problem")

(defn combinations-of-coins-for-cents []
  (run* [num-$ num-half-$ num-quarter num-dime num-nickel num-cent]
        (fresh [sum-without-cents]
               (fd/in num-$ (fd/interval 0 1))
               (fd/in num-half-$ (fd/interval 0 2))
               (fd/in num-quarter (fd/interval 0 4))
               (fd/in num-dime (fd/interval 0 10))
               (fd/in num-nickel (fd/interval 0 20))
               (fd/in num-cent (fd/interval 0 100))
               (fd/in sum-without-cents (fd/interval 0 100))
               (fd/eq
                 (= sum-without-cents (+ (* 100 num-$)
                                         (* 50 num-half-$)
                                         (* 25 num-quarter)
                                         (* 10 num-dime)
                                         (* 5 num-nickel)))
                 (>= 100 sum-without-cents)
                 (= num-cent (- 100 sum-without-cents))))))


(combinations-of-coins-for-cents)

(comment
  "Check if all vectors indeed sum up to 100 cents")

(defn same-value-in-cents? [value-in-cents num-coin-vectors]
  (every? (fn [[num-$ num-half-$ num-quarter num-dime num-nickel num-cent]]
            (= value-in-cents
               (+ (* 100 num-$)
                  (* 50 num-half-$)
                  (* 25 num-quarter)
                  (* 10 num-dime)
                  (* 5 num-nickel)
                  (* num-cent))))
          num-coin-vectors))

(same-value-in-cents? 100 (combinations-of-coins-for-cents))

(comment
  "Function that works for arbitrary value of cents?")

(defn combinations-of-coins-for-cents
  ([]
   (combinations-of-coins-for-cents 100))
  ([value-in-cents]
   (run* [num-$ num-half-$ num-quarter num-dime num-nickel num-cent]
         (fresh [sum-without-cents]
                (fd/in num-$ (fd/interval 0 (int (/ value-in-cents 100))))
                (fd/in num-half-$ (fd/interval 0 (int (/ value-in-cents 50))))
                (fd/in num-quarter (fd/interval 0 (int (/ value-in-cents 25))))
                (fd/in num-dime (fd/interval 0 (int (/ value-in-cents 10))))
                (fd/in num-nickel (fd/interval 0 (int (/ value-in-cents 5))))
                (fd/in num-cent (fd/interval 0 value-in-cents))
                (fd/in sum-without-cents (fd/interval 0 value-in-cents))
                (fd/eq
                  (= sum-without-cents (+ (* 100 num-$)
                                          (* 50 num-half-$)
                                          (* 25 num-quarter)
                                          (* 10 num-dime)
                                          (* 5 num-nickel)))
                  (>= value-in-cents sum-without-cents)
                  (= num-cent (- value-in-cents sum-without-cents)))))))

(combinations-of-coins-for-cents 19)

(comment
  "Apply constraints on the number of specific types
   of coins and the value in cents")

(defn combinations-of-coins-for-cents
  ([]
   (combinations-of-coins-for-cents 100 nil))
  ([value-in-cents]
   (combinations-of-coins-for-cents value-in-cents nil))
  ([value-in-cents {:keys [$ half-$ quarter dime nickel cent]}]
   (run* [num-$ num-half-$ num-quarter num-dime num-nickel num-cent]
         (fresh [sum-without-cents]
                (fd/in num-$ (fd/interval 0 (int (/ value-in-cents 100))))
                (fd/in num-half-$ (fd/interval 0 (int (/ value-in-cents 50))))
                (fd/in num-quarter (fd/interval 0 (int (/ value-in-cents 25))))
                (fd/in num-dime (fd/interval 0 (int (/ value-in-cents 10))))
                (fd/in num-nickel (fd/interval 0 (int (/ value-in-cents 5))))
                (fd/in num-cent (fd/interval 0 value-in-cents))
                (fd/in sum-without-cents (fd/interval 0 value-in-cents))
                (if $
                  (fd/== $ num-$)
                  succeed)
                (if half-$
                  (fd/== half-$ num-half-$)
                  succeed)
                (if quarter
                  (fd/== quarter num-quarter)
                  succeed)
                (if dime
                  (fd/== dime num-dime)
                  succeed)
                (if nickel
                  (fd/== nickel num-nickel)
                  succeed)
                (if cent
                  (fd/== cent num-cent)
                  succeed)
                (fd/eq
                  (= sum-without-cents (+ (* 100 num-$)
                                          (* 50 num-half-$)
                                          (* 25 num-quarter)
                                          (* 10 num-dime)
                                          (* 5 num-nickel)))
                  (>= value-in-cents sum-without-cents)
                  (= num-cent (- value-in-cents sum-without-cents)))))))

(combinations-of-coins-for-cents 43 {:nickel 3 :dime 2})
