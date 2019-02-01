package Parkeersimulator.Model;

/**
 * Een locatie met floors, rows en places
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */
public class Location {

    private boolean pass;
    private int floor;
    private int row;
    private int place;

    /**
     * Constructor voor de locatie
     * @param pass, voor de abonnement plekken
     * @param floor, de verdieping van de locatie
     * @param row, de rij van de locatie
     * @param place, de plaats van de locatie
     * 
     */
    public Location(boolean pass, int floor, int row, int place) {
    	this.pass = pass;
        this.floor = floor;
        this.row = row;
        this.place = place;
    }

    /**
     * Checkt of het object in de parameter hetzelfde is als dit object
     * @param obj Object, het object om te controleren
     * @return geeft true terug als het object hetzelfde is als de parameter
     */
    public boolean equals(Object obj) {
        if(obj instanceof Location) {
            Location other = (Location) obj;
            return floor == other.getFloor() && row == other.getRow() && place == other.getPlace();
        }
        else {
            return false;
        }
    }

    /**
     * Return a string of the form floor,row,place.
     * @return A string representation of the location.
     */
    public String toString() {
        return floor + "," + row + "," + place;
    }

    /**
     * Use the 10 bits for each of the floor, row and place
     * values. Except for very big car parks, this should give
     * a unique hash code for each (floor, row, place) tupel.
     * @return A hashcode for the location.
     */
    public int hashCode() {
        return (floor << 20) + (row << 10) + place;
    }
    
    /**
     * Geeft plekken voor de abonnementhouders
     * @return pass, plekken voor de abonnementhouders
     */
    public boolean getPlaceIsPassPlace() {
    	return pass;
    }

    /**
     * Geeft de vloer van de locatie
     * @return The floor.
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Geeft de rij van de locatie
     * @return The row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Geeft de plaats van de locatie
     * @return The place.
     */
    public int getPlace() {
        return place;
    }

}