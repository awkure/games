(ns games.utils.utils 
  (use [games.utils.macros])
  (:gen-class))

(def alpha-start 97)
(def alpha-end 123)
(def letters 
  (map (comp str char) 
       (range alpha-start alpha-end)))

(def ansi-styles 
  {:red   "[31m"
   :green "[32m"
   :yellow "[33m"
   :blue  "[34m"
   :reset "[0m"})

(defn ansi
  [style]
  (str \u001b (style ansi-styles)))

(defn colorize
  [text color]
  (str (ansi color) text (ansi :reset)))

(defn random-string 
  [len]
  ; (apply str (take len (repeatedly #(rand-nth letters)))))
  (->> #(rand-nth letters) repeatedly (take len) (apply str)))

(defn all-vars-ns 
  [namespace]
  (filter (fn [[_ value]] 
              (and (instance? clojure.lang.Var value)
                   (= namespace (.getName (.ns value)))))
          (ns-map namespace)))

(defn get-input 
  ([] (get-input nil))
  ([defaults]
    (let [input (clojure.string/trim (read-line))]
      (if (empty? input)
        defaults 
        (clojure.string/lower-case input)))))
