(ns sudoku-test
  (:require [midje.sweet :refer :all]
            [sudoku :as sudoku]))

(def hints-from-wikipedia
  [5 3 0 0 7 0 0 0 0
   6 0 0 1 9 5 0 0 0
   0 9 8 0 0 0 0 6 0
   8 0 0 0 6 0 0 0 3
   4 0 0 8 0 3 0 0 1
   7 0 0 0 2 0 0 0 6
   0 6 0 0 0 0 2 8 0
   0 0 0 4 1 9 0 0 5
   0 0 0 0 8 0 0 7 9])

(fact
  "For Wikipedia example solver function
   gives the same solution"
  (sudoku/solve-sudoku
    hints-from-wikipedia)
  => [[5 3 4 6 7 8 9 1 2
       6 7 2 1 9 5 3 4 8
       1 9 8 3 4 2 5 6 7
       8 5 9 7 6 1 4 2 3
       4 2 6 8 5 3 7 9 1
       7 1 3 9 2 4 8 5 6
       9 6 1 5 3 7 2 8 4
       2 8 7 4 1 9 6 3 5
       3 4 5 2 8 6 1 7 9]])

(fact
  "Wikipedia example has only one solution"
  (count
    (sudoku/solve-sudoku
      10
      hints-from-wikipedia))
  => 1)

(fact
  "Second example"
  (sudoku/solve-sudoku
    5 [2 0 7 0 1 0 5 0 8
       0 0 0 6 7 8 0 0 0
       8 0 0 0 0 0 0 0 6
       0 7 0 9 0 6 0 5 0
       4 9 0 0 0 0 0 1 3
       0 3 0 4 0 1 0 2 0
       5 0 0 0 0 0 0 0 1
       0 0 0 2 9 4 0 0 0
       3 0 6 0 8 0 4 0 9])
  => [[2 6 7 3 1 9 5 4 8
       9 5 4 6 7 8 1 3 2
       8 1 3 5 4 2 7 9 6
       1 7 2 9 3 6 8 5 4
       4 9 5 8 2 7 6 1 3
       6 3 8 4 5 1 9 2 7
       5 4 9 7 6 3 2 8 1
       7 8 1 2 9 4 3 6 5
       3 2 6 1 8 5 4 7 9]])

(fact
  "With all 0 on the first row
   there are more than 1 solutions"
  (count
    (sudoku/solve-sudoku
      100 [0 0 0 0 0 0 0 0 0
           0 0 0 6 7 8 0 0 0
           8 0 0 0 0 0 0 0 6
           0 7 0 9 0 6 0 5 0
           4 9 0 0 0 0 0 1 3
           0 3 0 4 0 1 0 2 0
           5 0 0 0 0 0 0 0 1
           0 0 0 2 9 4 0 0 0
           3 0 6 0 8 0 4 0 9]))
  => 10)
