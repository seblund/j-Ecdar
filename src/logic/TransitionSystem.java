package logic;

import lib.DBMLib;
import models.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// parent class for all TS's, so we can use it with regular TS's, composed TS's etc.
public abstract class TransitionSystem {
		private List<Component> machines;
		private List<Clock> clocks;
		private int dbmSize;

		TransitionSystem(List<Component> machines) {
				this.machines = machines;
				this.clocks = new ArrayList<>();
				for (Component machine : machines) {
						clocks.addAll(machine.getClocks());
				}
				dbmSize = clocks.size() + 1;

				String fileName = "src/" + System.mapLibraryName("DBM");
				File lib = new File(fileName);
				System.load(lib.getAbsolutePath());
		}

		int getDbmSize() {
				return dbmSize;
		}

		public List<Clock> getClocks() {
				return clocks;
		}

		public State getInitialState() {
				List<Location> initialLocations = new ArrayList<>();

				for (Component machine : machines) {
						Location init = machine.getInitLoc();
						initialLocations.add(init);
				}

				int[] zone = initializeDBM();
				State state = new State(initialLocations, zone);
				state.applyInvariants(clocks);
				state.delay();

				return state;
		}

		List<StateTransition> addNewStateTransitions(State currentState, List<List<Location>> locationsArr, List<List<Transition>> transitionsArr) {
				List<StateTransition> stateTransitions = new ArrayList<>();

				// loop through all sets of locations and transitions
				for (int n = 0; n < locationsArr.size(); n++) {
						List<Location> newLocations = locationsArr.get(n);
						List<Transition> transitions = (transitionsArr.get(n) == null) ? new ArrayList<>() : transitionsArr.get(n);
						List<Guard> guards = new ArrayList<>();
						List<Update> updates = new ArrayList<>();
						// gather all the guards and resets of one set of transitions
						for (Transition t : transitions) {
								if (t != null) {
										guards.addAll(t.getGuards());
										updates.addAll(t.getUpdates());
								}
						}

						// build the target state given the set of locations
						State state = new State(newLocations, currentState.getZone());
						// get the new zone by applying guards and resets on the zone of the target state
						if (!guards.isEmpty()) state.applyGuards(guards, clocks);
						if (!updates.isEmpty()) state.applyResets(updates, clocks);

						// if the zone is valid, build the transition and add it to the list
						if (isDbmValid(state.getZone())) {
								StateTransition stateTransition = new StateTransition(currentState, state, transitions);
								stateTransitions.add(stateTransition);
						}
				}

				return stateTransitions;
		}

		public abstract Set<Channel> getInputs();

		public abstract Set<Channel> getOutputs();

		public abstract Set<Channel> getSyncs();

		public abstract List<StateTransition> getNextTransitions(State currentState, Channel channel);

		int[] initializeDBM() {
				// we need a DBM of size n*n, where n is the number of clocks (x0, x1, x2, ... , xn)
				// clocks x1 to xn are clocks derived from our components, while x0 is a reference clock needed by the library
				// initially dbm is an array of 0's, which is what we need
				int[] dbm = new int[dbmSize*dbmSize];
				dbm = DBMLib.dbm_init(dbm, dbmSize);
				return dbm;
		}

		boolean isDbmValid(int[] dbm) {
				return DBMLib.dbm_isValid(dbm, dbmSize);
		}
}