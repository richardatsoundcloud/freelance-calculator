(ns freelance-calculator.events
    (:require [re-frame.core :as re-frame]
              [freelance-calculator.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
  :input-stuff
  (fn [db [event-name field-key input-text]]
    (prn "debug")
    (prn field-key)
    (prn input-text)
    (assoc-in db [:data field-key] (js/parseInt input-text))))

