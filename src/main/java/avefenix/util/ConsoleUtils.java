
package avefenix.util;
public class ConsoleUtils {
    public static void clear(){
        try{
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
            else { System.out.print("\033[H\033[2J"); System.out.flush(); }
        }catch(Exception ignored){}
    }
}
