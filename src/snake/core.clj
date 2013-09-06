(ns snake.core)

(defn add-points [x y]
  [(+ (x 0) (y 0))
   (+ (x 1) (y 1))])

(def left [-1, 0])
(def right [1, 0])
(def up [0, 1])
(def down [0, -1]) 
(def w 10)
(def h 10)
(def win-length 10)

(defn create-snake []
  {:body (list [1,1])
   :dir [0,1]})

(defn create-apple []
  {:pos [(rand-int w), (rand-int h)]})

(defn move [snake & grow]
  (let [body (snake :body)
        dir (snake :dir)]
    (assoc snake :body (cons (add-points (first body) dir)
                             (if grow body (butlast body))))))

(defn win? [snake]
  (let [body (snake :body)]
    (>= (count body) win-length)))

(defn eat-her-tail? [snake]
  (some #(= (first (snake :body)) %) (last (snake :body))))

(defn hit-wall? [snake]
  (let [head (first (snake :body))]
    (or (> (head 0) w)
        (> (head 1) h)
        (< (head 0) 0)
        (< (head 1) 0))))

(defn lose? [snake]
  (do
    (println "luir")
    (or (hit-wall? snake) (eat-her-tail? snake))))

(defn eats? [snake apple]
  (let [snake-head (first (snake :body))
        apple (apple :location)]
    (= snake-head apple)))

(defn turn [snake new-dir]
  (assoc snake :dir new-dir))

(defn update-dirs [snake new-dir]
  (when new-dir 
    (dosync
      (alter snake turn new-dir))))

(defn update-pos [snake apple]
  (dosync
    (if (eats? @snake @apple)
      (do (ref-set apple (create-apple))
          (alter snake move :grow))
      (alter snake move)))
  nil)

(defn game [snake apple]
  (while (not (lose? @snake))
    (println "kur")
    (update-pos snake apple)
    (Thread/sleep 2000)))

             
