(ns games.guessing.core
  (:require [games.utils.utils 
              :refer :all])
  (:gen-class))

(def ^:dynamic *lose* false)

(defn gen-range 
  []
  (->> #(rand-int 100) 
       repeatedly 
       (take 16)))

(def get-secret-number (comp read-string get-input))

; (defn print-range
;   [range matching]
;   (doseq [v range]
;     (print (if (= matching v)
;       (colorize v :green) v))))

;; Forgive me father for the lack of
;; knowledge about all std functions
(defn take-from 
  [from what]
  ; (flatten 
  ;   (reduce #(apply % from) 
  ;           []
  ;           [(take-while #(not= % what)) 
  ;            (rest (drop-while #(not= % what)))])))
  (flatten 
    [(take-while #(not= % what) from) 
     (rest (drop-while #(not= % what) from))]))

(defn start 
  []
  (println "\n:::::: No Match Dealer :::::\n")
  (println "The player enters a secret number first.")
  (println "Then the dealer shall deal out 16 random numbers between 0 and 99")
  (println "If there're no matches among them, you win!\n")
  (println "Enter your secret number:")
  (set! *lose* false)
  (let [rowlimit 0
        secret-num (get-secret-number nil)
        secret-range (gen-range)
        lose false]
    (doseq [v secret-range]
      (print " "
        ;; cond-> here
        (if (= secret-num v)
          (do (colorize v :green)
              (set! *lose* true))
          (if (contains? (into [] (take-from secret-range v)) v)
            (do (colorize v :blue)
                (set! *lose* false))
            v)))
      (when (= (inc rowlimit) 8)
        (println)))
   (if *lose*
     (println "[*] You lose!\n")
     (println "[*] You win!\n"))))
  