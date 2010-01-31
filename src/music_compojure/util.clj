(ns music-compojure.util)

(defn- vec-contains? [vec el]
   (= 1 (count (filter #(= el %) vec))))


(defn indexed [coll] (map vector (iterate inc 0) coll))

(defn index-filter [pred coll]
  (when pred
    (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(defn index-of-any [pred coll]
  (first (index-filter pred coll)))
