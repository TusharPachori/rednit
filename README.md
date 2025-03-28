Elastic Search Setup

1. Install docker image: https://docs.docker.com/desktop/setup/install/mac-install/
2. Run this command to start docker image: docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" -e "xpack.security.enabled=false" elasticsearch:8.11.2
3. If you want to run elastic search with password use this command: docker run -d --name es-container \
  -e "discovery.type=single-node" \
  -p 9200:9200 -p 9300:9300 \
  docker.elastic.co/elasticsearch/elasticsearch:8.12.0





