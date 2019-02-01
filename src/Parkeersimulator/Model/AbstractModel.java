package Parkeersimulator.Model;

import Parkeersimulator.View.AbstractView;

import java.util.ArrayList;
import java.util.List;

/**
 * De abstracte model
 * @author Andy Perukel, Ramon kits
 * @version 01-03-2019
 *
 */

public abstract class AbstractModel {
    public List<AbstractView> views;

    /**
     * Constuctor voor de abstracte model
     */    
    public AbstractModel (){
        views = new ArrayList<>();
    }

    
    /**
     * Voegt alle views toe aan de list
     * @param view, de view om toe te voegen
     */
    public void addView (AbstractView view){
        views.add(view);
    }

    public abstract void updateViews();
}
