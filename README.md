Elastic Search Setup

1. Install docker image: https://docs.docker.com/desktop/setup/install/mac-install/
2. Run this command to start docker image: docker run -d --name elasticsearch -p 9200:9200 -e "discovery.type=single-node" -e "xpack.security.enabled=false" elasticsearch:8.11.2
3. If you want to run elastic search with password use this command: docker run -d --name es-container \
  -e "discovery.type=single-node" \
  -p 9200:9200 -p 9300:9300 \
  docker.elastic.co/elasticsearch/elasticsearch:8.12.0

Redis setup:
1. brew install redis     
2.  redis-server 


Minio Setup
1. Run this command docker run -p 9000:9000 -p 9001:9001 \
   --name minio \
   -e "MINIO_ROOT_USER=admin" \
   -e "MINIO_ROOT_PASSWORD=admin123" \
   -v ~/minio-data:/data \
   quay.io/minio/minio server /data --console-address ":9001"


