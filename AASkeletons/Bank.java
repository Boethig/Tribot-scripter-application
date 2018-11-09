package scripts.AASkeletons;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;

public class Bank extends Node {

	public Bank(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean validate() {
		return Banking.isInBank();
	}

	@Override
	public void execute() {
		if (Banking.isBankScreenOpen()) {
			//Bank
		} else {
			if (Banking.openBank()) {
				Timing.waitCondition(new Condition() {
					public boolean active() {
						return Banking.isBankScreenOpen();
					}
				}, General.random(2000,3000));
			}
		}
		
	}

	@Override
	public String status() {
		return Vars.stringStatus = "Banking";
	}

}
