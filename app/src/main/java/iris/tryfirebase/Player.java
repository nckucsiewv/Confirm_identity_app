package iris.tryfirebase;

/**
 * Created by acer on 2017/10/1.
 */

public class Player {
    public String name;
    public String id;
    public String phoneNum;
    //default ctor
    public Player(){

    }
    // for general players
    public Player(String name, String id){
        this.name = name;
        this.id = id;
    }
    // for leader
    public Player(String name, String id, String phoneNum){
        this.name = name;
        this.id = id;
        this.phoneNum = phoneNum;
    }
}
