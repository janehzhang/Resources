//给你一个文件，里面包含40亿个整数，写一个算法找出该文件中不包含的一个整数， 假设你有1GB内存可用。
//如果你只有10MB的内存呢？

//1.40亿个整数用bitmap存需要用500M内存

#include <stdio.h>
#include <stdlib.h>
#define N  100			//N是文件里的整数的数目
#define BitLen 32

int bmap[1+N/BitLen];      //N是总数，N=40亿，一个int有32bit


void setVal(int val){
	bmap[val/BitLen] = (1<<(val%BitLen));              //bmap[val/32]：每个数在bmap的位置；
	//bmap[val>>5] != (val&0x1F);//这个更快？
}
/*
我们插入一个整数val，要先计算val位于数组bmap中的索引:index = val/32;
比如整数33，index=33/32=1,第33位于数组中的index=1
比如整数67，index=67/32=2,位于数组中index=2

然后在计算在这个index中的位置，因为数组中的每个元素有32位
33，index=1，在1中的位置为33%32=1
67，index=2，在2中的位置为67%32=3
然后就是标识这个位置为1：
bmap[val/32]  |= (1<<(val%32));
33: bmap[1]    != (1<<1);//xxxxxx1x,红丝位置被置为1
67: bmap[2]   !=  (1<<3);//xxxx1xxx
*/


int testVal(int val){
	if(bmap[val/BitLen]&&(1<<(val%BitLen))){
		return 1;
	}else{
		return 0;
	}
	//return bmap[val>>5] & (val&0x1F);
}
/*
怎样检测整数是否存在？
比如我们检测33，同样我们需要计算index，以及在index元素中的位置
33: index = 1, 在bmap[1]中的位置为 1，只需要检测这个位置是否为1

bmp[1] &(1<<1),这样是1返回true，否侧返回false
67:bmp[2]&(1<<3)
127:bmp[3]&(1<<31)
*/

int main()
{
	int a[] = {1, 2, 3, 4, 6, 7};

	for (int i=0; i<6; ++i)
	{
		setVal(a[i]);
	}

	printf("%d",testVal(5));
	getchar();
	return 0;
	
}



/*
现在我们来看如果内存要求是10MB呢？
这当然不能用bitmap来直接计算。因为从40亿数据找出一个不存在的数据，我们可以将这么多的数据分成许

多块， 比如每一个块的大小是1000，那么第一块保存的就是0到999的数，第2块保存的就是1000 到1999的数……

实际上我们并不保存这些数，而是给每一个块设置一个计数器。 这样每读入一个数，我们就在它所在的块对应的计数器加1。

处理结束之后， 我们找到一个块，它的计数器值小于块大小(1000)， 说明了这一段里面一定有数字是文件中所不包含的。然后我们单独处理
这个块即可。接下来我们就可以用Bit Map算法了。我们再遍历一遍数据， 把落在这个块的数对应的位置1(我们要先把这个数
归约到0到blocksize之间)。 最后我们找到这个块中第一个为0的位，其对应的数就是一个没有出现在该文件中的数。)
*/
const int N           = 1000;
const int BITLEN      = 32;
const int BLOCK_SIZE  = 100;

int Bucket[1+N/BLOCK_SIZE]={0};
int BitMap[1+BLOCK_SIZE/BITLEN] = {0};

void test()
{
	//生成测试数据
	freopen("test.txt", "w", stdout);
	for (int i=0; i<1000; ++i)
	{
		if (i == 127) 
		{
			printf("0\n");
			continue;
		}
		printf("%d\n", i);
	}
	fclose(stdout);

	//读入测试数据
	freopen("test.txt", "r", stdin);
	int Value;
	while (scanf("%d", &Value) != EOF)
	{
		++Bucket[Value/BLOCK_SIZE]; //测试数据分段累计
	}
	fclose(stdin);

    //找出累计计数小于BLOCK_SIZE的
	int Start=-1, i;
	for (i=0; i<1+N/BLOCK_SIZE; ++i)
	{
		if (Bucket[i] < BLOCK_SIZE)
		{
			Start = i*BLOCK_SIZE;
			break;
		}
	}
	if (i == 1+N/BLOCK_SIZE || Bucket[N/BLOCK_SIZE]==0 && i==N/BLOCK_SIZE) return;
    int End = Start + BLOCK_SIZE-1;

	//在不满足的那段用bitmap来检测
	freopen("test.txt", "r", stdin);
	while (scanf("%d", &Value) != EOF)
	{
		if (Value >= Start && Value <= End)//Value必须满足在那段
		{
			int Temp = Value - Start;
			BitMap[Temp/BITLEN] |= (1<<(Temp%BITLEN));
		}
	}
	fclose(stdin);

	//找出不存在的数
	freopen("re.txt", "w", stdout);
	bool Found = false;
	for (int i=0; i<1+BLOCK_SIZE/BITLEN; ++i)
	{
		for (int k=0; k < BITLEN; ++k)
		{
			if ((BitMap[i] & (1<<k)) == 0) 
			{
				printf("%d ", i*BITLEN+k+Start);
				Found = true;
				break;
			}
		}
		if (Found) break;
	}
	fclose(stdout);
}
