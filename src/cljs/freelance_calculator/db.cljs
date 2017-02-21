(ns freelance-calculator.db
  (:require [cljs.spec :as s]
            [cljs.spec.impl.gen]))




(s/def ::hours-per-week (s/and number? #(<= 0 % 168)))

(s/def ::weeks-off (s/and number? #(<= 0 % 52)))

(s/def ::insurance-rate (s/and number? #(<= 0 %)))

(s/def ::employed-ins-rate ::insurance-rate)

(s/def ::open-market-ins-rate ::insurance-rate)

(s/def ::hourly-wage-range (s/and (s/cat :min ::hourly-wage :max ::hourly-wage)
                                  (fn [{:keys [min max]}]
                                    (<= min max 200))))

(s/def ::db (s/keys :req-un [::hours-per-week
                             ::weeks-off
                             ::insurance-rate
                             ::employed-ins-rate
                             ::open-market-ins-rate
                             ::hourly-wage-range]))

(def default-db
  {:db {:hours-per-week 30
        :weeks-off 10
        :employed-ins-rate 200
        :open-market-ins-rate 500
        :hourly-wage-range [45 105]}})


