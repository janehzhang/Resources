#include <stdio.h>
#include <string.h>
#include <assert.h> 

/*
举例说明
	input:abcadefg
	rule:a?c
	output:abc

	input :newsadfanewfdadsf
	rule: new
	output: new new

	input :breakfastfood
	rule: f*d
	output:fastfood
*/
char* my_find(char  input[],   char rule[]){


}

int main(){
	char input[] = "abcadefg";
	char rule[] = "a?c";
	char result[] = my_find(input,rule);
	return 0;
}