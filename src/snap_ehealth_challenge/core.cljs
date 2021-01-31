(ns snap-ehealth-challenge.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))


;; state variables
(def tasks (r/atom []))
(def task-list-controls-input-value (r/atom "test"))

;; event handlers

(def handle-task-controls-button-on-click
  (fn [] 
    (reset! tasks (concat @tasks [{:value @task-list-controls-input-value}]))
    (println @tasks)
    (reset! task-list-controls-input-value "") 
    ))

(def handle-task-list-controls-input-on-change
  (fn [event] (reset! task-list-controls-input-value event.target.value)))

;; components


; "
;   0:  id
;   1:  name
;   3:  age
; ; "
; (def items (r/atom [
;   ["joe" "27"]
;   ["" "35"]
;   ["bill" "42"]]))

; (println (get @items 1))
; (println (get (get @items 1) 1))
; (println (js/Date.now))

; (defn itemsComponent []
;   [:div
;     (for [item @items]
;       [:div {:key (get item 0)} (get item 1)])])

; (def handle-add-button-on-click
;   (fn [] (reset! click-count (+ @click-count 1))))

; (def handle-input-on-change
;   (fn [event] 
;     (reset! items (concat @items [[(js/Date.now) (js/Date.now) 9]])) 
;     (println @input-value)))

(defn application []
  [container [task-list handle-task-list-controls-input-on-change handle-task-controls-button-on-click]])

(defn container [body]
  [:div
    {:class "Container"}
    [:div {:class "ContainerHeader"} 
      [:div {:class "ContainerHeaderSection"}
        [:div {:class "ContainerHeaderSectionPrimary"} "Snap eHealth"]
        [:div {:class "ContainerHeaderSectionSecondary"} "Task Checklist"]
        ]
      [:div {:class "ContainerHeaderSection"}
        [:div {:class "ContainerHeaderSectionPrimary"} "January 01, 2020"]
        [:div {:class "ContainerHeaderSectionSecondary"} "12:00 PM"]
        ]
      ]
    [:div {:class "ContainerBody"} body]
    ])

(defn task-list [handle-task-list-controls-input-on-change handle-task-controls-button-on-click]
  [:div {:class "TaskList"} 
    [:div {:class "TaskListControls"}
      [:input {:class "TaskListControlsInput" :value @task-list-controls-input-value :on-change handle-task-list-controls-input-on-change}]
      [:button {:class "TaskListControlsButton" :on-click handle-task-controls-button-on-click} "Add"]
      ]
    ])


; (defn button []
;   [:button {:class "button" :on-click handle-add-button-on-click} "add"])

; (defn input [event]
;   [:input {:placeholder "test" :on-change handle-input-on-change}])

; (defn counter [value]
;   [:div value])


;; initialize app

(defn mount-root []
  (d/render [application] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
