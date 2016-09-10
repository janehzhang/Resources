/*
0为红球，1为白球，2为蓝球。  
将 0 1 2 1 1 2 0 2 1 0  弄成   0 0 0 1 1 1 1 2 2 2

弄三个指针begin，current，end。指向首，当前，尾部。

*/


while(current<=end){
	if(array[current] == 0){		//遇到红球，交换current和begin
		swap(array[current],array[begin]);
		current++;
		begin++;
	}else if(array[current]==1){
		current++;
	}else{
		swap(array[current],array[end]);
		end--;
	}
}

//类似于快排中的partition
