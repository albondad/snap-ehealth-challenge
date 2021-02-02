(ns snap-ehealth-challenge.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))


;;debugging
(println (count (clojure.string/split "test test" #"\s")))

;; state variables
(def empty-array [])
(def tasks (r/atom []))
(def task-list-controls-input-value (r/atom ""))
(def progress (r/atom 0))


;; utility functions
(defn create-id []
  (js/Date.now))

(defn update-progress []
  (def complete-tasks-count (count (remove nil? (concat [] (for [task @tasks] (if (= (get task :checked) true) 1))))))
  (def tasks-count (count @tasks))
  (reset! progress (if (= tasks-count 0) 0 (* (/ complete-tasks-count tasks-count) 100))))



;; event handlers
(defn handle-task-controls-button-on-click []
    (reset! tasks (concat @tasks [{:id (create-id) :checked false :value @task-list-controls-input-value}]))
    (println @tasks)
    (reset! task-list-controls-input-value "")
    (update-progress))

(defn handle-task-list-controls-input-on-change []
  [event] (reset! task-list-controls-input-value event.target.value))

(defn handle-task-list-list-item-checkbox-on-click [id]
  (fn [] 
    (println id)
    (def new-tasks (concat [] (for [task @tasks] (if (= id (get task :id)) {:id (get task :id) :checked (not (get task :checked)) :value (get task :value)} {:id (get task :id) :checked (get task :checked) :value (get task :value)}))))
    (println new-tasks)
    (reset! tasks new-tasks)
    (println "checkbox clicked")
    (update-progress)))

(defn handle-task-list-list-item-delete-on-click [id]
  (fn [] 
    (println id)
    (def new-tasks (remove nil? (concat [] (for [task @tasks] (if (not (= id (get task :id))) {:id (get task :id) :checked (get task :checked) :value (get task :value)})))))
    (println new-tasks)
    (reset! tasks new-tasks)
    (println "delete clicked")
    (update-progress)))


;; components
(defn application []
  [:div
    [container 
    [statistics @progress @tasks]
    [task-list 
      @tasks
      handle-task-list-controls-input-on-change 
      handle-task-controls-button-on-click
      handle-task-list-list-item-checkbox-on-click
      handle-task-list-list-item-delete-on-click]]])

(defn container [statistics body]
  [:div
    {:class "Container"}
    [:div {:class "ContainerHeader"} 
      [:div {:class "ContainerHeaderSection"}
        [:div {:class "ContainerHeaderSectionPrimary"} "Snap eHealth"]
        [:div {:class "ContainerHeaderSectionSecondary"} "Task Checklist"]]
      [:div {:class "ContainerHeaderSection"}
        [:div {:class "ContainerHeaderSectionPrimary"} "January 01, 2020"]
        [:div {:class "ContainerHeaderSectionSecondary"} "12:00 PM"]]]
    [:div {:class "ContainerBody"} statistics body]])

(defn statistics [progress tasks]
  [:div {:class "Statistics"} 
    [:div {:class "StatisticsSection"}  
      [:div {:class "StatisticsSectionContent"}
        [:div {:class "StatisticsSectionContentHeader"} "Completed"]
        [:div {:class "StatisticsSectionContentBody"} [pie-chart progress]]]]
  [:div {:class "StatisticsSection"}  
      [:div {:class "StatisticsSectionContent"}
        [:div {:class "StatisticsSectionContentHeader"} "Word Count"]
        [:div {:class "StatisticsSectionContentBody"} [bar-chart tasks]]]]])

  (defn pie-chart [progress]
    [:div {:class "PieChart"}
      [:div {:class "PieChartRight"}
        [:div {:class "PieChartRightMask" :style {:transform (str "rotate(" (* (/ (if (> progress 50) 50 progress) 50) 180) "deg)")}}]]
      [:div {:class "PieChartLeft" }
        [:div {:class "PieChartLeftMask" :style {:transform (str "translateX(100%) rotate(" (* (/ (if (<= progress 50) 50 progress) 50) 180) "deg)")}}]]])

  (defn bar-chart [tasks]
    [:div {:class "BarChart"} 
      [:div {:class "BarChartBody"}
        (for [task tasks] 
          [:div {:key (get task :id) :class "BarChartBodyItem"}
            [:div {:key (get task :id) :class "BarChartBodyItemFill" :style {:height (str (* (/ (count (clojure.string/split (get task :value) #"\s")) 20) 100) "%")}}]])]
      [:div {:class "BarChartFooter"} 
        [:div {:class "BarChartFooterText"} (str "Words: " (reduce + (concat [] (for [task tasks] (count (clojure.string/split (get task :value) #"\s"))))))]]])

  
(defn task-list 
  [tasks
   handle-task-list-controls-input-on-change 
   handle-task-controls-button-on-click 
   handle-task-list-list-item-checkbox-on-click
   handle-task-list-list-item-delete-on-click]
  [:div {:class "TaskList"} 
    [:div {:class "TaskListControls"}
      [:input {:class "TaskListControlsInput" :value @task-list-controls-input-value :placeholder "task name" :on-change handle-task-list-controls-input-on-change}]
      [:button {:class "TaskListControlsButton" :on-click handle-task-controls-button-on-click} "Add"]]
  [:div {:class "TaskLisList"} (for [task tasks]
    [:div {:key (get task :id) :class "TaskListListItem"} 
      [:div {:class (+ "TaskListListItemCheckbox" (if (get task :checked) " TaskListListItemCheckbox--Checked" "")) :on-click (handle-task-list-list-item-checkbox-on-click (get task :id))} ""]
      [:div {:class "TaskListListItemValue"} (get task :value)]
      [:div {:class "TaskListListItemDelete" :on-click (handle-task-list-list-item-delete-on-click (get task :id))} "delete"]])]])


;; initialize app
(defn mount-root []
  (d/render [application] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
