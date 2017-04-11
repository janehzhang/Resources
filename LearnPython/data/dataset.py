import csv
import numpy as np
import matplotlib.pyplot as plt

path = "B_train.csv"

def dataset(path, filter_field = None, filter_value = None):
	with open(path, 'r') as csvfile:
		reader = csv.DictReader(csvfile)

		#print set([row["filter_field"] for row in reader])

		if filter_field:
			for row in filter(lambda row: row[filter_field] == filter_value, reader):
				yield row
		else:
			for row in reader:
				yield row


def  main(path):
	data = [(row["Year"], 
		float(row["Average income"])) for row in dataset(path, "Country", "United State") ]
	width = 0.35
	ind = np.arange(len(data))
	fig =  plt.figure()			
    
	ax = plt.subplot(111)		#AxesSubplot对象
	ax.bar(ind, list(d[1] for d in data))
	ax.set_xticks(np.arange(0, len(data), 4))		#刻度位置
	ax.set_xtickslables(list(d[0] for d in data)[0::4],
		rotation = 45)	#刻度标签
	ax.set_yticks("income in USA")

	plt.title("USA icome in 1913-2008")
	plt.show()

if __name__ == "__main__":
	   main(path)