import csv
import sys

#读取path = F:\Electronic\Resources\LearnPython\data\B_train.csv目录里面的csv文件

path = 'F:\Electronic\Resources\LearnPython\\testData\B_train.csv'


#with保证了当with退出时存在异常，csv文件也会关闭，相当于代替了trycatch
with open(path, 'r') as csvfile:				
     #读取csv文件，返回的是迭代类型
     #reader = csv.reader(csvfile)
     reader = csv.DictReader(csvfile)
     for row in reader:
        print(row)

     print(reader.fieldnames)


