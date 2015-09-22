#!/usr/bin/env python
# -*- coding: utf-8 -*-

from flask import abort, session, flash
from web.config import CONFIG
from .utils import utils
import requests
import json

DEFAULT_HEADER = {'Content-Type': 'application/json'}
DEFAULT_TIMEOUT = 120

class Api(object):
    def __init__(self, method, path, data={}, async=False):
        self.async = async
        data = json.dumps(data) if type(data) == type({}) else data
        try:
            self.r = requests.request(method, CONFIG['API_URL'] + path, data=data, headers=DEFAULT_HEADER, timeout = DEFAULT_TIMEOUT)
            utils.logger.info(' '.join(['API', method, path]))
            if self.r == None or self.r.status_code != 200:
                if self.r == None:
                    utils.logger.warning(' '.join(['API', method, path, 'timeout']))
                else:  
                    utils.logger.warning(' '.join(['API', method, path, str(self.json()['code']), self.json()['message']]))
                self.abort_on(self.r.status_code)
        except Exception as e:
            utils.logger.warning(' '.join(['API', method, path, 'Status:' + str(e)]))
            self.abort_on(500)

    def abort_on(self, status_code):
        if not self.async:
            abort(status_code)

    def json(self):
        if not hasattr(self, 'r') or self.r == None:
            return {}
        try:
            return self.r.json()
        except:
            utils.logger.warning('response can not be paresd to json')
            self.abort_on(500)

    def data(self, name, return_type='none'):
        try:
            result = None
            if self.json()['data'].has_key(name):
                result = self.json()['data'][name]
            if return_type == 'none':
                return result
            elif return_type == 'first':
                if type(result) == type([]) and len(result) > 0:
                    return result[0]
                else:
                    return None
            elif return_type == 'list':
                if type(result) == type([]):
                    return result
                else:
                    return []
        except:
            utils.logger.warning('API data %s not found' % name)
            abort(404)