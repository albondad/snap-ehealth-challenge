(ns snap-ehealth-challenge.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))


;; state variables
(def empty-array [])
(def tasks (r/atom []))
(def task-list-controls-input-value (r/atom ""))


;; utility functions
(defn create-id []
  (js/Date.now))


;; event handlers
(defn handle-task-controls-button-on-click []
    (reset! tasks (concat @tasks [{:id (create-id) :checked false :value @task-list-controls-input-value}]))
    (println @tasks)
    (reset! task-list-controls-input-value ""))

(defn handle-task-list-controls-input-on-change []
  [event] (reset! task-list-controls-input-value event.target.value))


;; components
(defn application []
  [container [task-list 
    @tasks 
    handle-task-list-controls-input-on-change 
    handle-task-controls-button-on-click
    handle-task-list-list-item-checkbox-on-click]])

(defn container [body]
  [:div
    {:class "Container"}
    [:div {:class "ContainerHeader"} 
      [:div {:class "ContainerHeaderSection"}
        [:div {:class "ContainerHeaderSectionPrimary"} "Snap eHealth"]
        [:div {:class "ContainerHeaderSectionSecondary"} "Task Checklist"]]
      [:div {:class "ContainerHeaderSection"}
        [:div {:class "ContainerHeaderSectionPrimary"} "January 01, 2020"]
        [:div {:class "ContainerHeaderSectionSecondary"} "12:00 PM"]]]
    [:div {:class "ContainerBody"} body]])

(defn task-list 
  [tasks 
   handle-task-list-controls-input-on-change handle-task-controls-button-on-click 
   handle-task-list-list-item-checkbox-on-click]
  [:div {:class "TaskList"} 
    [:div {:class "TaskListControls"}
      [:input {:class "TaskListControlsInput" :value @task-list-controls-input-value :placeholder "task name" :on-change handle-task-list-controls-input-on-change}]
      [:button {:class "TaskListControlsButton" :on-click handle-task-controls-button-on-click} "Add"]]
  [:div {:class "TaskLisList"} (for [task tasks]
    [:div {:key (get task :id) :class "TaskListListItem"} 
      [:div {:class (+ "TaskListListItemCheckbox" (if (get task :checked) " TaskListListItemCheckbox--Checked" ""))} ""]
      [:div {:class "TaskListListItemValue"} (get task :value)]
      [:div {:class "TaskListListItemDelete"} "delete"]])]])


;; initialize app
(defn mount-root []
  (d/render [application] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
