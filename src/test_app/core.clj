(ns test-app.core
  (:gen-class)
  [:require clojure.string])

(defn random-value [v]
  (nth v (rand-int (count v))))

(defn remove-value [v val]
  (remove (fn [item] (= val item)) v))

(defn new-char-value [val]
  (char (inc (int val))))

(defn generate-board [placement-fn dim-x dim-y]
  (loop [val \a
         free-places (range (* dim-x dim-y))
         board {}]
    (if (empty? free-places)
      board
      (let [one (placement-fn free-places)
            two (placement-fn (remove-value free-places one))]
        (recur (new-char-value val)
               (remove-value (remove-value free-places one) two)
               (assoc board one val two val))))))

(defn print-board [board dim-x]
  (println (clojure.string/join \newline (map #(clojure.string/join \space %) (partition dim-x (vals (into (sorted-map) board)))))))

(defn hidden-board [board]
  (zipmap (keys board) (repeat (count board) "*")))

(defn -main
  [& args]

  (let [dim-x 2
        dim-y 2
        real-board (generate-board random-value dim-x dim-y)
        hidden-board (hidden-board real-board)]
    (print-board hidden-board dim-x)
    (loop [total 1
           old-card -1
           card (Integer/parseInt (read-line))
           board hidden-board]
      (if-not (>= total (* dim-x dim-y))
        (do
          (print-board (assoc board card (real-board card)) dim-x)
          (if (>= old-card 0)
            (if (not (= (real-board old-card) (real-board card)))
              (do
                (recur (- total 1) -1 (Integer/parseInt (read-line)) (assoc board old-card "*" card "*")))
              (do
                (recur (inc total) -1 (Integer/parseInt (read-line)) (assoc board card (real-board card)))))
            (recur (inc total) card (Integer/parseInt (read-line)) (assoc board card (real-board card)))))
        (print-board (assoc board card (real-board card)) dim-x)
        )
      )
   )
 )