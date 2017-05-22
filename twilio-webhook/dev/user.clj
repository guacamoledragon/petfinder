(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [org.httpkit.server :as http]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]))

(def server nil)

(defn start [])

(defn stop [])

(defn restart []
  (stop)
  (refresh :after 'user/start))
