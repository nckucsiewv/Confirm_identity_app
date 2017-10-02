package iris.tryfirebase;

/**
 * Created by acer on 2017/10/1.
 */

public class Leader extends Player{
    public String phoneNum;
    public Leader(){

    }
    public Leader(String name, String id, String phoneNum){
        this.name = name;
        this.id = id;
        this.phoneNum = phoneNum;
    }
}
