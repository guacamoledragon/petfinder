(ns twilio-webhook.lex
  (:import (com.amazonaws.services.lexruntime.model PostTextRequest PostTextResult)
           (com.amazonaws.services.lexruntime AmazonLexRuntimeClient AmazonLexRuntimeClientBuilder)
           (com.amazonaws.auth BasicAWSCredentials AWSCredentialsProvider AWSStaticCredentialsProvider)
           (com.amazonaws.regions Regions)))

(def bot-name "PetFinder")
(def bot-alias "dev")

(defn lex-builder
  []
  (-> (AmazonLexRuntimeClientBuilder/standard)
      (.withRegion (Regions/US_EAST_1))))

(defn post-text-request
  [{:keys [user-id bot-name bot-alias input-text]}]
  (-> (PostTextRequest.)
      (.withUserId user-id)
      (.withBotName bot-name)
      (.withBotAlias bot-alias)
      (.withInputText input-text)))

(defn ^PostTextResult send-text-request
  [from input-text]
  (when-some [text (not-empty input-text)]
    (let [lex-client (.build (lex-builder))
          request (post-text-request {:user-id from
                                      :bot-name bot-name
                                      :bot-alias bot-alias
                                      :input-text text})]
      (.postText lex-client request))))
