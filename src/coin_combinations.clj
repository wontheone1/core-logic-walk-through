(ns coin-combinations
  (:require
    [clojure.core.logic :refer [all run* == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))


(comment
  "Coming back to the motivating example problem")

(defn combinations-of-coins-for-cents-v1 []
  (run* [num-$ num-half-$ num-quarter
         num-dime num-nickel num-cent]
        (fd/in num-$
               num-half-$
               num-quarter
               num-dime
               num-nickel
               num-cent
               (fd/interval 0 100))
        (fd/eq
          (= 100 (+ (* 100 num-$)
                    (* 50 num-half-$)
                    (* 25 num-quarter)
                    (* 10 num-dime)
                    (* 5 num-nickel)
                    num-cent)))))

(defn combinations-of-coins-for-cents-v1-optimized []
  (run* [num-$ num-half-$ num-quarter
         num-dime num-nickel num-cent]
        (fd/in num-$ (fd/interval 0 1))
        (fd/in num-half-$ (fd/interval 0 2))
        (fd/in num-quarter (fd/interval 0 4))
        (fd/in num-dime (fd/interval 0 10))
        (fd/in num-nickel (fd/interval 0 20))
        (fd/in num-cent (fd/interval 0 100))
        (fd/eq
          (= 100 (+ (* 100 num-$)
                    (* 50 num-half-$)
                    (* 25 num-quarter)
                    (* 10 num-dime)
                    (* 5 num-nickel)
                    num-cent)))))

(comment
  "Function that works for arbitrary value of cents?")

(defn- constrain-interval-of-num-coins
  "Constrains number of coins when specified"
  [total-value-in-cents num-coins coin-denominations]
  (if (seq num-coins)
    (let [coin-denomination (first coin-denominations)]
      (all
        (fd/in (first num-coins)
               (fd/interval
                 0 (int (/ total-value-in-cents coin-denomination))))
        (constrain-interval-of-num-coins
          total-value-in-cents
          (rest num-coins)
          (rest coin-denominations))))
    succeed))

(defn combinations-of-coins-for-cents-v2-optimized
  ([]
   (combinations-of-coins-for-cents-v2-optimized 100))
  ([value-in-cents]
   (run* [num-$ num-half-$ num-quarter
          num-dime num-nickel num-cent]
         (constrain-interval-of-num-coins
           value-in-cents
           [num-$ num-half-$ num-quarter
            num-dime num-nickel num-cent]
           [100 50 25 10 5 1])
         (fd/eq
           (= value-in-cents (+ (* 100 num-$)
                                (* 50 num-half-$)
                                (* 25 num-quarter)
                                (* 10 num-dime)
                                (* 5 num-nickel)
                                num-cent))))))

#_(combinations-of-coins-for-cents-v2-optimized 19)

(comment
  "Apply constraints on the number of specific types
   of coins and the value in cents"

  all
  "Goals occuring within form a logical conjunction.
   (but does not create logic variables)")

(defn- coin-vector->coin-map [[num-$ num-half-$ num-quarter
                               num-dime num-nickel num-cent]]
  {:$       num-$
   :half-$  num-half-$
   :quarter num-quarter
   :dime    num-dime
   :nickel  num-nickel
   :cent    num-cent})

(defn- constrain-specified-number-of-coins
  "Constrains number of coins when specified"
  [lvars constraints]
  (if (seq lvars)
    (let [constraint (first constraints)]
      (all
        (if constraint
          (== (first lvars) constraint)
          succeed)
        (constrain-specified-number-of-coins
          (rest lvars)
          (rest constraints))))
    succeed))

(defn combinations-of-coins-for-cents-v3-optimized
  ([]
   (combinations-of-coins-for-cents-v3-optimized 100 nil))
  ([value-in-cents]
   (combinations-of-coins-for-cents-v3-optimized value-in-cents nil))
  ([value-in-cents
    {:keys [$ half-$ quarter dime nickel cent]}]
   (map coin-vector->coin-map
        (run* [num-$ num-half-$ num-quarter
               num-dime num-nickel num-cent]
              (constrain-interval-of-num-coins
                value-in-cents
                [num-$ num-half-$ num-quarter
                 num-dime num-nickel num-cent]
                [100 50 25 10 5 1])
              (constrain-specified-number-of-coins
                [num-$ num-half-$ num-quarter
                 num-dime num-nickel num-cent]
                [$ half-$ quarter dime nickel cent])
              (fd/eq
                (= value-in-cents (+ (* 100 num-$)
                                     (* 50 num-half-$)
                                     (* 25 num-quarter)
                                     (* 10 num-dime)
                                     (* 5 num-nickel)
                                     num-cent)))))))

#_(combinations-of-coins-for-cents-v3-optimized
    43 {:dime 2 :nickel 3})

(defn combinations-of-coins-for-cents-v3
  ([]
   (combinations-of-coins-for-cents-v3 100 nil))
  ([value-in-cents]
   (combinations-of-coins-for-cents-v3 value-in-cents nil))
  ([value-in-cents
    {:keys [$ half-$ quarter dime nickel cent]}]
   (map coin-vector->coin-map
        (run* [num-$ num-half-$ num-quarter
               num-dime num-nickel num-cent]
              (fd/in num-$
                     num-half-$
                     num-quarter
                     num-dime
                     num-nickel
                     num-cent
                     (fd/interval 0 value-in-cents))
              (constrain-specified-number-of-coins
                [num-$ num-half-$ num-quarter
                 num-dime num-nickel num-cent]
                [$ half-$ quarter dime nickel cent])
              (fd/eq
                (= value-in-cents (+ (* 100 num-$)
                                     (* 50 num-half-$)
                                     (* 25 num-quarter)
                                     (* 10 num-dime)
                                     (* 5 num-nickel)
                                     num-cent)))))))
