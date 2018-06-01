package scripts.POHplanks;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.Clicking;
import org.tribot.api.interfaces.Clickable;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.WalkingPreference;
import org.tribot.api2007.Combat;
import org.tribot.api2007.Inventory;



public class Antiban {

	private static ABCUtil abc;
	 
    /**
     * The bool flag that determines whether or not debug information is
     * printed.
     */
    private static boolean print_debug;
    /**
     * The bool that determines whether or not we should be hovering.
     */
    private static boolean should_hover;
 
    /**
     * The bool that determines whether or not we should be opening the menu.
     */
    private static boolean should_open_menu;
 
    /**
     * The time stamp at which we were last under attack.
     */
    private static long last_under_attack_time;
 
    /**
     * The bool that determines whether or not we should reaction sleep
     */
    private static boolean enableReactionSleep;
 
    /**
     * Static initialization block. By default, the use of general anti-ban
     * compliance is set to be true.
     */
    static {
        abc = new ABCUtil();
        print_debug = true;
        should_hover = abc.shouldHover();
        should_open_menu = abc.shouldOpenMenu() && abc.shouldHover();
        last_under_attack_time = 0;
        enableReactionSleep = true;
        General.useAntiBanCompliance(true);
    }
 
    /**
     * Prevent instantiation of this class.
     */
    Antiban() {
    }
 
    /**
     * Destroys the current instance of ABCUtil and stops all anti-ban threads.
     * Call this at the end of your script.
     */
    public static void destroy() {
        abc.close();
        abc = null;
    }
 
    /**
     * Creates a new instance of ABCUtil and sets the instance to be equal to
     * the current ABCUtil.
     */
    public static void create() {
        abc = new ABCUtil();
    }
 
    /**
     * Gets the ABCUtil object.
     *
     * @Return The ABCUtil object.
     */
    public static ABCUtil getABCUtil() {
        return abc;
    }
 
    /**
     * Gets the energy % to run at
     *
     * @return The energy % to run at
     */
 
    public static boolean getShouldHover() {
        return should_hover;
    }
 
    /**
     * Gets the bool should open menu
     *
     * @return True if should open menu, false otherwise
     */
    public static boolean getShouldOpenMenu() {
        return should_open_menu;
    }
 
    /**
     * Gets the last time the character was under attack, in milliseconds
     *
     * @return The last time under attack
     */
    public static long getLastUnderAttackTime() {
        return last_under_attack_time;
    }
 
    /**
     * Gets the properties for ABCUtil.
     *
     * @Return The properties.
     */
    public static ABCProperties getProperties() {
        return getABCUtil().getProperties();
    }
 
    /**
     * Gets the waiting time for the next action we want to perform.
     *
     * @Return The waiting time.
     */
    public static int getWaitingTime() {
        return getProperties().getWaitingTime();
    }
 
    /**
     * Gets the reaction time that we should sleep for before performing our
     * next action. Examples:
     * <ul>
     * <li>Reacting to when our character stops fishing. The response time will
     * be used before we move on to the next fishing spot, or before we walk to
     * the bank.</li>
     * <li>Reacting to when our character stops mining. The response time will
     * be used before we move on to the next rock, or before we walk to the
     * bank.</li>
     * <li>Reacting to when our character kills our target NPC. The response
     * time will be used before we attack our next target, or before we walk to
     * the bank.</li>
     * </ul>
     *
     * @Return The reaction time.
     */
    public static int getReactionTime() {
    	resetShouldHover();
        resetShouldOpenMenu();

        ABCProperties properties = getProperties();

        properties.setWaitingTime(getWaitingTime());
        properties.setHovering(should_hover);
        properties.setMenuOpen(should_open_menu);
        properties.setUnderAttack(Combat.isUnderAttack() || (Timing.currentTimeMillis() - last_under_attack_time < 2000));
        properties.setWaitingFixed(false);

        return getABCUtil().generateReactionTime();
    }
 
    /**
     * Sets the print_debug bool to be equal to the specified bool. By calling
     * this method and providing a true value, other methods in this class will
     * start printing debug information into the system print stream when they
     * are executed.
     *
     * @param state The bool to set.
     */
    public static void setPrintDebug(boolean state) {
        print_debug = state;
    }
    /**
     * Sets the last_under_attack_time to be equal to the specified time stamp.
     *
     * @param time_stamp The time stamp.
     */
    public static void setLastUnderAttackTime(long time_stamp) {
        last_under_attack_time = time_stamp;
    }
 
    /**
     * Sleeps for the reaction time generated by ABCUtil. Note that this method
     * uses a special sleeping method from ABCUtil that allows the ABC2
     * background thread to interrupt the sleep when needed.
     */
    public static void sleepReactionTime() {
        if (!enableReactionSleep)
            return;
        final int reaction_time = (getReactionTime()/7);
        if (print_debug) {
            debug("Reaction time: " + reaction_time + "ms.");
        }
        try {
            getABCUtil().sleep(reaction_time);
        } catch (InterruptedException e) {
            debug("Background thread interrupted sleep");
        }
    }
 
