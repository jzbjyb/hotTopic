#!/usr/bin/env python
# -*- coding: utf-8 -*-

import logging
import datetime, re

class utils(object):
    logger = logging.getLogger('web')

    MONTH = {
        1: 'Jan',
        2: 'Feb',
        3: 'Mar',
        4: 'Apr',
        5: 'May',
        6: 'Jun',
        7: 'Jul',
        8: 'Aug',
        9: 'Sep',
        10: 'Oct',
        11: 'Nov',
        12: 'Dec'
    } 

    @staticmethod
    def format_time_all(iso_date):
        #parser iso_date to datetime
        d = datetime.datetime(*map(int, re.split('[^\d]', str(iso_date))[:6]))
        return u'%s %d, %d'%(utils.MONTH[d.month], d.day, d.year)

    @staticmethod
    def format_time(iso_date):
        #parser iso_date to datetime
        d = datetime.datetime(*map(int, re.split('[^\d]', str(iso_date))[:6]))

        cur = datetime.datetime.now()

        if d.date() == cur.date():
            return d.strftime('%H:%M')
        elif (cur.date() - d.date()).days <= 3:
            return u"%d天前"%(cur.date() - d.date()).days
        elif d.year == cur.year:
            return u"%d月%d日"%(d.month, d.day)
        else:
            return u"%d年%d月%d日"%(d.year, d.month, d.day)