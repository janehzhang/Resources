


遍历map
Iterator i = map.entrySet().iterator();
while(i.hasNext()){
	Map.Entry entry = (Map.Entry)i.next();
	int count = (Integer) entry.getValue();
}