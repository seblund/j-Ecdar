package logic;

import models.Guard;

import java.util.ArrayList;
import java.util.List;

public class InconsistentLocation extends SymbolicLocation {

    public List<Guard> getInvariants() {
        // TODO new clocks should be <= 0
        return new ArrayList<>();
    }}
