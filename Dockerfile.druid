FROM alpine:latest AS extensions
RUN apk add --no-cache curl
WORKDIR /extensions
ADD https://repo1.maven.org/maven2/software/amazon/msk/aws-msk-iam-auth/2.3.2/aws-msk-iam-auth-2.3.2-all.jar /extensions/aws-msk-iam-auth-2.3.2-all.jar

FROM apache/druid:35.0.0
ENV DRUID_SET_HOST_IP=1
COPY --from=extensions --chown=1000:1000 /extensions/aws-msk-iam-auth-2.3.2-all.jar /opt/druid/extensions/druid-kafka-indexing-service
COPY --from=extensions --chown=1000:1000 /extensions/aws-msk-iam-auth-2.3.2-all.jar /opt/druid/extensions/druid-kafka-extraction-namespace
COPY --from=extensions --chown=1000:1000 /extensions/aws-msk-iam-auth-2.3.2-all.jar /opt/druid/extensions/druid-ranger-security
COPY --from=extensions --chown=1000:1000 /extensions/aws-msk-iam-auth-2.3.2-all.jar /opt/druid/extensions/kafka-emitter
