import sys

def test():					#写成__test()就是private的
	args = sys.argv				#默认第一个元素放该py文件名
	if len(args) == 1:
		print('hello, %s' %args[0])
	elif len(args) == 2:
		print('hello, %s' % args[1])
	else:
		print('too many args')

if __name__ == '__main__':
	test()