package scripts.POHplanks;

import javax.swing.JOptionPane;

public class Enums {

	enum Logs 
	{
		REGULAR, OAK, TEAK, MAHOGANY
	}
	enum Teleports
	{
		LUMBRIDGE, CAMELOT
	}
	public static void main()
	{
		Logs[] options = {Logs.REGULAR, Logs.OAK,Logs.TEAK,Logs.MAHOGANY};
		Logs select =(Logs)JOptionPane.showInputDialog(null,"Select the type of logs", "Log Type", JOptionPane.QUESTION_MESSAGE, null, options, options[3]);
	if(select != null)
	{
	switch(select){
	case REGULAR:
		Vars.LOGS = Vars.reg_log;
		Vars.PLANK = Vars.reg_plank;
		Vars.LOG_NAME = "Logs";
		break;
	case OAK:
		Vars.LOGS = Vars.oak_log;
		Vars.PLANK = Vars.oak_plank;
		Vars.LOG_NAME = "Oak logs";
		break;
	case TEAK:
		Vars.LOGS = Vars.teak_log;
		Vars.PLANK = Vars.teak_plank;
		Vars.LOG_NAME = "Teak logs";
		break;
	case MAHOGANY:
		Vars.LOGS = Vars.mahog_log;
		Vars.PLANK = Vars.mahog_plank;
		Vars.LOG_NAME = "Mahogany logs";
		break;
		}
	}
	
	Teleports[] teles = {Teleports.CAMELOT, Teleports.LUMBRIDGE};
	Teleports teleport =(Teleports)JOptionPane.showInputDialog(null, "Chose the banking method", "Teleports", JOptionPane.QUESTION_MESSAGE, null, teles, teles[1]);
	if(teleport != null){	
	switch(teleport)
	{
	case CAMELOT:
		Vars.TeleLoc=Area_Tiles.Cammy;
		Vars.TELEPORT="Camelot Teleport";
		break;
	case LUMBRIDGE:
		Vars.TeleLoc=Area_Tiles.Lumbridge;
		Vars.TELEPORT="Lumbridge Teleport";
		break;
		}
	}
	
	}

}
