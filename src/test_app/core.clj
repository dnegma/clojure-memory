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
  (println (str \newline (clojure.string/join \newline (map #(clojure.string/join \space %) (partition dim-x (vals (into (sorted-map) board))))))))

(defn hidden-board [board]
  (zipmap (keys board) (repeat (count board) "*")))

(defn reveal-card [board card real-board]
  (assoc board card (real-board card)))

(defn hide-cards [board keys]
  (println keys)
  (apply assoc board (interleave keys (repeat "*"))))

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
        real-board (generate-board random-value dim-x dim-y)
        hidden-board (hidden-board real-board)]
    (loop [pair #{}
           board hidden-board]
      (print-board board dim-x)

      (println (str "Pair: " pair))
      (if-not (= board real-board)
        (let [card (read-int)
              opened-card-board (reveal-card board card real-board)]

          (if-not (revealed? board card)
            (if (second pair)
              (if (not (apply = (map real-board pair)))
                (do
                  (recur #{card} (hide-cards opened-card-board pair)))
                (do
                  (recur #{card} opened-card-board)))
              (recur (conj pair card) opened-card-board))
            (recur pair board)))
        )
      )
   )
 )