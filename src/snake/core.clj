(ns snake.core)

(defn add-points [x y]
  [(+ (x 0) (y 0))
   (+ (x 1) (y 1))])

(def directions {:left  [-1,  0]
                 :right [ 1,  0]
                 :up    [ 0, -1]
                 :down  [ 0,  1]})
(def w 50)
(def h 50)
(def win-score 200)
(def score (atom 0))        
(def period (atom 200))

(defn create-snake []
  {:body (list [25 25] [26 25] [27 25])
   :dir [0,1]})

(defn create-apple []
  {:location [(rand-nth (range 5 (- w 5))), (rand-nth (range 5 (- h 5)))]})

(defn move [snake & grow]
  (let [body (snake :body)
        dir (snake :dir)]
    (assoc snake :body (cons (add-points (first body) dir)
                             (if grow body (butlast body))))))

(defn win? []
  (>= @score win-score))

(defn eat-her-tail? [snake]
  (some #(= (first (snake :body)) %) (rest (snake :body))))

(defn hit-wall? [snake]
  (let [head (first (snake :body))]
    (or (>= (head 0) w)
        (>= (head 1) h)
        (<= (head 0) 0)
        (<= (head 1) 0))))

(defn lose? [snake]
  (or (hit-wall? snake) (eat-her-tail? snake)))

(defn eats? [snake apple]
  (let [snake-head (first (snake :body))
        apple (apple :location)]
    (= snake-head apple)))

(defn turn [snake new-dir]
  (assoc snake :dir new-dir))

(defn update-dirs [snake new-dir]
  (when (not= (add-points new-dir (snake :dir)) [0 0]) 
    (dosync
      (alter snake turn new-dir))))

(defn update-pos [snake apple]
  (dosync
    (if (eats? @snake @apple)
      (do 
        (ref-set apple (create-apple))
        (swap! score + 10)
        (swap! period - 10)
        (alter snake move :grow))
      (alter snake move)))
  nil)

(defn reset-game [snake apple]
  (dosync 
    (ref-set apple (create-apple))
    (ref-set snake (create-snake)))
   nil)

(defn running? [snake]
  (not (or (lose? @snake) (win?))))

(defn game [snake apple]
  (while (running? snake)
    (update-pos snake apple)
    (Thread/sleep @period)))

             
