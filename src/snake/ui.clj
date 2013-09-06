(ns snake.ui
  (:use snake.core)
  (:use quil.core))

(def square-image (atom nil))
(def apple-image (atom nil))
(def win-image (atom nil))
(def lose-image (atom nil))        
(def snake (ref (create-snake)))
(def apple (ref (create-apple)))

(defn setup []
  (frame-rate 200)
  (background 0)
  (swap! square-image (fn [_] (load-image "square.gif")))
  (swap! apple-image (fn [_] (load-image "apple.gif")))
  (swap! win-image (fn [_] (load-image "win.jpg")))
  (swap! lose-image (fn [_] (load-image "lose.jpg"))))

(defn draw-body-part [v]
  (image @square-image (* 10 (v 0)) (* 10 (v 1))))

(defn draw-apple [v]
  (image @apple-image (* 10 (v 0)) (* 10 (v 1))))

(defn draw []
  (background 0)
  (doall (map draw-body-part (snake :body)))
  (draw-apple (apple :location))
  (text (format "Score: %d" @score) 0 (* 11 h))
  (cond
    (win?) (image @win-image 0 0)
    (lose? @snake) (image @lose-image 0 0)))
   
(defn key-handler []
  (update-dirs snake (directions (key-as-keyword))))

(defn -main [] 
  (future (game snake apple))
  (defsketch snake-skatch
    :title "Snake"
    :key-pressed key-handler
    :setup setup
    :size [(* 10 h) (* 11 w)]
    :draw draw))
