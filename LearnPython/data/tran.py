# -*- coding: utf-8 -*-

#导入数值计算库
import numpy as np
#导入科学计算库
import pandas as pd
#导入图表库
import matplotlib.pyplot as plt
#导入机器学习linear_model库, 交叉验证库
#from sklearn import linear_model, datasets, discriminant_analysis, cross_validation


b_train = pd.read_csv('data/B_train.csv', encoding = 'gbk')

#按列统计每列的缺失值

#一共有401列，即401个变量
#print(a_train.columns)
#print(a_train.head(2))

#b_train.info()
b_train.describe()




