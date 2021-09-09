/**
 * Author: Anderson Hsiao
 * 2020/09/01
 * Version 1.0
 */

package Spaces;

public abstract class Space implements Comparable<Space> {
    
    private String name;
    private int id;
    
    public Space(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    @Override
    public int compareTo(Space space) {
        if(space != null)
            return id - space.getId();
        return 0;
    }
    
    public String getName() {
        return name;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return id + ". " + name + "\n";
    }
}
