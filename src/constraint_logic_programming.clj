(ns constraint-logic-programming
  (:require
    [clojure.core.logic :refer [run* == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))


(comment
  "CLP(FD)
   :constraint logic programming over finite domains(positive integers)

   Included operators are:
   +, -, *, quot, ==, !=, <, <=, >, >=, distinct

   In order to use these finite domain operators
   on logic vars,
   domains must be declared with `fd/in.`")

(comment
  "Motivating example problem"

  "Let’s write a program that returns all
   possible combinations of coins that total to 1 $.

   Assume available coins in $ currency are following

   - Cent (0.01)
   - Nickel (0.05)
   - Dime (0.10)
   - Quarter (0.25)
   - Half Dollar (0.5)
   - Dollar (1.0)\n")

(run* [q]
      (fd/in q (fd/interval 1 5)))

(run* [x y]
      (fd/in x y (fd/interval 1 10))
      (fd/+ x y 10))

(run* [x y]
      (fd/in x y (fd/interval 1 10))
      (fd/+ x y 10)
      (fd/distinct [x y]))
; Note that [5 5] is no longer in the set of returned solutions.

(comment
  "fd/eq

   fd/eq is a macro that allows you to
   write arithmetic expressions
   in normal Lisp syntax which
   will be expanded
   into the appropriate series of
   CLP(FD) operators. ")

(run* [x y]
      (fd/in x y (fd/interval 0 9))
      (fd/eq
        (= (+ x y) 9)
        (= (+ (* x 2) (* y 4)) 24)))
