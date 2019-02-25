(ns primer
  (:require
    [clojure.core.logic :refer [run* == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))

(comment
  "Motivating example problem"

  "Let’s write a program that returns all possible combinations of coins that total to 1 $
   Assume available coins in $ currency are
   - Cent (0.01)
   - Nickel (0.05)
   - Dime (0.10)
   - Quarter (0.25)
   - Half Dollar (0.5)
   - Dollar (1.0)\n")

(comment
  "The example usages")

(comment
  (run* [logic-variable]
        &logic-expressions))

(run* [q]
      (== q true))

(run* [q1 q2]
      (== q1 q2))

(run* [q]
      (== q 1)
      (== q 2))

(comment
  "Unification of a single lvar with a literal")

(run* [q]
      (== q 1))

(run* [q]
      (== q {:a 1 :b 2}))

(run* [q]
      (== {:a q :b 2} {:a 1 :b 2}))

(run* [q]
      (== q '(1 2 3)))

(comment
  "Membero goal")

(run* [q]
      (membero q [1 2 3]))

(run* [q]
      (membero q [1 2 3])
      (membero q [3 4 5]))

(comment
  "Unification of two lvars")

(run* [q]
      (fresh [a]
             (membero a [1 2 3])
             (membero q [3 4 5])
             (== a q)))

(comment
  "Core.logic is Declarative (order of constraints doesn’t matter for return value)")

(run* [q]
      (fresh [a]
             (membero a [3 4 5])
             (== q a)
             (membero q [1 2 3])))

(run* [q]
      (fresh [a]
             (== q a)
             (membero a [3 4 5])
             (membero q [1 2 3])))

(run* [q]
      (fresh [a]
             (== q a)
             (membero a [3 4 5])
             (membero q [1 2 3])))

(comment
  "The final operator, conde (the other two operators were 'fresh' and 'unify'(==)"

  "logical disjunction (OR)"
  "(conde &clauses)"

  (run* [q]
        (conde
          [goal1 goal2 ...]
          ...))

  "conde succeeds for each clause that succeeds, independently.")

(run* [q]
      (conde
        [succeed succeed succeed succeed]))

(run* [q]
      (conde
        [succeed succeed fail succeed]))

(run* [q]
      (conde
        [succeed]
        [succeed]))

(run* [q]
      (conde
        [succeed]
        [fail]))

(run* [q]
      (conde
        [(== q 1)]
        [(== q 2)]))



(comment
  "Goals..."

  "Conso"

  (conso x r s)

  "It is a function that succeeds only if s is a list with head x and rest r.")

(run* [q]
      (conso 1 [2 3] q))

(run* [q]
      (conso 1 q [1 2 3]))

(run* [q]
      (conso 1 [2 q] [1 2 3]))

(comment
  "resto"

  (resto l r)

  "constrains whatever logic variables are present such that r is (rest l) ")


(run* [q]
      (resto [1 2 3 4] q))

(run* [q]
      (resto [q 2 3 4] [2 3 4]))

(comment
  "We've already had a sneak peak at membero:"

  (membero x l))

(comment
  "CLP(FD)"

  "`core.logic` now has useful operators for constraint logic programming over finite domains,
   that is positive integers.

   In order to use these finite domain operators on logic vars,
   domains must be declared with `fd/in.`")

(run* [q]
      (fd/in q (fd/interval 1 5)))

(run* [x y]
      (fd/in x y (fd/interval 1 10))
      (fd/+ x y 10))

(run* [q]
      (fresh [x y]
             (fd/in x y (fd/interval 1 10))
             (fd/+ x y 10)
             (fd/distinct [x y])
             (== q [x y])))
; Note that [5 5] is no longer in the set of returned solutions.

(comment
  "`fd/eq`"

  "fd/eq is a macro that allows you to write arithmetic expressions "
  "in normal Lisp syntax which will be expanded into the appropriate series of CLP(FD) operators. ")

(run* [x y]
      (fd/in x y (fd/interval 0 9))
      (fd/eq
        (= (+ x y) 9)
        (= (+ (* x 2) (* y 4)) 24)))

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
  "Value in cents as a parameter")

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
  "Apply constraints on number of specific types of coins and the value in cents")

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
                  (fd/== half-$ num-$)
                  succeed)
                (if quarter
                  (fd/== quarter num-half-$)
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
