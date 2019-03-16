(ns logic-programming-primer
  (:require
    [clojure.core.logic :refer [run* == != membero fresh conde succeed fail conso resto]]))

(comment
  "The example usages")

(comment
  (run* [logic-variable]
        &logic-expressions))

(run* [q]
      (== q true))

(run* [q1 q2]
      (== q1 q2))

(run* [q1 q2]
      (!= q1 q2))

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
  "Core.logic is Declarative
   (order of constraints doesn’t matter
    for return value)")

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
  "conde, logical disjunction (OR)"
  "(conde &clauses)"

  (run* [q]
        (conde
          [goal1 goal2 ...]
          [goal3 goal4 ...]
          ...))

  "conde succeeds for each clause
   that succeeds, independently.")

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

  "It is a function that succeeds only if
  s is a list with head x and rest r.")

(run* [q]
      (conso 1 [2 3] q))

(run* [q]
      (conso 1 q [1 2 3]))

(run* [q]
      (conso 1 [2 q] [1 2 3]))

(comment
  "resto"

  (resto l r)

  "constrains whatever logic variables are
   present such that r is (rest l) ")


(run* [q]
      (resto [1 2 3 4] q))

(run* [q]
      (resto [q 2 3 4] [2 3 4]))

(comment
  "We've already had a sneak peak at membero"

  (membero x l))
