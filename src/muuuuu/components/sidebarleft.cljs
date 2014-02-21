(ns muuuuu.components.sidebarleft
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [clojure.string]
            [muuuuu.utils :refer [guid get-next-color]]))

(enable-console-print!)

(defn- value-from-node
  [component field]
  (let [n (om/get-node component field)
        v (-> n .-value clojure.string/trim)]
    (when-not (empty? v)
      [v n])))

(defn- clear-nodes!
  [& nodes]
  (doall (map #(set! (.-value %) "") nodes)))

; Structure (element.classname  - componentname)
; div.sidebar                   - allrooms
;   div.channellist
;     ul.joinchatmenu
;       li                      - channel
;     ul
;       li                      - addchannelform
;         form
;           span
;           input
;       li                      - channel

(defn addChannel [title app]
  "Puts a new channel in the app-state"
  (let [[room] [{:title title :color (get-next-color) :inviewport false :id (guid)}]]
    (om/transact! app [:rooms]
      (fn [rooms] (conj rooms room)))))

(defn newChannel
  "Handles form submit"
  [e app owner]
  (let [[channelname channel-node] (value-from-node owner "channelname")]
    (when channelname
      ;(prn channelname))
      (addChannel channelname app)
      (clear-nodes! channel-node))
    false))

(defn joinedchannel [data app owner]
  (let [[title colors] [(first data) (:color (second data))]]
  (om/component
    (dom/li
      #js {:data-panel title
           :className (if (false? (:bright :color (second room))) "bright")
           :style  #js {:borderColor (str "#" (:hex :color (second room)))
                        :backgroundColor (str "#" (:color :hex (second room)))}}
      (dom/a nil (first room))))))

(defn popularchannels [app owner opts]
  (om/component
      (dom/li nil
        (dom/a #js {:onClick #(addChannel % opts)}
          (first app)
          ;(dom/span #js {:className "count"} 3)
        ))))

(defn addchannelform [app owner]
  (om/component
    (dom/li #js {:className "addchannel"}
      (dom/form  #js {:onSubmit #(newChannel % app owner)}
        (dom/span nil "Add new")
        (dom/input #js {:type "text" :placeholder "channel name" :ref "channelname"})))))

(defn init [app owner]
  (om/component
   (dom/div #js {:className "sidebar"}
      (dom/div #js {:className "channellist"}
         (apply dom/ul #js {:className "joinchatmenu"}
                (om/build-all joinedchannel (:joined (:rooms app)) {:key :id}))
         (apply dom/ul nil
                 (om/build addchannelform app)
                 (om/build-all popularchannels (:popular (:rooms app)) {:key :id :opts app}))))))
