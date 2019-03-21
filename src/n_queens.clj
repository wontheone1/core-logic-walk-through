(ns n-queens
  "Example from
  https://github.com/clojure/core.logic/blob/10ee95eb2bed70af5bc29ea3bd78b380f054a8b4/src/main/clojure/clojure/core/logic/bench.clj#L178-L20="
  (:require
    [clojure.core.logic :refer [project all run-nc* defne
                                run* everyg lvar == != membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))

(comment
  defne
  "Define a goal fn. Supports pattern matching. All
   patterns will be tried. See conde."

  project
  "Extract the values bound to
   the specified logic vars.
   Non-relational.")

(defne safe [q others]
       ([_ ()])
       ([[x y] [[x1 y1] . r]]
         (!= y y1)
         (project [y y1 x x1]
                  (!= (- y1 y) (- x1 x))
                  (!= (- y1 y) (- x x1)))
         (safe [x y] r)))

(defne nqueens [l]
       ([()])
       ([[[x y] . others]]
         (nqueens others)
         (membero y [1 2 3 4 5 6 7 8])
         (safe [x y] others)))


(defn solve-nqueens []
  (run* [q]
        (fresh [y1 y2 y3 y4 y5 y6 y7 y8]
               (== q [[1 y1] [2 y2] [3 y3] [4 y4] [5 y5] [6 y6] [7 y7] [8 y8]])
               (nqueens q))))

(defn board-from-solution [solution]
  (reduce
    (fn [board [x y]]
      (assoc-in board [(dec x) (dec y)] "1"))
    (let [n (count solution)]
      (vec (repeatedly
             n #(vec (repeatedly n (fn [] "0"))))))
    solution))

(defn print-nqueens-solution [solution]
  (doseq [row (board-from-solution solution)]
    (println (clojure.string/join " " row))))
