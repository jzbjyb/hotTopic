cd `dirname $0`

sh setup.sh

source env/bin/activate
docker build -t "hot-topic/web-service" .
deactivate
