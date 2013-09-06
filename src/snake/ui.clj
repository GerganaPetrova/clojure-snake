(ns snake.ui
  (:use snake.core)
  (:use quil.core))

(def square-image (atom nil))
(def apple-image (atom nil))
(def snake (ref (create-snake)))
(def apple (ref (create-apple)))

(defn setup []
  (frame-rate 50)
  (background 0)
  (swap! square-image (fn [_] (load-image "square.gif")))
  (swap! apple-image (fn [_] (load-image "apple.gif"))))

(defn draw-body-part [v]
  (image @square-image (* 10 (v 0)) (* 10 (v 1))))

(defn draw-apple [v]
  (image @apple-image (* 10 (v 0)) (* 10 (v 1))))

(defn draw []
  (background 0)
  (doall (map draw-body-part (snake :body)))
  (draw-apple (apple :location)))
   
(defn key-handler []
  (update-dirs snake (directions (key-as-keyword))))

(defn -main [] 
  (future (game snake apple))
  (defsketch snake-skatch
    :title "Snake"
    :key-pressed key-handler
    :setup setup
    :size [(* 10 h) (* 12 w)]
    :draw draw))
