package logic;

import models.Guard;
import models.Location;

import java.util.List;
import java.util.Objects;

public class SimpleLocation extends SymbolicLocation {
    private Location location;

    public SimpleLocation(Location location) {
        super();
        this.location = location;
    }

    public Location getActualLocation() {
        return location;
    }

    public List<Guard> getInvariants() {
        return location.getInvariant();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleLocation that = (SimpleLocation) o;
        return location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location);
    }
}