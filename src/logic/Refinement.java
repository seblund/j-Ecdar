package logic;

import models.*;

import java.util.*;
import java.util.stream.Collectors;

public class Refinement {
    private final TransitionSystem ts1, ts2;
    private final List<Clock> allClocks;
    private final List<BoolVar> allBVs;

    private final Map<LocationPair, StatePair> passed;
    private final Deque<StatePair> waiting;

    private final Set<Channel> inputs1, inputs2, outputs1, outputs2;
    private GraphNode refGraph;
    private GraphNode currNode;
    private GraphNode supersetNode;
    private HashMap<Clock,Integer> maxBounds;
    private static boolean RET_REF = false;
    public static int NODE_ID = 0;
    private StringBuilder errMsg = new StringBuilder();

    public Refinement(TransitionSystem system1, TransitionSystem system2) {
        this.ts1 = system1;
        this.ts2 = system2;
        this.waiting = new ArrayDeque<>();
        this.passed = new HashMap<>();

        allClocks = new ArrayList<>(ts1.getClocks());
        allClocks.addAll(ts2.getClocks());
        allBVs= new ArrayList<>(ts1.getBVs());
        allBVs.addAll(ts2.getBVs());


        inputs1 = ts1.getInputs();
        inputs2 = ts2.getInputs();

        outputs1 = new HashSet<>(ts1.getOutputs());
        outputs1.addAll(ts1.getSyncs());

        outputs2 = new HashSet<>(ts2.getOutputs());
        outputs2.addAll(ts2.getSyncs());

        setMaxBounds();
    }

    public String getErrMsg() {
        return errMsg.toString();
    }

    public boolean check(boolean ret_ref) { // TODO: test this.
        Refinement.NODE_ID = 0;
        Refinement.RET_REF = ret_ref;
        return checkRef();
    }

    public boolean check() {
        Refinement.RET_REF = false;
        return checkRef();
    }

    public GraphNode getTree() {
        return refGraph;
    }

    public boolean checkPreconditions() {
        boolean precondMet = true;

        // check for duplicate automata
        List<SimpleTransitionSystem> leftSystems = ts1.getSystems();
        List<SimpleTransitionSystem> rightSystems = ts2.getSystems();

        for (SimpleTransitionSystem left : leftSystems) {
            for (SimpleTransitionSystem right : rightSystems) {
                if (left.hashCode() == right.hashCode()) {
                    precondMet = false;
                    errMsg.append("Duplicate process instance: ");
                    errMsg.append(left.getName());
                    errMsg.append(".\n");
                }
            }
        }
        // signature check, precondition of refinement: inputs and outputs must be the same on both sides,
        // with the exception that the left side is allowed to have more outputs

        // inputs on the right must contain all inputs on the left side
        if (!inputs2.containsAll(inputs1)) {
            precondMet = false;
            errMsg.append(inputs2 + " <--> " + inputs1 + "\n");
            errMsg.append("Inputs on the left side are not equal to inputs on the right side.\n");
        }

        // the left side must contain all outputs from the right side
        if (!outputs1.containsAll(outputs2)) {
            precondMet = false;
            errMsg.append("Not all outputs of the right side are present on the left side.\n");
        }



        Set<Channel> output1Copy = new HashSet<>(outputs1);
        output1Copy.retainAll(inputs2);
        // the left side must contain all outputs from the right side
        if (!output1Copy.isEmpty()){
            precondMet = false;
            errMsg.append("Alphabet mismatch.\n");
        }

        Set<Channel> output2Copy = new HashSet<>(outputs2);
        output1Copy.retainAll(inputs1);
        // the left side must contain all outputs from the right side
        if (!output1Copy.isEmpty()){
            precondMet = false;
            errMsg.append("Alphabet mismatch.\n");
        }


        if (!ts1.isLeastConsistent()) {
            precondMet = false;
            errMsg.append(ts1.getLastErr());
        }

        if (!ts2.isLeastConsistent()) {
            precondMet = false;
            errMsg.append(ts2.getLastErr());
        }
        return precondMet;
    }

