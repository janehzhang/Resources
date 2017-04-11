# -*- coding: utf-8 -*-
"""
Created on Sat Apr  8 08:22:32 2017

@author: zhangjaneh
"""

import pandas as pd



df1 = pd.DataFrame({'key' : ['a', 'b', 'c', 'a', 'b', 'g'],
                'value' : range(6)})

df2 = pd.DataFrame({'key' : ['a', 'b', 'c'],
                'value' : range(3)})

#merge默认做的是内连接
mer = pd.merge(df1, df2, on = 'key', how = 'left')    #指明要用key列进行left连接
