(ns freelance-calculator.events
    (:require [re-frame.core :as re-frame]
              [freelance-calculator.db :as db]))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))
