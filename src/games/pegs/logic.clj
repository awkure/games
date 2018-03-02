(ns games.pegs.logic
  (:gen-class))

(defn- tri*
  ([] (tri* 0 1))
  ([sum n]
     (let [new-sum (+ sum n)]
      (->> (inc n) (tri* new-sum) lazy-seq (cons new-sum)))))

(def tri (tri*))

(defn- triangular? 
  [n]
  (= n (last (take-while #(>= n %) tri))))

(defn row-tri
  [n]
  ((comp last take) n tri))

(defn- row-num 
  [pos]
  (inc (count (take-while #(> pos %) tri))))

(defn connect 
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
              board
              [[pos destination] [destination pos]])
    board))

(defn- connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? neighbor) (triangular? pos))
      (connect board max-pos pos neighbor destination) 
      board)))

(defn- connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ row pos)
        destination (+ 1 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn- connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ 1 row pos)
        destination (+ 2 row neighbor)]
    (connect board max-pos pos neighbor destination)))

(defn- add-pos
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce #(%2 %1 max-pos pos) 
            pegged-board
            [connect-right connect-down-left connect-down-right])))

(defn new-board 
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos] (add-pos board max-pos pos))
            initial-board 
            (range 1 (inc max-pos)))))

;; Would need
(defn- pegged?
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg 
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn- place-peg 
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn- move-peg 
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

(defn- valid-moves
  [board pos]
  (into {} (filter (fn [[destination jumped]]
                     (and (not (pegged? board destination))
                          (pegged? board jumped)))
                   (get-in board [pos :connections]))))

(defn- valid-move?
  [board p1 p2]
  (get (valid-moves board p1) p2))

(defn make-move
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))

(defn can-move? 
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged) board))))