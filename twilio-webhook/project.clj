(defproject petfinder/twilio-webhook "0.1.0-SNAPSHOT"
  :description "A Twilio Webhook that interacts with Lex."
  :url "https://github.com/guacamoledragon/petfinder"
  :min-lein-version "2.0.0"
  :dependencies [[cheshire "5.7.1"]
                 [com.amazonaws/aws-java-sdk-lex "1.11.132"]
                 [com.amazonaws/aws-java-sdk-lambda "1.11.132"]
                 [com.amazonaws/aws-lambda-java-core "1.1.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.twilio.sdk/twilio "7.9.1"]
                 [org.clojure/clojure "1.8.0"]
                 [ring/ring-codec "1.0.1"]]
            [lein-ring "0.9.7"]]
  :ring {:handler twilio-webhook.handler/app}
  :profiles {:dev     {:dependencies [[compojure "1.5.1"]
                                      [http-kit "2.2.0"]
                                      [javax.servlet/servlet-api "2.5"]
                                      [org.clojure/tools.namespace "0.2.11"]
                                      [ring/ring-defaults "0.2.1"]
                                      [ring/ring-devel "1.6.0"]
                                      [ring/ring-mock "0.3.0"]]
                       :source-paths ["dev"]}
             :uberjar {:aot      [twilio-webhook.handler]
                       :jvm-opts ["-Xmx1g"]}})