    public boolean checkRef() {
        // one or more of the preconditions failed, so fail refinement
        if (!checkPreconditions())
            return false;

        CDD.init(CDD.maxSize,CDD.cs,CDD.stackSize);
        CDD.addClocks(allClocks);
        CDD.addBddvar(allBVs);

        // the first states we look at are the initial ones
        waiting.push(getInitialStatePair());

        if (RET_REF) {
            refGraph = new GraphNode(waiting.getFirst());
            currNode = refGraph;
        }


        while (!waiting.isEmpty()) {
            StatePair curr = waiting.pop();

            if (RET_REF)
                currNode = curr.getNode();

            State left = curr.getLeft();
            State right = curr.getRight();
            // need to make deep copy
            State newState1 = new State(left);
            State newState2 = new State(right);
            // mark the pair of states as visited
            LocationPair locPair = new LocationPair(left.getLocation(), right.getLocation());
            StatePair pair = new StatePair(newState1, newState2, currNode);

            if (!passed.containsKey(locPair)) {
                for (LocationPair keyPair : passed.keySet())
                    if (keyPair.equals(locPair)) {
                        System.out.println("rest");
                        assert (false);
                    }
            }


            if (passed.containsKey(locPair)) {
                passed.get(locPair).getLeft().disjunctCDD(pair.getLeft().getCDD());
                passed.get(locPair).getRight().disjunctCDD(pair.getRight().getCDD());
            }
            else
                passed.put(locPair,pair);

            // assert(passedContainsStatePair(curr));

            // check that for every delay in TS 1 there is a corresponding delay in TS
            boolean holds0 = checkDelay(left, right);
            if (!holds0) {
                System.out.println("Delay violation");
                CDD.done();
                return false;
            }

            // check that for every output in TS 1 there is a corresponding output in TS 2
            boolean holds1 = checkOutputs(left, right);
            if (!holds1) {

                System.out.println("Output violation");
                CDD.done();
                return false;
            }

            // check that for every input in TS 2 there is a corresponding input in TS 1
            boolean holds2 = checkInputs(left, right);
            if (!holds2) {
                //assert(false); // assuming everything is input enabled
                System.out.println("Input violation");
                CDD.done();
                return false;
            }
        }

        // if we got here it means refinement property holds
        CDD.done();
        return true;
    }

    public int getHashMapTotalSize(Map<LocationPair, List<StatePair>> map){
        int result = 0;

        for (Map.Entry<LocationPair, List<StatePair>> entry : map.entrySet()) {
            result += entry.getValue().size();
        }

        return result;
    }

    private boolean checkDelay(State leftState, State rightState)
    {
        assert (leftState.getCDD().equiv(rightState.getCDD()));
        CDD currentStateCDD = new CDD(leftState.getCDD().getPointer()); //TODO: is the explicit new needed?
        currentStateCDD=currentStateCDD.delay();

        CDD leftPart = currentStateCDD.conjunction(leftState.getInvarCDDDirectlyFromInvariants());
        CDD rightPart = currentStateCDD.conjunction(rightState.getInvarCDDDirectlyFromInvariants());
        if (CDD.isSubset(leftPart,rightPart))
            return true;

        System.out.println("left invariant: " + leftState.getInvarCDDDirectlyFromInvariants());
        System.out.println("right invariant: " + rightState.getInvarCDDDirectlyFromInvariants());
        System.out.println("left : " + leftState);
        System.out.println("right : " + rightState);
        return false;
    }

