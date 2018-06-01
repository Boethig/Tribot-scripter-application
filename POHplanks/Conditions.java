package scripts.POHplanks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.Player;

public class Conditions {

	public static Condition Full() {
		return new Condition() {

			@Override
			public boolean active() {
				 General.sleep(100,200);
				return Inventory.isFull();
			}
		};
	}

	public static Condition bankOpen(){
		return new Condition() {

			@Override
			public boolean active() {
				 General.sleep(100,200);
				return Banking.isBankScreenOpen();
			}
			
		};
	}
	public static Condition Banked(){
		return new Condition() {

			@Override
			public boolean active() {
				 General.sleep(100,200);
				return Inventory.getCount(Vars.PLANK)== 0;
			}
			
		};
	}

	public static Condition SelectSpell(){
		return new Condition(){

			@Override
			public boolean active() {
				 General.sleep(100,200);
				return Magic.isSpellSelected();
			}
			
		};
	}
	public static Condition Clicked(){
		return new Condition(){

			@Override
			public boolean active() {
				 General.sleep(100,200);
				return Clicking.click();
			}
			
		};
	}
public static Condition ClickedInt(){
		return new Condition(){

			@Override
			public boolean active() {
				 General.sleep(100,200);
				return Interfaces.isInterfaceValid(370);
			}
			
		};
	}
public static Condition ClickedOptions(){
	return new Condition(){

		@Override
		public boolean active() {
			 General.sleep(100,200);
			return NPCChat.getClickContinueInterface() != null;
		}
		
	};
}
public static Condition ClickedContinue(){
	return new Condition(){

		@Override
		public boolean active() {
			 General.sleep(100,200);
			return NPCChat.getClickContinueInterface() == null;
		}
		
	};
	
}
public static Condition atBank(){
	return new Condition(){

		@Override
		public boolean active() {
			 General.sleep(100,200);
			return Vars.TeleLoc.contains(Player.getPosition());
		}
		
	};
	
}


}
