(ns snake.ui
  (:use snake.core)
  (:use quil.core))

(def square (atom nil))
(def snake (ref (create-snake)))
(def apple (ref (create-apple)))

(defn setup []
  (frame-rate 1)
  (swap! square (fn [_] (load-image "square.gif"))))

(defn body-part [v]
  (println v)
  (image @square  (* 10 (v 0)) (* 10 (v 1))))

(defn draw []
  (background 10 100 10)
  ;(println (snake :body))
  (map body-part (snake :body)))
   
(defn -main [] 
  (future (game snake apple))
  (defsketch snake-skatch
    :title "Snake"
    :key-pressed #()
    :setup setup
    :frame false
    :on-close shutdown-agents
    :draw draw))