    /**
     * Generates the trackers for ABCUtil. Call this only after successfully
     * completing an action that has a dynamic wait time for the next action.
     *
     * @param estimated_wait
     *            The estimated wait time (in milliseconds) before the next
     *            action occurs.
     * @param fixed_wait
     *            True if estimated wait is fixed, false otherwise
     */
    public static void generateTrackers(int estimated_wait, boolean fixed_wait) {
        final ABCProperties properties = getProperties();
 
        properties.setHovering(should_hover);
        properties.setMenuOpen(should_open_menu);
        properties.setWaitingTime(estimated_wait);
        properties.setWaitingFixed(fixed_wait);
 
        getABCUtil().generateTrackers();
    }
 
    /**
     * Resets the should_hover bool to match the ABCUtil value. This method
     * should be called after successfully clicking an entity.
     */
    public static void resetShouldHover() {
        should_hover = getABCUtil().shouldHover();
    }
 
    /**
     * Resets the should_open_menu bool to match the ABCUtil value. This method
     * should be called after successfully clicking an entity.
     */
    public static void resetShouldOpenMenu() {
        should_open_menu = getABCUtil().shouldOpenMenu() && getABCUtil().shouldHover();
    }
 
    /**
     * Randomly moves the camera. Happens only if the time tracker for camera
     * movement is ready.
     *
     * @Return True if the action was performed, false otherwise.
     */
    public static boolean moveCamera() {
        if (getABCUtil().shouldRotateCamera()) {
            if (print_debug) {
                debug("Rotated camera");
            }
            getABCUtil().rotateCamera();
            return true;
        }
        return false;
    }
 
    public static void randomBankAfk() {
    	int a = General.random(1, 25);
    	if (a == 7) {
    		debug("Leaving screen, afking 1.5-3.2 seconds");
    		leaveGame();
    		General.sleep(1479, 3252);
    	}
    	if (a == 12) {
    		debug("rotating camera and sleeping 1.5-1.9 seconds");
    		moveCamera();
    		General.sleep(1501, 1912);
    	}
    	if(a == 5){
    		int b = General.random(1,2);
    		if(b == 1){
    			debug("opening inventory");
    			Inventory.open();
    		}
    		else {
    			debug("examining entitys");
    			examineEntity();
    		}
    	}
    	if(a == 6)
    	{	
    		debug("Picking up mouse and sleeping 2.3-4.2 seconds");
    		pickUpMouse();
    		General.sleep(2321,4232);
    	}
    }
    
    public static void randomMovement() {
    	int a = General.random(1, 50);
    	if (a == 2) {
    		debug("Picked up mouse");
    		pickUpMouse();
    	}
    	if (a == 9) {
    		debug("Picked up & Mouse moved");
    		pickUpMouse();
    		General.sleep(General.random(300,500));
    		mouseMovement();
    	}
    	if (a == 16) {
    		debug("Mouse moved");
    		mouseMovement();
    	}
    	if (a == 23) {
    		debug("Mouse moved x2");
    		mouseMovement();
    		General.sleep(General.random(300,600));
    		mouseMovement();
    	}
    	if(a == 25){
    		moveCamera();
    	}
    }
    
    /**
     * Picks up the mouse. Happens only if the time tracker for picking up the
     * mouse is ready.
     *
     * @Return True if the mouse was picked up, false otherwise.
     */
    public static boolean pickUpMouse() {
        if (getABCUtil().shouldPickupMouse()) {
            if (print_debug) {
                debug("Picked up mouse");
            }
            getABCUtil().pickupMouse();
            return true;
        }
        return false;
    }
 
    /**
     * Navigates the mouse off game window and mimics de-focusing the window.
     * Happens only if the time tracker for leaving the game is ready.
     *
     * @Return True if the mouse left the game window, false otherwise.
     */
    public static boolean leaveGame() {
        if (getABCUtil().shouldLeaveGame()) {
            if (print_debug) {
                debug("Left game window");
            }
            getABCUtil().leaveGame();
            return true;
        }
        return false;
    }
 
    /**
     * Examines an entity near your player. Happens only if the time tracker for
     * examining an entity is ready.
     *
     * @Return True if an entity was examined, false otherwise.
     */
    public static boolean examineEntity() {
        if (getABCUtil().shouldExamineEntity()) {
            if (print_debug) {
                debug("Examined entity");
            }
            getABCUtil().examineEntity();
            return true;
        }
        return false;
    }
 
    /**
     * Right clicks the mouse. Happens only if the time tracker for right
     * clicking the mouse is ready.
     *
     * @Return True if a random spot was right clicked, false otherwise.
     */
    public static boolean rightClick() {
        if (getABCUtil().shouldRightClick()) {
            if (print_debug) {
                debug("Right clicked");
            }
            getABCUtil().rightClick();
            return true;
        }
        return false;
    }
 
