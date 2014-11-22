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

(defn print-board [board dim-x]
  (println (clojure.string/join \newline (map #(clojure.string/join \space %) (partition dim-x (vals (into (sorted-map) board)))))))

(defn hidden-board [board]
  (zipmap (keys board) (repeat (count board) "*")))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (str "Hello, "))

  (let [dim-x 2
        dim-y 2
        real-board (generate-board dim-x dim-y)
        hidden-board (hidden-board real-board)]
    (print-board hidden-board dim-x)
    (loop [total 1
           old-card -1
           card (Integer/parseInt (read-line))
           board hidden-board]
      (when-not (>= total (* dim-x dim-y))
        (print-board (assoc board card (real-board card)) dim-x)
        (if (>= old-card 0)
          (if (not (= (real-board old-card) (real-board card)))
            (do
              (recur (- total 1) -1 (Integer/parseInt (read-line)) (assoc board old-card "*" card "*")))
            (do
              (recur (inc total) -1 (Integer/parseInt (read-line)) (assoc board card (real-board card)))))
          (recur (inc total) card (Integer/parseInt (read-line)) (assoc board card (real-board card))))
        )
      )
   )
 )