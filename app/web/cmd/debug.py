#!/usr/bin/env python
# -*- coding: utf-8 -*-

from web import app
import logging

log = logging.getLogger('werkzeug')
log.setLevel(logging.WARNING)

app.run(host="0.0.0.0", debug=True, port=18202, use_debugger=True, use_reloader=True)