    /**
     * Moves the mouse. Happens only if the time tracker for moving the mouse is
     * ready.
     *
     * @Return True if the mouse was moved to a random point, false otherwise.
     */
    public static boolean mouseMovement() {
        if (getABCUtil().shouldMoveMouse()) {
            if (print_debug) {
                debug("Mouse moved");
            }
            getABCUtil().moveMouse();
            return true;
        }
        return false;
    } 
 
    /**
     * Checks all of the actions that are perform with the time tracker; if any
     * are ready, they will be performed.
     */
    public static void timedActions() {
        moveCamera();
        mouseMovement();
        rightClick();
        pickUpMouse();
        mouseMovement();
    }
    /**
     * Gets the next target that should be interacted with from the specified
     * list of targets.
     *
     * @param rsnpc
     *            The targets to choose from.
     * @param <T>
     *            The generic type.
     * @Return The target to interact with.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Positionable> T selectNextTarget(Positionable[] rsnpc) {
        return (T) getABCUtil().selectNextTarget(rsnpc);
    }
 
    /**
     * Sleeps the current thread for the item interaction delay time. This
     * method should be called directly after interacting with an item in your
     * players inventory.
     */
    public static void waitItemInteractionDelay() {
        General.sleep(25, 75);
    }
 
    /**
     * Sleeps the current thread for the item interaction delay time multiplied
     * by the specified number of iterations. This method can be used to sleep
     * between certain actions that do not have a designated method already
     * assigned to them such as casting spells or clicking interfaces.
     * <p/>
     * This method does not guarantee a static sleep time each iteration.
     *
     * @param iterations
     *            How many times to sleep the item interaction delay time.
     * @see #waitItemInteractionDelay()
     */
    public static final void waitItemInteractionDelay(int iterations) {
        for (int i = 0; i < iterations; i++) {
            waitItemInteractionDelay();
        }
    }
 
    /**
     * Hovers the next available object if applicable.
     *
     * Note that you <i>must</i> reset the tracker yourself after the current
     * Object interaction is finished.
     
    public static boolean hoverObject(RSObject object) {
        if (!should_hover)
            return false;
        if (!Entities07.isHovering(object.getModel()) && Clicking.hover(object)) {
            if (print_debug) {
                debug("Hovering object");
            }
            return Timing07.waitCondition(() -> Entities07.isHovering(object.getModel()), General.random(2000, 3000));
        }
        return false;
    }
    */
 
    /**
     * Hovers the entity if applicable.
     *
     * Note that you <i>must</i> reset the tracker yourself after the current
     * Object interaction is finished.
     */
    public static boolean hoverEntity(Clickable[] b) {
        if (should_hover) {
            if (print_debug) {
                debug("Hovering entity");
            }
            Clicking.hover(b);
            return true;
        }
        return false;
    }
 
    /**
     * Hovers the next available NPC if applicable.
     *
     * Note that you <i>must</i> reset the tracker yourself after the current
     * NPC interaction is finished. */
 /*    
    public static void hoverNextNPC() {
        if (!should_hover)
            return;
 
        final RSCharacter interacting = Player.getRSPlayer().getInteractingCharacter();
 
        if (interacting != null) {
            final String name = interacting.getName();
            if (name != null) { 
                final RSNPC[] next = NPCs.getAll(new Filter<RSNPC>() {
                    @Override
                    public boolean accept(RSNPC npc) {
                        if (npc == null)
                            return false;
                        RSNPCDefinition def = npc.getDefinition();
                        if (def == null)
                            return false;
                        String def_name = def.getName();
                        if (def_name == null || !def_name.equals(name))
                            return false;
                        return npc.isOnScreen() && npc.isClickable() && !npc.getPosition().equals(interacting.getPosition());
                    }
                });
                if (next != null)
                	if (Mouse.isInBounds() && Clicking.hover(next)) 
                	Timing.waitCondition(Clicking.hover(next), 500);
               //     if (!Entities07.isHovering(next.getModel()) && Clicking.hover(next))
               //         Timing07.waitCondition(()  Entities.isHovering(next.getModel()), 500);
            }
        }
    } */
 
    /**
     * Enable or disable reaction sleeps
     *
     * @param state
     *            The new state
     */
    public static void setEnableReactionSleep(boolean state) {
        enableReactionSleep = state;
    }
 
    /**
     * Returns the walking preference of the player's profile. SCREEN or MINIMAP
     * or both
     *
     * @return WalkingPreference Walking preference
     */
    public static WalkingPreference generateWalkingPreference(int distance) {
        return getABCUtil().generateWalkingPreference(distance);
    }
 
 
    /**
     * Sends the specified message to the system print stream with the [ABC2]
     * tag.
     *
     * @param message
     *            The message to print.
     */
    private static void debug(Object message) {
        General.println("[ABC2] " + message);
    }

}
