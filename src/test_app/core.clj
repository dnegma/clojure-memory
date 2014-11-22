(ns test-app.core
  (:gen-class)
  [:require clojure.string])

(defn random-value [v]
  (nth v (rand-int (count v))))

(defn remove-value [v val]
  (remove (fn [item] (= val item)) v)
  )

(defn generate-board [dim-x dim-y]
  (loop [val \a
         occupied (range (* dim-x dim-y))
         board {}]
    (if (= (count occupied) 0)
      board
      (let [one (random-value occupied)
            two (random-value (remove-value occupied one))
            new-occupied (remove-value (remove-value occupied one) two)]
        (recur (char (inc (int val)))
               new-occupied
               (assoc board one val two val)))))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (str "Hello, "))

  (let [dim-x 4
        dim-y 4
        board (generate-board dim-x dim-y)]

    (println (clojure.string/join \newline (map #(clojure.string/join \space %) (partition dim-x (vals (into (sorted-map) board)))))))
)