    private StatePair buildStatePair(Transition leaderTransition, Transition followerTransition) {
        // this function will create the target states for each side.
        // We start off with the guards of the leader transition, and then do forward exploration on the transition
        State leaderTarget = new State(leaderTransition.getTarget().getLocation(), leaderTransition.getGuardCDD());

        //forward exploration starts now

        // check if there is a part of the CDD where both leader and follower are enabled, abort otherwise
        leaderTarget.applyGuards(followerTransition.getGuardCDD());
        if (leaderTarget.getCDD().isFalse()) {
            return null;
        }

        leaderTarget.applyResets(leaderTransition.getUpdates());
        leaderTarget.applyResets(followerTransition.getUpdates());

        // check target invariants to see if transitions are actually enabled
        CDD leaderTargetInvariant = leaderTransition.getTarget().getInvarCDDDirectlyFromInvariants();
        if (leaderTarget.getCDD().conjunction(leaderTargetInvariant).isFalse())
            return null;

        CDD followerTargetInvariant = followerTransition.getTarget().getInvarCDDDirectlyFromInvariants();
        if (leaderTarget.getCDD().conjunction(followerTargetInvariant).isFalse())
            return null;

        // forward explored both transitions, reaching the new target states

        leaderTarget.delay();

        leaderTarget.applyInvariants(leaderTransition.getTarget().getInvarCDDDirectlyFromInvariants());
        leaderTarget.applyInvariants(followerTransition.getTarget().getInvarCDDDirectlyFromInvariants());


        // This line can never be triggered, because the transition will not even get constructed if the invariant breaks it
        // The exact same check will catch it but in TransitionSystem instead
        //if (!leaderTarget.getInvZone().isValid()) return null;

        leaderTarget.extrapolateMaxBounds(maxBounds,allClocks);
        // if ( leaderTarget.getInvarCDD().equiv(CDD.getUnrestrainedCDD()))
        //     assert(false);
        State target2 = new State(followerTransition.getTarget().getLocation(), leaderTarget.getCDD());
        return new StatePair(leaderTarget, target2);
    }

    private boolean createNewStatePairs(List<Transition> trans1, List<Transition> trans2, boolean isInput) {
        boolean pairFound = false;


        List<CDD> gzLeft = trans1.stream().map(Transition::getGuardCDD).collect(Collectors.toList());
        List<CDD> gzRight = trans2.stream().map(Transition::getGuardCDD).collect(Collectors.toList());

        CDD leftCDD = CDD.cddFalse();
        for (CDD c: gzLeft)
            leftCDD = leftCDD.disjunction(c);

        CDD rightCDD = CDD.cddFalse();
        for (CDD c: gzRight)
            rightCDD = rightCDD.disjunction(c);

        // If trans2 does not satisfy all solution of trans1, return empty list which should result in refinement failure
        if (!isInput && leftCDD.minus(rightCDD).isNotFalse()) {
            System.out.println("trans 2 does not satisfiy all solutions of trans 1");
//            System.out.println("trans 2 does not satisfiy all solutions " + trans2.get(0).getEdges().get(0).getChan());
            System.out.println(leftCDD);
            System.out.println(rightCDD);
            System.out.println(leftCDD.minus(rightCDD));
            return false;
        }

        if (isInput && rightCDD.minus(leftCDD).isNotFalse()) {
            System.out.println("trans 2 does not satisfiy all solutions of trans 1");
//            System.out.println("trans 2 does not satisfiy all solutions " + trans2.get(0).getEdges().get(0).getChan());
            System.out.println(leftCDD);
            System.out.println(rightCDD);
            System.out.println(rightCDD.minus(leftCDD));
            return false;
        }

        for (Transition transition1 : trans1) {
            for (Transition transition2 : trans2) {
                StatePair pair = buildStatePair(transition1, transition2);
                if (pair != null) {
                    pairFound = true;

                    if (!pair.getRight().getLocation().getIsUniversal())
                    {
                        if (!waitingContainsStatePair(pair) && !passedContainsStatePair(pair)) {
                            waiting.add(pair);
                            if (RET_REF) {
                                currNode.constructSuccessor(pair, transition1.getEdges(), transition2.getEdges());
                            }
                        } else {
                            if (RET_REF && supersetNode != null && !currNode.equals(supersetNode)) {
                                GraphEdge edge = new GraphEdge(currNode, supersetNode, transition1.getEdges(), transition2.getEdges(), (pair.getLeft().getCDD()));
                                currNode.addSuccessor(edge);
                                supersetNode.addPredecessor(edge);
                            }
                        }
                    }
                }
            }
        }
        return pairFound;
    }

    private boolean checkInputs(State state1, State state2) {
        return checkActions(state1, state2, true);
    }

    private boolean checkOutputs(State state1, State state2) {
        return checkActions(state1, state2, false);
    }

