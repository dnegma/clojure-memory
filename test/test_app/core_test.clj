(ns test-app.core-test
  (:require [clojure.test :refer :all]
            [test-app.core :refer :all]))

(deftest new-char-value-test
  (is (= (new-char-value \a) \b)))

(deftest generate-board-test
  (is (= (generate-board (range 4) 2) {0 \a 1 \a 2 \b 3 \b})))

(deftest assoc-cards-test
  (is (= (assoc-cards {0 \a 1 \a 2 \b 3 \b} [0 1 2 3] "*") {0 "*" 1 "*" 2 "*" 3 "*"}))
  (is (= (assoc-cards {0 \a 1 \a 2 \b 3 \b} [0 1 2 3] \a) {0 \a 1 \a 2 \a 3 \a})))