#!/usr/bin/env python
# -*- coding: utf-8 -*-

from flask import Flask, Blueprint
from flask.ext.compress import Compress
from web import views
from web.models import utils
from logging import StreamHandler, DEBUG, INFO, Formatter

app = Flask(__name__)
Compress(app)

#log config
level = DEBUG
handler = StreamHandler()
handler.setLevel(level)
handler.setFormatter(Formatter("[%(levelname)s] %(asctime)s %(message)s", '%Y-%m-%d %H:%M:%S'))
app.logger.handlers = []
app.logger.addHandler(handler)
app.logger.setLevel(level)

#blueprints config
BLUEPRINTS = [ 
    (views.index, '')
    ]

for blueprint, url_prefix in BLUEPRINTS:
    app.register_blueprint(blueprint, url_prefix=url_prefix)

app.jinja_env.globals.update(
    format_time = utils.format_time,
    format_time_all = utils.format_time_all
    )