    private boolean checkActions(State state1, State state2, boolean isInput) {
        for (Channel action : (isInput ? inputs2 : outputs1)) {
            List<Transition> leaderTransitions = isInput ? ts2.getNextTransitions(state2, action, allClocks)
                    : ts1.getNextTransitions(state1, action, allClocks);

            if (!leaderTransitions.isEmpty()) {

                List<Transition> followerTransitions;
                Set<Channel> toCheck = isInput ? inputs1 : outputs2;
                if (toCheck.contains(action)) {
                    followerTransitions = isInput ? ts1.getNextTransitions(state1, action, allClocks)
                            : ts2.getNextTransitions(state2, action, allClocks);

                    if (followerTransitions.isEmpty()) {
                        state2.getCDD().printDot();
                        System.out.println("followerTransitions empty");
                        return false;
                    }
                } else {
                    // if action is missing in TS1 (for inputs) or in TS2 (for outputs), add a self loop for that action
                    followerTransitions = new ArrayList<>();
                    if (isInput) {
                        Transition loop = new Transition(state1, state1.getCDD());
                        followerTransitions.add(loop);
                    }
                    else {
                        Transition loop = new Transition(state2, state2.getCDD());
                        followerTransitions.add(loop);
                    }

                }

                //System.out.println("Channel: " + action);
                if(!(isInput ? createNewStatePairs(followerTransitions, leaderTransitions, isInput) : createNewStatePairs(leaderTransitions, followerTransitions, isInput))) {
                    System.out.println(isInput);
                    System.out.println("followerTransitions: " + followerTransitions.size());
                    for (Transition t: followerTransitions)
                        for (Edge e : t.getEdges())
                            System.out.println(e);
                    System.out.println("leaderTransitions: " + leaderTransitions.size());
                    for (Transition t: leaderTransitions)
                        for (Edge e : t.getEdges())
                            System.out.println(e);
                    System.out.println("create pairs failed");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean passedContainsStatePair(StatePair pair) {
        LocationPair locPair = new LocationPair(pair.getLeft().getLocation(), pair.getRight().getLocation());
        if (passed.containsKey(locPair)) {
            if (CDD.isSubset(pair.getRight().getCDD(),passed.get(locPair).getRight().getCDD()))
                return true;
            if (CDD.isSubset(pair.getLeft().getCDD(),passed.get(locPair).getLeft().getCDD()))
                assert(false); // left and right side are supposed to be identical at all times, so this should not be reachable
            /*if (pair.getRight().getCDD().toFederation().isSubset(passed.get(locPair).getRight().getCDD().toFederation()))
            {
                assert(false);
            }*/
        }

        return false;
    }

    private boolean waitingContainsStatePair(StatePair pair) {
        return listContainsStatePair(pair, waiting);
    }

    private boolean listContainsStatePair(StatePair pair, Iterable<StatePair> pairs) {
        State currLeft = pair.getLeft();
        State currRight = pair.getRight();




        for (StatePair state : pairs) {
            // check for zone inclusion
            State passedLeft = state.getLeft();
            State passedRight = state.getRight();

            if (passedLeft.getLocation().equals(currLeft.getLocation()) &&
                    passedRight.getLocation().equals(currRight.getLocation())) {
                if (CDD.isSubset(currLeft.getCDD(),passedLeft.getCDD()) &&
                        CDD.isSubset(currRight.getCDD(),passedRight.getCDD())) {
                    supersetNode = state.getNode();
                    return true;
                }
                if (currRight.getCDD().toFederation().isSubset(passedRight.getCDD().toFederation()))
                {
                    assert(false);
                }
            }
        }

        return false;
    }

    public StatePair getInitialStatePair() {
        State left = ts1.getInitialStateRef( ts2.getInitialLocation().getInvariantCDD());
        State right = ts2.getInitialStateRef(ts1.getInitialLocation().getInvariantCDD());
        return new StatePair(left, right);
    }

    public void setMaxBounds() {
        HashMap<Clock,Integer> res = new HashMap<>();
        res.putAll(ts1.getMaxBounds());
        res.putAll(ts2.getMaxBounds());
        System.out.println("BOUNDS: " + res);
        maxBounds = res;
    }






}
