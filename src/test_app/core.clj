(ns test-app.core
  (:gen-class)
  [:require clojure.string])

(defn random-value [v]
  (nth v (rand-int (count v))))

(defn remove-value [v val]
  (remove (fn [item] (= val item)) v))

(defn new-char-value [val]
  (char (inc (int val))))

(defn assoc-cards [board keys val]
  (apply assoc board (interleave keys (repeat val))))

(defn generate-places [placement-fn free-places]
  (reverse (take (count free-places) (iterate #(remove-value % (placement-fn %)) free-places))))

(defn generate-board [placement-fn dim-x dim-y memory-no]
  (loop [free-places (range (* dim-x dim-y))
         board {}
         val \a]
    (if (empty? free-places)
      board
      (let [places (nth (generate-places placement-fn free-places) (dec memory-no))]
        (recur (reduce remove-value free-places places)
               (assoc-cards board places val)
               (new-char-value val))))))

(defn print-board [board dim-x]
  (println (str \newline (clojure.string/join \newline (map #(clojure.string/join \space %) (partition dim-x (vals (into (sorted-map) board))))))))

(defn hidden-board [board]
  (zipmap (keys board) (repeat "*")))

(defn reveal-card [board card real-board]
  (assoc board card (real-board card)))

(defn revealed? [board card]
  (not (= (board card) "*")))

(defn read-int []
  (try
    (Integer/parseInt (read-line))
    (catch Exception e
      -1)))

(defn -main
  [& args]

  (let [dim-x 2
        dim-y 2
        real-board (generate-board random-value dim-x dim-y 2)]
    (loop [pair #{}
           board (zipmap (keys real-board) (repeat "*"))]
      (print-board board dim-x)

      (println (str "Pair: " pair))
      (if-not (= board real-board)
        (let [card (read-int)
              opened-card-board (reveal-card board card real-board)]

          (if-not (revealed? board card)
            (if (second pair)
              (if (not (apply = (map real-board pair)))
                (do
                  (recur #{card} (assoc-cards opened-card-board pair "*")))
                (do
                  (recur #{card} opened-card-board)))
              (recur (conj pair card) opened-card-board))
            (recur pair board)))
        )
      )
    )
  )