(ns freelance-calculator.views
    (:require [re-frame.core :as re-frame]
              [goog.i18n.NumberFormat]))

(enable-console-print!)


(def soc-sec-rate 0.123)
(def medicare-rate 0.030)
(def soc-sec-salary-cutoff 113700)

(def max-hourly-wage 200)
(def hourly-wage-step 5)

(def inputs [{:key :hours-per-week
              :type "text"
              :label "Hours per week"}
             {:key :weeks-off
              :type "text"
              :label "Weeks off"}
             {:key :health-ins-diff
              :type "text"
              :label "Monthly health insurance diff"}
             {:key :low-hourly-wage
              :type "range"
              :label "Minimum hourly wage"}
             {:key :high-hourly-wage
              :type "range"
              :label "Maximum hourly wage"}])



(def default-db
  {:db {:hours-per-week       30
        :weeks-off            10
        :employed-ins-rate    200
        :open-market-ins-rate 500
        :hourly-wage-range    [45 105]}})


(defn dollar-str
  "round to the nearest dollar, no decimal places"
  [n]
  (let [with-decimal
        (.format (goog.i18n.NumberFormat.
                   (.-CURRENCY goog.i18n.NumberFormat.Format)) n)]
    (subs with-decimal 0 (- (count with-decimal) 3))))

(defn row [{:keys [hourly-wage
                   hours-per-week
                   weeks-off
                   health-ins-diff]}]
  (let [weekly-income (* hourly-wage hours-per-week)
        yearly-income (* weekly-income (- 52 weeks-off))
        if-w2 (- yearly-income (* health-ins-diff 12))
        soc-sec-tax (* (min yearly-income soc-sec-salary-cutoff)
                       (/ soc-sec-rate 2))
        medicare-tax (* yearly-income (/ medicare-rate 2))
        if-1099 (- if-w2 (+ soc-sec-tax medicare-tax))]
    [:tr {:key hourly-wage}
     (map-indexed (fn [idx n]
                    [:td {:key idx} (dollar-str n)])
                  [hourly-wage weekly-income yearly-income if-w2 if-1099])]))


(defn main-table [{:keys [hours-per-week
                          weeks-off
                          employed-ins-rate
                          open-market-ins-rate
                          hourly-wage-range] :as db}]
  (prn db)
  (let [[min max] hourly-wage-range
        health-ins-diff (- open-market-ins-rate employed-ins-rate)]
    [:table.main-table
     [:thead
      [:tr
       [:th "Hourly wage"]
       [:th "Weekly income"]
       [:th "Yearly income"]
       [:th "If you'll be paid as an employee on a W-2, FTE salary equiv is:"]
       [:th "If you'll be an independent contractor, FTE salary equiv is:"]]]
     [:tbody
      (let [wage-range (range min (inc max) hourly-wage-step)]
        (map #(row {:hourly-wage     %
                    :hours-per-week  hours-per-week
                    :weeks-off       weeks-off
                    :health-ins-diff health-ins-diff})
             wage-range))]]))



(defn main-panel []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div.page
       (main-table (:db default-db))])))





