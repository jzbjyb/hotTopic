#!/usr/bin/env python
# -*- coding: utf-8 -*-

from flask import Blueprint, render_template, request
from web.models import Api, utils
import json

index = Blueprint(__name__, __name__)

@index.route('/', methods=['GET'])
def index_page():
    return render_template('layout.html')

@index.route('/templates', defaults={'async_url': ''}, methods=['GET'])
@index.route('/templates/<path:async_url>', methods=['GET'])
def template(async_url):
    return render_template("/" + request.url[request.url.find(async_url):])

@index.route('/api/<path:async_api>', methods=['GET', 'POST', 'PATCH', 'PUT', 'DELETE', 'OPTIONS'])
def api(async_api):
    async_api = "/" + request.url[request.url.find(async_api):]
    utils.logger.info(" ".join(["ASYNC API", request.method, async_api])) 
    return json.dumps(Api(request.method, async_api, data = request.get_data(), async = True).json())