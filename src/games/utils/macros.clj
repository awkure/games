(ns games.utils.macros
  (:gen-class))

(defn ppmap
 [grain-size f & colls]
 (apply concat 
   (apply pmap 
          (fn [& pgroups] (doall (apply map f pgroups)))
          (map (partial partition-all grain-size) colls))))

(defn- split-eq
  [n coll]
  (loop [n n parts [] coll coll c (count coll)]
    (if (<= n 0)
      parts
      (let [t (quot (+ c n -1) n)]
        (recur (dec n) 
               (conj parts (take t coll))
               (drop t coll) 
               (- c t))))))

(defmacro dopar 
  [thread-count [sym coll] & body]
  `(doall (ppmap (fn [vals#]
                     (doseq [~sym vals#]
                      ~@body))
      (split-eq ~thread-count ~coll))))