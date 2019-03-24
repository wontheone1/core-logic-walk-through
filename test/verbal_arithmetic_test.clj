(ns verbal-arithmetic-test
  (:require [midje.sweet :refer :all]
            [verbal-arithmetic :as verbal-arithmetic]))

(fact
  "send more money solution is solved"
  (verbal-arithmetic/send-more-money-solutions)
  => [[9 5 6 7 1 0 8 2]])

(fact
  "The generalized version works"
  (verbal-arithmetic/cryptarithmetic
    "send" "more" "money")
  => [{\s 9, \e 5, \n 6, \d 7, \m 1, \o 0, \r 8, \y 2}])

(fact
  "21 ways to make two wrongs right"
  (count (verbal-arithmetic/cryptarithmetic "wrong" "wrong" "right"))
  => 21)
