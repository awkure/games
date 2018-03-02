(ns games.swepper.logic
  (:gen-class))

(def truthy? #{1})

;; TODO : Refactor here
(defn zeroes* [] (cons 0 (lazy-seq (zeroes*))))
(def zeroes (zeroes*))

(defn iterate* 
  [start] 
  (cons start (lazy-seq (iterate* (inc start)))))
(def naturals (iterate* 0))

(defn new-empty-field
  "Generate a new field"
  [rows cols & opts]
  (for [[y row] (map-indexed list (make-array Integer/TYPE rows cols))
       [x cell] (map-indexed list row)]
       {:x x :y y 
        :value cell 
        :mine false 
        :visible false}))

;; TODO : complete
(defn new-field 
  [rows cols & opts]
  (let [field (new-empty-field rows cols opts)
        random-field (repeatedly (* rows cols) #(rand-int 2))]
    field))

;; TODO : get rid of helper function
; (defn convert-field
;   [field]
;   (reduce ()))

; (defn new-empty-field 
;   [rows cols & opts]
;   ())