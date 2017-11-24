import edu.sc.csce740.BILL;
import edu.sc.csce740.BILLIntf;

public class start {
    public static void main(String[] args) throws Exception {

        BILLIntf billIntf = new BILL();

        try {
            billIntf.loadUsers("file/users.txt");
            billIntf.loadRecords("file/students.txt");
            System.out.print("s");
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

}
