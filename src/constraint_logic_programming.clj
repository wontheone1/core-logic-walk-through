(ns constraint-logic-programming
  (:require
    [clojure.core.logic :refer [run* == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))


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
