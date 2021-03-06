(defproject petfinder/twilio-webhook "0.1.0-SNAPSHOT"
  :description "A Twilio Webhook that interacts with Lex."
  :url "https://github.com/guacamoledragon/petfinder"
  :min-lein-version "2.0.0"
  :dependencies [[amazonica "0.3.101" :exclusions [com.amazonaws/amazon-kinesis-client
                                                   com.amazonaws/aws-java-sdk
                                                   com.amazonaws/dynamodb-streams-kinesis-adapter]]
                 [camel-snake-kebab "0.4.0"]
                 [cheshire "5.7.1"]
                 [com.amazonaws/aws-java-sdk-core "1.11.132" :exclusions [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor
                                                                          joda-time]]
                 [com.amazonaws/aws-java-sdk-lambda "1.11.132"]
                 [com.amazonaws/aws-java-sdk-lex "1.11.132"]
                 [com.amazonaws/aws-java-sdk-s3 "1.11.132"]
                 [com.amazonaws/aws-lambda-java-core "1.1.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [com.twilio.sdk/twilio "7.9.1" :exclusions [com.fasterxml.jackson.core/jackson-annotations
                                                             com.fasterxml.jackson.core/jackson-core
                                                             com.fasterxml.jackson.core/jackson-databind
                                                             io.jsonwebtoken/jjwt
                                                             joda-time
                                                             org.apache.httpcomponents/httpclient]]
                 [http-kit "2.2.0"]
                 [org.clojure/clojure "1.8.0"]
                 [ring/ring-codec "1.0.1" :exclusions [commons-codec]]]
  :plugins [[lein-clj-lambda "0.10.3"]]
  :ring {:handler twilio-webhook.handler/app}
  :lambda {"dev" [{:handler       "twilio-webhook.handler.handler"
                   :function-name "twilio-webhook"
                   :memory-size   512
                   :timeout       15
                   :region        "us-east-1"
                   :s3            {:bucket     "petfinder.com"
                                   :object-key "lambda/twilio-webhook-0.1.0-SNAPSHOT.jar"}}]}
  :profiles {:dev     {:dependencies   [[org.clojure/tools.namespace "0.2.11"]]
                       :resource-paths ["resources" "dev-resources"]
                       :source-paths   ["dev"]}
             :uberjar {:aot      [twilio-webhook.handler]
                       :jvm-opts ["-Xmx1g"]}})
