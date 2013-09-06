(ns snake.ui
  (:use snake.core)
  (:use quil.core))

(def square (atom nil))
(def snake (ref (create-snake)))
(def apple (ref (create-apple)))

(defn setup []
  (frame-rate 1)
  (swap! square (fn [_] (load-image "square.gif"))))

(defn draw-body-part [v]
  (image @square (v 0) (v 1)))

(defn draw []
  (background 10 100 10)
  (println (snake :body))
  (map draw-body-part (snake :body)))
    
(defn -main [] 
  (do
    (future (game snake apple))
    (defsketch snake-skatch
      :title "Snake"
      :key-pressed #()
      :setup setup
      :draw draw)))
