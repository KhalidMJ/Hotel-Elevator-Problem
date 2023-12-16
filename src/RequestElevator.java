/**
 * An interface for objects that can request an elevator to move up or down.
 * @author Ammar
 */
public interface RequestElevator {
    /**
     * A Request for going up
     */
    public void requestUp();

    /**
     * A Request for going down
     */
    public void requestDown();
}
