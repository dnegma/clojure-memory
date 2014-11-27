(ns test-app.core-test
  (:require [clojure.test :refer :all]
            [test-app.core :refer :all]))

(deftest new-char-value-test
  (is (= (new-char-value \a) \b)))

(deftest generate-board-test
  (is (= (generate-board first 2 2) {0 \a 1 \a 2 \b 3 \b})))