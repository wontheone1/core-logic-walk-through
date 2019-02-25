(ns primer
  (:require
    [clojure.core.logic :refer [run* == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))

(run* [q]
      (== q true))

(run* [q]
      (conde
        [succeed]))

(run* [q]
      (conde
        [succeed succeed fail succeed]))

(run* [q]
      (conso 1 [2 3] q))

(run* [q]
      (resto [1 2 3 4] q))

(run* [q]
      (resto [q 2 3 4] [2 3 4]))

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
               (= num-cent (- 100 sum-without-cents)))))

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

(defn combinations-of-coins-for-cents [value-in-cents]
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
                 (= num-cent (- value-in-cents sum-without-cents))))))

; Applying constraints on number of specific types of coins

(defn combinations-of-coins-for-cents [value-in-cents {:keys [$s half-$s quarters dimes nickels cents]}]
  (run* [num-$ num-half-$ num-quarter num-dime num-nickel num-cent]
        (fresh [sum-without-cents]
               (fd/in num-$ (fd/interval 0 (int (/ value-in-cents 100))))
               (fd/in num-half-$ (fd/interval 0 (int (/ value-in-cents 50))))
               (fd/in num-quarter (fd/interval 0 (int (/ value-in-cents 25))))
               (fd/in num-dime (fd/interval 0 (int (/ value-in-cents 10))))
               (fd/in num-nickel (fd/interval 0 (int (/ value-in-cents 5))))
               (fd/in num-cent (fd/interval 0 value-in-cents))
               (fd/in sum-without-cents (fd/interval 0 value-in-cents))
               (if $s (fd/== $s num-$) succeed)
               (if half-$s (fd/== half-$s num-$) succeed)
               (if quarters (fd/== quarters num-half-$) succeed)
               (if dimes (fd/== dimes num-dime) succeed)
               (if nickels (fd/== nickels num-nickel) succeed)
               (if cents (fd/== cents num-cent) succeed)
               (fd/eq
                 (= sum-without-cents (+ (* 100 num-$)
                                         (* 50 num-half-$)
                                         (* 25 num-quarter)
                                         (* 10 num-dime)
                                         (* 5 num-nickel)))
                 (>= value-in-cents sum-without-cents)
                 (= num-cent (- value-in-cents sum-without-cents))))))
