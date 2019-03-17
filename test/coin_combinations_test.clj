(ns coin-combinations-test
  (:require [midje.sweet :refer :all]
            [coin-combinations :as cc]))

(defn same-value-in-cents? [value-in-cents
                            num-coin-vectors]
  (every? (fn [[num-$ num-half-$ num-quarter num-dime num-nickel num-cent]]
            (= value-in-cents
               (+ (* 100 num-$)
                  (* 50 num-half-$)
                  (* 25 num-quarter)
                  (* 10 num-dime)
                  (* 5 num-nickel)
                  (* num-cent))))
          num-coin-vectors))

(fact
  "Total number of combinations of
   combinations-of-coins-for-cents-v1 is 293"
  (count (cc/combinations-of-coins-for-cents-v1))
  => 293)

(fact
  "All combinations of combinations-of-coins-for-cents-v1
   sum up to 100 cents"
  (same-value-in-cents?
    100
    (cc/combinations-of-coins-for-cents-v1))
  => true)

(fact
  "All combinations of combinations-of-coins-for-cents-v2
   sum up to the number received from argument"
  (same-value-in-cents?
    19
    (cc/combinations-of-coins-for-cents-v2 19))
  => true)

(fact
  "All combinations of combinations-of-coins-for-cents-v3
   sum up to the number received from argument"
  (same-value-in-cents?
    43
    (cc/combinations-of-coins-for-cents-v3 43 {:dime 2 :nickel 3}))
  => true)

(fact
  "All combinations of combinations-of-coins-for-cents-v3
   satisfies specified number of coins"
  (every?
    (fn [[num-$ num-half-$ num-quarter
          num-dime num-nickel num-cent]]
      (and (= 2 num-dime)
           (= 3 num-nickel)))
    (cc/combinations-of-coins-for-cents-v3 43 {:dime 2 :nickel 3}))
  => true)
