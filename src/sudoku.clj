(ns sudoku
  (:require
    [clojure.core.logic :refer [all run everyg lvar == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))

(comment
  "Example from official wiki(https://github.com/clojure/core.logic/wiki/Examples#sudoku)"

  all
  "Goals occuring within form a logical conjunction.
   (but does not create logic variables)"

  lvar
  "When called without argument,
   returns an automatically named lvar
   eg. 'lvar:5666'"

  run
  "Executes goals until a maximum of n results are found."

  everyg
  "A pseudo-relation that takes a coll and ensures that the goal g
   succeeds on every element of the collection.")

(defn get-square [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]
    (get-in rows [x y])))

(defn init
  "Constrains numbers in the position where a hint is given"
  [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (all
        (if (zero? hint)
          succeed
          (== (first vars) hint))
        (init (rest vars) (rest hints))))
    succeed))

(defn solve-sudoku
  ([hints]
   (solve-sudoku 1 hints))
  ([n hints]
   (let [vars (repeatedly 81 lvar)
         rows (->> vars (partition 9) (map vec) (into []))
         cols (apply map vector rows)
         sqs (for [x (range 0 9 3)
                   y (range 0 9 3)]
               (get-square rows x y))]
     (run n [q]
          (== q vars)
          (everyg #(fd/in % (fd/interval 1 9)) vars)
          (init vars hints)
          (everyg fd/distinct rows)
          (everyg fd/distinct cols)
          (everyg fd/distinct sqs)))))

(def hints
  [2 0 7 0 1 0 5 0 8
   0 0 0 6 7 8 0 0 0
   8 0 0 0 0 0 0 0 6
   0 7 0 9 0 6 0 5 0
   4 9 0 0 0 0 0 1 3
   0 3 0 4 0 1 0 2 0
   5 0 0 0 0 0 0 0 1
   0 0 0 2 9 4 0 0 0
   3 0 6 0 8 0 4 0 9])

(comment
  (solve-sudoku hints)
  =>
  [[2 6 7 3 1 9 5 4 8
    9 5 4 6 7 8 1 3 2
    8 1 3 5 4 2 7 9 6
    1 7 2 9 3 6 8 5 4
    4 9 5 8 2 7 6 1 3
    6 3 8 4 5 1 9 2 7
    5 4 9 7 6 3 2 8 1
    7 8 1 2 9 4 3 6 5
    3 2 6 1 8 5 4 7 9]])

(def hints-with-all-0-first-row
  [0 0 0 0 0 0 0 0 0
   0 0 0 6 7 8 0 0 0
   8 0 0 0 0 0 0 0 6
   0 7 0 9 0 6 0 5 0
   4 9 0 0 0 0 0 1 3
   0 3 0 4 0 1 0 2 0
   5 0 0 0 0 0 0 0 1
   0 0 0 2 9 4 0 0 0
   3 0 6 0 8 0 4 0 9])

(def hints-with-all-0
  "Will take too long find solutions for it"
  [0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0
   0 0 0 0 0 0 0 0 0])

