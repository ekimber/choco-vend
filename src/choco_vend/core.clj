(ns choco-vend.core)
(require '[shiroko.core :as s])
(require '[clojure.core.async :refer [<!!]])

(def prices {"bounty" 2 "rolo" 3})
(def stock (ref {"bounty" 1 "rolo" 11}))
(def coins (ref 0))
(def credit (ref 0))

(defn insert-coin [n]
  (alter coins + n)
  (alter credit + n))

(defn select-choc [name]
  (let [price (prices name)]
    (cond
      (nil? price) (str "Sorry I don't stock " name)
      (< (stock name) 1) (str name " is out of stock")
      (< @credit (prices name)) (str "Insufficient credit, insert " (- (prices name) @credit) " coin(s)")
      :else (do
              (ref-set stock (assoc @stock name (dec (@stock name))))
              (alter credit - (prices name))))))

(def machine (s/init-db :ref-list [stock coins credit]))
