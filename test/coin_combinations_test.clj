(ns coin-combinations-test
  (:require [midje.sweet :refer :all]
            [coin-combinations :as cc]))

(defn- sum-coin-map-value [{:keys [$ half-$ quarter dime nickel cent]}]
  (+ (* 100 $)
     (* 50 half-$)
     (* 25 quarter)
     (* 10 dime)
     (* 5 nickel)
     (* cent)))

(defn same-value-in-cents? [value-in-cents
                            num-coin-vectors]
  (every? (fn [coin-combination]
            (cond
              (vector? coin-combination)
              (let [[num-$ num-half-$ num-quarter
                     num-dime num-nickel num-cent]
                    coin-combination]
                (= value-in-cents
                   (sum-coin-map-value
                     {:$       num-$
                      :half-$  num-half-$
                      :quarter num-quarter
                      :dime    num-dime
                      :nickel  num-nickel
                      :cent    num-cent})))

              (map? coin-combination)
              (= value-in-cents
                 (sum-coin-map-value coin-combination))))
          num-coin-vectors))

(fact
  "Total number of combinations of
   combinations-of-coins-for-cents-v1 is 293"
  (count (cc/combinations-of-coins-for-cents-v1-optimized))
  => 293)

(fact
  "All combinations of combinations-of-coins-for-cents-v1
   sum up to 100 cents"
  (same-value-in-cents?
    100
    (cc/combinations-of-coins-for-cents-v1-optimized))
  => true)

(fact
  "All combinations of combinations-of-coins-for-cents-v2
   sum up to the number received from argument"
  (same-value-in-cents?
    19
    (cc/combinations-of-coins-for-cents-v2-optimized 19))
  => true)

(fact
  "Total number of combinations of
   combinations-of-coins-for-cents-v2 for 19 cents is 6"
  (count (cc/combinations-of-coins-for-cents-v2-optimized 19))
  => 6)

(fact
  "The number of combinations of combinations-of-coins-for-cents-v3
   is not 0"
  (count
    (cc/combinations-of-coins-for-cents-v3-optimized 43 {:dime 2}))
  => 5)

(fact
  "All combinations of combinations-of-coins-for-cents-v3
   sum up to the number received from argument"
  (same-value-in-cents?
    43
    (cc/combinations-of-coins-for-cents-v3-optimized 43 {:dime 2 :nickel 3}))
  => true)

(fact
  "All combinations of combinations-of-coins-for-cents-v3
   satisfies specified number of coins"
  (every?
    (fn [{:keys [$ half-$ quarter dime nickel cent]}]
      (and (= 2 dime)
           (= 3 nickel)))
    (cc/combinations-of-coins-for-cents-v3-optimized 43 {:dime 2 :nickel 3}))
  => true)

(facts
  "Optimized and non-optimized versions work the same way"
  (fact
    "v1"
    (cc/combinations-of-coins-for-cents-v1-optimized)
    => (cc/combinations-of-coins-for-cents-v1))

  (fact
    "v3"
    (cc/combinations-of-coins-for-cents-v3-optimized 50 {:nickel 2})
    => (cc/combinations-of-coins-for-cents-v3 50 {:nickel 2})))
