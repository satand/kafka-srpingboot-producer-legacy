# Kafka SpringBoot Producer Legacy

```bash
podman build -t kafka-springboot-producer-legacy:1.0.0 .
# podman build --platform linux/amd64 -t kafka-springboot-producer-legacy:1.0.0 .
podman tag kafka-springboot-producer-legacy:1.0.0 quay.io/<user>/kafka-springboot-producer-legacy:1.0.0
podman push quay.io/<user>/kafka-springboot-producer-legacy:1.0.0
```

Only in case of private registry repo

```bash
oc create secret docker-registry quay-pull-secret \
  --docker-server=quay.io \
  --docker-username=<user-or-robot> \
  --docker-password=<token-quay>
```
