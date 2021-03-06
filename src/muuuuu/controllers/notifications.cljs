(ns muuuuu.controllers.notifications
  (:require [cljs.core.async :refer [<! chan timeout]]
            [goog.events :as events])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def state (atom {:title "" :message "" :time 5000}))

(defn make-notification
  "Makes a notification and returns the notification object"
  [title message duration]
  (let [notifi (.-webkitNotifications js/window)]
    (if (not= (.checkPermission notifi) 0)
      (.requestPermission notifi)
      (let [notification (.createNotification notifi nil title message)]
          (.show notification)
          (go
            (<! (timeout 2000))
            (.cancel notification))
        notification))))

(defn on-mention [msg roomname]
  (if (re-find #"@Guest" (:content msg))
  ; TODO correct regex
  ; TODO if window not in focus and or not in viewport
  ; TODO strip name from message (I presume the user knows it's own name)
  (let [notification (make-notification (str (:sender msg) " mentions you") (:content msg) 4000)]
    (set! (.-onclick notification)
      #((.focus js/window nil)
        (.panelSnap (js/$ ".chat") "snapToPanel"
        (js/$ (str "[data-panel=\"" roomname "\"]")))
      )))))

(add-watch state :notification
  (fn [_ _ _ message]
    (make-notification (:title @state) (:message @state) (:time @state))))
