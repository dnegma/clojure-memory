(ns test-app.core
  (:gen-class)
  [:require clojure.string])

(defn new-char-value [val]
  (char (inc (int val))))

(defn assoc-cards [board keys val]
  (apply assoc board (interleave keys (repeat val))))

(defn generate-board [free-places memory-no]
  (loop [places free-places
         board {}
         val \a]
    (if (empty? places)
      board
      (recur (drop memory-no places)
             (assoc-cards board (take memory-no places) val)
             (new-char-value val)))))

(defn print-board [board dim-x]
  (println (str \newline (clojure.string/join \newline (map #(clojure.string/join \space %) (partition dim-x (vals (into (sorted-map) board))))))))

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
        real-board (generate-board (shuffle (range (* dim-y dim-x))) 2)]
    (loop [pair #{}
           board (zipmap (keys real-board) (repeat "*"))]
      (print-board board dim-x)

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