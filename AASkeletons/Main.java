package scripts.AASkeletons;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import org.tribot.api.General;
import org.tribot.script.Script;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

public class Main extends Script implements Painting, MessageListening07  {
		public ArrayList<Node> nodes = new ArrayList<Node>();
		private ACamera aCamera = new ACamera(this);
				
		@Override
		public void onPaint(Graphics arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void run() {
			Collections.addAll(nodes,new Attack(aCamera), new Bank(aCamera), new SpreadBones(aCamera), new WalktoBank(aCamera), new WalktoSkeletons(aCamera));
			while(!Vars.script) {
				for(final Node node: nodes) {
					if(node.validate()) {
						node.execute();
						}
					}
				General.sleep(50,70);
				}
			}
		
		
		@Override
		public void clanMessageReceived(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void duelRequestReceived(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void personalMessageReceived(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void playerMessageReceived(String arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void serverMessageReceived(String arg0) {
			if (arg0.equals("That was your last one!")) {
				Vars.noChinsLeft = true;
			}
		}
		@Override
		public void tradeRequestReceived(String arg0) {
			// TODO Auto-generated method stub
			
		}		
}
