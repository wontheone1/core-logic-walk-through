(ns verbal-arithmetic
  (:require
    [clojure.core.logic :refer [all emptyo run* everyg lvar == membero fresh conde succeed fail conso resto]]
    [clojure.core.logic.fd :as fd]))

(comment
  " SEND
  + MORE
  ______
   MONEY")

(defn send-more-money-solutions []
  (run* [s e n d m o r y]
        (fd/in s e n d m o r y (fd/interval 0 9))
        (fd/!= s 0)
        (fd/!= m 0)
        (fd/distinct [s e n d m o r y])
        (fd/eq (= (+ (* 1000 s) (* 100 e) (* 10 n) d
                     (* 1000 m) (* 100 o) (* 10 r) e)
                  (+ (* 10000 m) (* 1000 o) (* 100 n) (* 10 e) y)))))

(defn pprint-sendmory [[s e n d m o r y]]
  (println (str "  " s e n d))
  (println (str "+ " m o r e))
  (println (str "______"))
  (println (str " " m o n e y)))

; (= 10652 (+ 9567 1085))

(comment
  "General verbal arithmetic solution.")

(defn sumo [vars sum]
  (fresh [vhead vtail run-sum]
         (conde
           [(== vars ()) (== sum 0)]
           [(conso vhead vtail vars)
            (fd/+ vhead run-sum sum)
            (sumo vtail run-sum)])))

(defn product-sumo [vars multipliers sum]
  (fresh [vhead vtail mhead mtail product run-sum]
         (conde
           [(emptyo vars) (== sum 0)]
           [(conso vhead vtail vars)
            (conso mhead mtail multipliers)
            (fd/* vhead mhead product)
            (fd/+ product run-sum sum)
            (product-sumo vtail mtail run-sum)])))

(defn magnitudes [n]
  (reverse (take n (iterate #(* 10 %) 1))))

(defn cryptarithmetic [& words]
  (let [distinct-chars (distinct (apply concat words))
        char->lvar (zipmap distinct-chars (repeatedly (count distinct-chars) lvar))
        char-lvars (vals char->lvar)
        first-letter-lvars (distinct (map #(char->lvar (first %)) words))
        sum-lvars (repeatedly (count words) lvar)
        word-lvars (map #(map char->lvar %) words)]
    (run* [q]
          (everyg #(fd/in % (fd/interval 0 9)) char-lvars)       ;; digits 0-9
          (everyg #(fd/!= % 0) first-letter-lvars)          ;; no leading zeroes
          (fd/distinct char-lvars)                               ;; only distinct digits
          (everyg (fn [[sum word-lvar]]                             ;; calculate sums for each word
                    (product-sumo word-lvar (magnitudes (count word-lvar)) sum))
                  (map vector sum-lvars word-lvars))
          (fresh [sums-but-last]
                 (sumo (butlast sum-lvars) sums-but-last)               ;; sum all input word sums
                 (fd/== sums-but-last (last sum-lvars)))                ;; input word sums must equal last word sum
          (== q char->lvar))))

(defn pprint-answer [char->digit words]
  (let [nums (map #(apply str (map char->digit %))
                  words)
        width (apply max (map count nums))
        width-format (str "%" width "s")
        pad #(format width-format %)
        line (apply str (repeat (+ 2 width) \-))]
    (println
      (clojure.string/join \newline
                           (concat
                             (map #(str "+ " (pad %)) (butlast words))
                             [line
                              (str "= " (pad (last words)))]
                             [""]
                             (map #(str "+ " (pad %)) (butlast nums))
                             [line
                              (str "= " (pad (last nums)))])))))

(defn pprint-all-answers [char->digit-seq words]
  (let [width (apply max (map count words))
        width-format (str "%" width "s")
        pad #(format width-format %)
        line (apply str (repeat (+ 2 width) \-))]
    (println
      (clojure.string/join
        \newline
        (concat
          (map #(str "+ " (pad %)) (butlast words))
          [line
           (str "= " (pad (last words)))]))
      \newline)
    (doseq [char->digit char->digit-seq
            :let [nums (map #(apply str (map char->digit %))
                            words)]]
      (println
        (clojure.string/join
          \newline
          (concat
            (map #(str "+ " (pad %)) (butlast nums))
            [line
             (str "= " (pad (last nums)))]))
        \newline))))

#_(cryptarithmetic "cp" "is" "fun" "true")

#_(doseq [char->digit (cryptarithmetic "cp" "is" "fun" "true")]
    (pprint-answer char->digit ["cp" "is" "fun" "true"]))

#_(cryptarithmetic "SO" "MANY" "MORE" "MEN" "SEEM" "TO"
                   "SAY" "THAT" "THEY" "MAY" "SOON" "TRY"
                   "TO" "STAY" "AT" "HOME" "SO" "AS" "TO"
                   "SEE" "OR" "HEAR" "THE" "SAME" "ONE"
                   "MAN" "TRY" "TO" "MEET" "THE" "TEAM"
                   "ON" "THE" "MOON" "AS" "HE" "HAS"
                   "AT" "THE" "OTHER" "TEN" "TESTS")

#_(pprint-answer
    {\A 7, \E 0, \H 5, \M 2,
     \N 6, \O 1, \R 8, \S 3,
     \T 9, \Y 4}
    ["SO" "MANY" "MORE" "MEN" "SEEM" "TO"
     "SAY" "THAT" "THEY" "MAY" "SOON" "TRY"
     "TO" "STAY" "AT" "HOME" "SO" "AS" "TO"
     "SEE" "OR" "HEAR" "THE" "SAME" "ONE"
     "MAN" "TRY" "TO" "MEET" "THE" "TEAM"
     "ON" "THE" "MOON" "AS" "HE" "HAS"
     "AT" "THE" "OTHER" "TEN" "TESTS"])

#_(cryptarithmetic "wrong" "wrong" "right")
