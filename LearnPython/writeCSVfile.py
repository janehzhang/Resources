import csv

path = 'data/a.csv'
header = ['id', 'url', 'keywords']
data = [
  ('1', 'http://www.xiaoheiseo.com/', '小黑'),
  ('2', 'http://www.baidu.com/', '百度'),
  ('3', 'http://www.jd.com/', '京东')
]


with open(path, 'w') as csvfile:			#b代表binary类型，不能用with open('a.csv', 'wb') as csvfile:
	writer = csv.writer(csvfile, dialect='excel')
	writer.writerow(header)
	writer.writerows(data)		#writerows!!!!
	csvfile.close()
	
		