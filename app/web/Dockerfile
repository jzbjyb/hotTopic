FROM localhost:5000/centos7
MAINTAINER jzb <rucjzb@163.com>

# ENV PYTHONHOME /data/apps/web/env
COPY get-pip.py /get-pip.py
COPY . /data/apps/web
COPY cmd/debug.py /data/apps/debug.py

RUN python get-pip.py
RUN pip install virtualenv
RUN pip install -r /data/apps/web/etc/requirements.txt
WORKDIR /data/apps/web

EXPOSE 18201
ENTRYPOINT python -V && \
	python /data/apps/debug.py