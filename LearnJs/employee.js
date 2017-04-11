//对表单提交进行处理

function process(){		//将process赋给表单的onsubmit监听

	var firstName = document.getElementById("firstName").value;
	var lastName = document.getElementById("lastName").value;
	var department = document.getElementById("department").value;

	var output = document.getElementById("output");

	var employee = {
		firstName = this.firstName,
		lastName = this.lastName,
		department = this.department,
		hireDate = new Date()
	}
	
	var message = '<h2>Employee Added</h2>Name:<br>'+ 'Name:'
		employee.lastName + ' ' + employee.firstName + '<br>';
	message += 'Department:' + employee.department + '<br>';
	message += 'HireDate:' + employee.hireDate.toDateString();

	output.innerHTML = message;

	return false;
}

function init(){
	document.getElementById("theForm").onsubmit = process;
}

window.onload = init;    //事件监听


/*
$("#id").innerHTML 和$("#id").checked之类的写法都是错误的，
可以用$("#id").html()和$("#id").attr("checked")之类的 jQuery方法来代替。
*/