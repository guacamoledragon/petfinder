(ns user
  (:require [clojure.tools.namespace.repl :refer [refresh]]
            [org.httpkit.server :as http]
            [ring.middleware.reload :refer [wrap-reload]]
            [twilio-webhook.handler :refer [app-routes]]))

(def server nil)

(defn start []
  (let [app (wrap-reload #'app-routes)
        port 3000]
    (alter-var-root #'server
                    (constantly (http/run-server app {:port port})))
    (println (str "Listening on port " port "!"))))

(defn stop []
  (alter-var-root #'server
                  (fn [s]
                    (when s
                      (s :timeout 100)))))

(defn restart []
  (stop)
  (refresh :after 'user/start))
