package scripts.POHplanks.antiban;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Clickable;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.util.abc.ABCProperties;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api.util.abc.preferences.OpenBankPreference;
import org.tribot.api.util.abc.preferences.WalkingPreference;
import org.tribot.api2007.*;
import scripts.POHplanks.data.Vars;

/**
 * The AntiBan class provides an easy way to implement Anti-ban Compliance 2.0
 * into any script.
 *
 * @author Adapted from Starfox
 *
 * @Version 4.0 (1/24/2016 3:37 PM GTM+0)
 */
public final class Antiban {
 
    /**
     * The object that stores the seeds.
     */
    private static ABCUtil abc;
 
    /**
     * The bool flag that determines whether or not debug information is
     * printed.
     */
    private static boolean print_debug;
 
    /**
     * The amount of resources you have won.
     */
    private static int resources_won;
 
    /**
     * The amount of resources you have lost.
     */
    private static int resources_lost;
 
    /**
     * The % run energy to activate run at.
     */
    private static int run_at;
 
    /**
     * The % hp to eat food at.
     */
    private static int eat_at;
 
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
        resources_won = 0;
        resources_lost = 0;
        run_at = abc.generateRunActivation();
        eat_at = abc.generateEatAtHP();
        should_hover = abc.shouldHover();
        should_open_menu = abc.shouldOpenMenu() && abc.shouldHover();
        last_under_attack_time = 0;
        enableReactionSleep = true;
        General.useAntiBanCompliance(false);
    }
 
    /**
     * Prevent instantiation of this class.
     */
    private Antiban() {
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
    public static int getRunAt() {
        return run_at;
    }
 
    /**
     * Gets the % hp to eat at
     *
     * @return The hitpoints % to eat at
     */
    public static int getEatAt() {
        return eat_at;
    }
 
    /**
     * Gets the bool to hover or not a certain entity
     *
     * @return True if should hover, false otherwise
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
     * Gets the amount of resources won.
     *
     * @Return The amount of resources won.
     */
    public static int getResourcesWon() {
        return resources_won;
    }
 
    /**
     * Gets the amount of resources lost.
     *
     * @Return The amount of recourses lost.
     */
    public static int getResourcesLost() {
        return resources_lost;
    }
 
    /**
     * Sets the amount of resources won to the specified amount.
     *
     * @param amount The amount to set.
     */
    public static void setResourcesWon(int amount) {
        resources_won = amount;
    }
 
    /**
     * Sets the amount of resources lost to the specified amount.
     *
     * @param amount The amount to set.
     */
    public static void setResourcesLost(int amount) {
        resources_lost = amount;
    }
 
    /**
     * Increments the amount of resources won by 1.
     */
    public static void incrementResourcesWon() {
        resources_won++;
    }
 
    /**
     * Increments the amount of resources lost by 1.
     */
    public static void incrementResourcesLost() {
        resources_lost++;
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
        final int reaction_time = (int) (getReactionTime() / 4 * Vars.get().abcReactionPercentage);
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
     */
    public static void generateTrackers(int estimated_wait) {
        final ABCProperties properties = getProperties();
 
        properties.setHovering(should_hover);
        properties.setMenuOpen(should_open_menu);
        properties.setWaitingFixed(false);
        properties.setWaitingTime(estimated_wait);
 
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
 
    /**
     * Checks the exp of the skill being trained. Happens only if the time
     * tracker for checking exp is ready.
     *
     * @Return True if the exp was checked, false otherwise.
     */
    public static boolean checkXp() {
        if (getABCUtil().shouldCheckXP()) {
            if (print_debug) {
                debug("Checked xp");
            }
            getABCUtil().checkXP();
            return true;
        }
        return false;
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
     * Opens up a game tab. Happens only if the time tracker for tab checking is
     * ready.
     *
     * @Return True if the combat tab was checked, false otherwise.
     */
    public static boolean checkTabs() {
        if (getABCUtil().shouldCheckTabs()) {
            if (print_debug) {
                debug("Tab checked");
            }
            getABCUtil().checkTabs();

        }
        return false;
    }

    /**
     * Switches to Game Tab by generated trackers
     */
    public static boolean switchTab(GameTab.TABS TAB) {
        if (!GameTab.open(TAB)) {
            switch (getABCUtil().generateTabSwitchPreference()) {
                case F_KEY:
                    Keyboard.pressKeys(TAB.f_key);
                    debug("Switched tabs via F-keys");
                    return true;
                case MOUSE:
                    debug("Switched tabs via gameTabs");
                    return GameTab.open(TAB);
                default:
                    break;
            }
        }
        return false;
    }

    public static void mouseWheelScroll() {
        if (Options.isMouseScrollZoomEnabled()) {
            if (!Interfaces.isInterfaceSubstantiated(329)) {
                if (getABCUtil().shouldPickupMouse() || getABCUtil().shouldRotateCamera()) {
                    if (General.randomBoolean()) {
                        debug("Scrolling camera Out");
                        Mouse.scroll(false, General.random(1, 6));
                    }
                    else {
                        debug("Scrolling camera In");
                        Mouse.scroll(true, General.random(0, 4));
                    }
                }
            }
        }
        else {
            switchTab(GameTab.TABS.OPTIONS);
            Options.setMouseScrollZoomEnabled(true);
        }
    }
 
    /**
     * Checks all of the actions that are perform with the time tracker; if any
     * are ready, they will be performed.
     */
    public static void timedActions() {
    	checkTabs();
    	mouseWheelScroll();
    	checkXp();
        examineEntity();
    	moveCamera();
        pickUpMouse();
        rightClick();
        leaveGame();
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
     * Activates run. No action is taken if run is already enabled or the
     * current run energy is less than the value returned by ABCUtil.
     *
     * @Return True if run was enabled, false otherwise.
     */
    public static boolean activateRun() {
        if (Game.getRunEnergy() >= run_at && !Game.isRunOn()) {
            Options.setRunEnabled(true);
            if (Options.setRunEnabled(true)) {
                if (print_debug) {
                    debug("Turned run on at " + run_at + "%");
                }
                run_at = getABCUtil().generateRunActivation();
                return true;
            }
        }
        return false;
    }
 
    /**
     * Eats/drinks an item in your inventory with the specified name if your
     * current HP percent is less than or equal to the value generated by
     * ABCUtil. Note that if there is any delay/lag that is longer than 3000
     * milliseconds between the time the food/drink was clicked and when your
     * players HP is changed the tracker will not be reset and you will have to
     * reset it manually.
     *
     * @param option
     *            The option to click the food/drink with (this is normally
     *            "Eat" or "Drink"). Input an empty string to have the method
     *            attempt to find the correct option automatically. Note that
     *            this is not guaranteed to execute properly if an empty string
     *            is inputted.
     * @param name
     *            The name of the food or drink.
     * @Return True if the food/drink was successfully eaten/drank, false
     *         otherwise.
     * @see #(java.lang.String, org.tribot.api2007.types.RSItem)
     */
    public static boolean eat(String option, final String name) {
        return eat(option, Inventory.getCount(name));
    }
 
    /**
     * Eats/drinks an item in your inventory with the specified ID if your
     * current HP percent is less than or equal to the value generated by
     * ABCUtil. Note that if there is any delay/lag that is longer than 3000
     * milliseconds between the time the food/drink was clicked and when your
     * players HP is changed the tracker will not be reset and you will have to
     * reset it manually.
     *
     * @param option
     *            The option to click the food/drink with (this is normally
     *            "Eat" or "Drink"). Input an empty string to have the method
     *            attempt to find the correct option automatically. Note that
     *            this is not guaranteed to execute properly if an empty string
     *            is inputted.
     * @param id
     *            The ID of the food or drink.
     * @Return True if the food/drink was successfully eaten/drank, false
     *         otherwise.
     * @see #(java.lang.String, org.tribot.api2007.types.RSItem)
     */
    public static boolean eat(String option, final int id) {
        return eat(option, Inventory.getCount(id));
    }
 
    /**
     * Checks to see if the player should switch resources. Note that this
     * method will only return correctly if you have been tracking the resources
     * you have won and lost. Note also that you must create the check time in
     * your script and reset it accordingly. e.g. to check if you should switch
     * resources, you should check the following condition:
     * <code>Timing.currentTimeMillis() >= check_time && AntiBan.shouldSwitchResources()</code>
     *
     * @param player_count
     *            The amount of players gathering resources near you.
     * @Return True if your player should switch resources, false otherwise.
     */
    public static boolean shouldSwitchResources(int player_count) {
        double win_percent = ((double) (resources_won + resources_lost) / (double) resources_won);
        return win_percent < 50.0 && getABCUtil().shouldSwitchResources(player_count);
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
     * Returns the bank preference of the player's profile. BANKER or BOOTH
     *
     * @return OpenBankPreference Banking preference
     */
    public static OpenBankPreference generateOpenBankPreference() {
        return getABCUtil().generateOpenBankPreference();
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