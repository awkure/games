(ns games.core
  (:require [games.swepper.core 
              :refer  [start] 
              :rename {start cljswepper-start}]
            [games.guessing.core 
              :refer  [start] 
              :rename {start guessing-start}]
            [games.pegs.core 
              :refer  [start]
              :rename {start pegs-start}]
            [games.utils.utils
              :refer :all])
  (:gen-class))

(declare -main 
         prompt-option 
         save-and-exit 
         current-high-score
         make-user-name)

(def ^:dynamic *user-name*)
(def ^:dynamic *high-score*)

(def options
  [{ :name "Play No Match Dealer game" :execute guessing-start     }
   { :name "Play Swepper game"         :execute cljswepper-start   }
   { :name "Play Pegs game"            :execute pegs-start         }
   { :name "View current high score"   :execute current-high-score }
   { :name "Change user name"          :execure make-user-name     }
   { :name "Quit"                      :execute save-and-exit      }])

(defn greet 
  []
  (println "\nHello and welcome to my wonderful cli-games collection!" )
  (println "The project is currently work-in-progress and everything"  )
  (println "might not work at all. The purpose of this project was to" )
  (println "develop my skills writing clojure in parallel with book."  )
  (println "I'll probably continue working on it under the condition"  )
  (println "of proper motivation which isn't likely. Caveat usor!\n"   ))

(defn prompt-option
  []
  (println "Your choice: ")
  (let [input ((comp dec read-string get-input) nil)]
    (if (or (not input) 
            (> input (count options))
            (< input 0))
      (do (println "\n[!] Incorrect option entered\n") 
          (-main))
      (when (not ((:execute (nth options input)))
        (-main))))))

(defn print-options
  []
  (doseq [[n row] (map-indexed list options)]
    (println (inc n) "-" (:name row))))

(defn current-high-score
  []
  (println "The current high-score is " *high-score*))

(defn make-user-name 
  []
  (let [input (get-input "unknown")]
    (set! *user-name* input)))

(defn main-menu
  []
  (println "--==[ Main menu ]==--")
  ; (make-user-name)
  (print-options)
  ; (println "user: " *user-name* "high-score:" *high-score*)
  (prompt-option))

(defn -main
  [& args]
  (greet)
  (main-menu))

;; TODO 
(defn save-and-exit
  {:static true}
  []
  (System/exit 0))