(ns games.pegs.core
  (require [games.utils.utils 
             :refer [alpha-start 
                     alpha-end
                     ansi-styles 
                     letters
                     colorize
                     ansi]]
           [games.pegs.logic 
             :refer [row-tri
                     can-move?
                     make-move
                     remove-peg
                     new-board]]
           [clojure.set :as set])
  (:gen-class))

(declare prompt-move game-over)

(defn- render-pos 
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "0" :blue)
         (colorize "-" :red))))

(defn- row-positions
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn- row-padding 
  [row-num rows]
  (let [pad-length (/ (* (- rows row-num) 3) 2)]
    (->> (repeat " ") (take pad-length) (apply str))))

(defn- render-row 
  [board row-num]
  (str (row-padding row-num (:rows board))
       (clojure.string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))

(defn- print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

(defn- characters-as-strings 
  [string]
  (re-seq #"[a-zA-Z]" string))

(defn- letter->pos 
  [letter]
  (inc (- (int (first letter)) alpha-start)))

(defn- get-input 
  ([] (get-input nil))
  ([default] 
    (let [input (clojure.string/trim (read-line))]
      (if (empty? input)
        default
        (clojure.string/lower-case input)))))

(defn- user-entered-invalid-move 
  [board]
  (println "\n[!] Invalid move entered\n")
  (prompt-move board))

(defn- user-entered-valid-move 
  [board]
  (if (can-move? board)
    (prompt-move board)
    (game-over  board)))

(defn- prompt-move
  [board]
  (print-board board)
  (println "Your move:")
  (let [input (map letter->pos (characters-as-strings (get-input)))]
    (if-let [new-board (make-move board (first input) (second input))]
      (user-entered-valid-move new-board)
      (user-entered-invalid-move board))))
  
(defn- prompt-empty-peg
  [board]
  (print-board board)
  (println "First peg to remove [e]:")
  (prompt-move (remove-peg board (letter->pos (get-input "e")))))

(defn start
  [] 
  (println "\n***** Pegs *****\n")
  (println "In this game you start out with a triangular")
  (println "field and the object is to remove as many")
  (println "pegs as possible by \"jumping\" over them\n")
  (println "Number of rows [5]:")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (prompt-empty-peg board)))

(defn- game-over 
  [board]
  (let [remaining-pegs (count (filter :pegged (vals board)))]
    (println "Game over. " remaining-pegs "pegs left")
    (print-board board)
    (println "Again? y/n [y]")
    (let [input (get-input "y")]
      (when (= "y" input)
        (start)))))