(comment "WIP")

(ns games.swepper.core
  (:require [games.swepper.logic :refer :all] 
            [games.utils.utils   :refer :all]
            [clojure.string :as str])
  (:gen-class))

(declare prompt-move game-over)

(defn characters-as-strings
  [string]
  (str/split string #"(?=[1-9])" 2))

(defn letter->pos 
  [letter]
  (inc (- (->> letter first int) alpha-start)))

; TODO Refactor and complete
(defn print-field 
  [field]
  (let [mx (->> field last :x inc)
        my (->> field last :y inc)
        fill-cell (fn [game, {:keys [x y value]}] 
                      (assoc-in game [x y] value))
        empty-field (vec (repeat my (vec (repeat mx "#"))))
        converted-field (map vector (take mx naturals) 
                                    (reduce fill-cell empty-field field))]
    (println (->> (take mx letters) (str/join " ") (str "   ")))
    ; (println (str/join "\n" 
    ;          (map #(->> (take (:x %) zeroes)
    ;                     (str/join "")
    ;                     (str (:y %) " "))
    ;                field)))))
    (doseq [row converted-field] (apply println row))))
    
;; Edit here to generate a new field with random mines
;; Also need to check whether the given input is correct 
;; and contains valid numbers and size of dimensions
(defn start 
  []
  (println "\n##### Swepper #####\n")
  (println "This is a swepper game where all you have to do")
  (println "is diffuse the mines until none of them left")
  (println "[CURRENTLY WIP]\n")
  (println "Enter size of the field [10x10]: ")
  (let [input (as-> (get-input "10x10") string
                    (str/replace string "x" " ")
                    (str "[" string "]"))
        size (read-string input)
        x (first size)
        y (second size)]
    (if (not= x y)
      (do (println "Invalid size of the grid entered.")
          (println "Hint: Only sqare grids are acceptable right now")
          (start))
      (prompt-move (new-field x y)))))

(defn game-over 
  [field]
  (println "Game over.")
  (print-field field)
  (println "Play again? y/n [y]")
  (let [input (get-input "y")]
    (if (= "y" input)
      (start)
      (do (println "Bye")
          (System/exit 0)))))
  
(defn make-move [& args] (println "Entered a valid move"))
(defn can-move? [& args] true)

(defn user-entered-invalid-move 
  [field]
  (println "\n[!] Invalid move entered\n")
  (prompt-move field))

(defn user-entered-valid-move 
  [field]
  (if (can-move? field)
    (prompt-move field)
    (game-over   field)))

;; TODO : ended here
(defn prompt-move
  [field] 
  (print-field field)
  (println "Your move:")
  (let [replace-second #(assoc %1 1 %2)
        swap #(assoc %1 %3 (%1 %2) %2 (%1 %3))
        input* (map letter->pos (characters-as-strings (get-input)))
        ; input (replace-first input* ( letter->pos (first input*)))]
        input (replace-second input* (+ 48 (second input*)))]
    (if-let [new-field (make-move field (first input) (second input))]
      (user-entered-valid-move new-field)
      (user-entered-invalid-move field))))