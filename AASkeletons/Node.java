package scripts.AASkeletons;

public abstract class Node {
	
	protected ACamera aCamera;
	
	public Node(ACamera aCamera) 
	{       
	this.aCamera = aCamera;
	}

public abstract boolean validate();

public abstract void execute();

public abstract String status();

}
