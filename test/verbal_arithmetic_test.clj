(ns verbal-arithmetic-test
  (:require [midje.sweet :refer :all]
            [verbal-arithmetic :as verbal-arithmetic]))

(fact
  "For Wikipedia example solver function
   gives the same solution"
  (verbal-arithmetic/pprint-sendmory
    (first (verbal-arithmetic/send-more-money-solutions)))
  (verbal-arithmetic/send-more-money-solutions)
  => [[9 5 6 7 1 0 8 2]])

