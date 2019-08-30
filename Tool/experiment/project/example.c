void main (){

	boolean x = true;
	boolean y = false;
	boolean z = true;
	
	
	if (x && y || z)
		x = false;
	else
		z = false;
		
		
	if (x && y || z){
		x = false;
	} else {
		y = false;
	}
}

