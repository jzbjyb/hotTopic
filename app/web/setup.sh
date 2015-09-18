#!/bin/bash

cd `dirname -- $0`
virtualenv env
source env/bin/activate
pip install -r etc/requirements.txt
cd static
bower install --allow-root
compass compile sass/main.scss --force -s compressed