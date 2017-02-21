(ns freelance-calculator.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :get-the-stuff
 (fn [db]
   (prn "db")
   (prn db)
   (:data db)))
