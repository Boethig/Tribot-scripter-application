package scripts.AASkeletons;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSTile;

import scripts.webwalker_logic.WebWalker;

public class SpreadBones extends Node {
	
	public SpreadBones(ACamera aCamera) {
		super(aCamera);
		// TODO Auto-generated constructor stub
	}

	public int BONES;

	@Override
	public boolean validate() {
		// We are in the combat area
		if (Vars.area.contains(Player.getPosition())) {
			for(RSTile tile: Vars.area.getAllTiles()) {
				RSGroundItem[] item = GroundItems.getAll(new Filter<RSGroundItem>() {

					public boolean accept(RSGroundItem item) {
						return item.getPosition().equals(tile) && item.getID() == BONES;
					};				
				});
				// 5+ Bones are on a single tile
				if (item.length >= 5) {
					Vars.bone_Tile = tile;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void execute() {
		// Pick up a random bone from the pile
		RSGroundItem[] bones = GroundItems.getAt(Vars.bone_Tile);
		if (bones.length > 0) {
			RSGroundItem bone = bones[General.random(0, bones.length-1)];
			if (bone.click("Take")) {
				Timing.waitCondition(new Condition() {

					public boolean active() {
						return Inventory.getCount(BONES) > 0;
					}					
				}, General.random(1000, 2000));
				
				// Place the bone in another pile
				dropBones();
			}
		}
	}

	@Override
	public String status() {
		return Vars.stringStatus = "Spreading Bones";
	}
	
	public void dropBones() {
		RSTile dropTile = null;
		for (RSTile tile: Vars.area.getAllTiles()) {
			RSGroundItem[] items = GroundItems.getAt(tile);
			if (items.length <= 3) {
				dropTile = tile;
				break;
			}
		}
		if (dropTile != null) {
			WebWalker.walkTo(dropTile);
			RSItem[] bone = Inventory.find(BONES);
			if (bone.length > 0) {
				if (bone[0].click("Drop")) {
					Timing.waitCondition(new Condition() {

						public boolean active() {
							return Inventory.getCount(BONES) == 0;
						}					
					}, General.random(1000, 2000));
				}
			}
		}
		
	}

}
