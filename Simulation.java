import java.util.*;

public class Simulation {
    boolean[][] cams, pols;
    int numCams, numPols, maxCams, maxPols;
    ArrayList<Integer> camsData, polsData;
    boolean refuge; // Activite refuge if true
        // The refuge is the top left 4x4 squares, where pollards cannot spawn

    public Simulation() {
        cams = new boolean[10][10];
        pols = new boolean[10][10];
        numCams = 5;
        numPols = 5;
        maxCams = 100;
        maxPols = 75;
        camsData = new ArrayList<Integer>();
        polsData = new ArrayList<Integer>();
        refuge = false;
    }

    public Simulation(int mc, int mp, boolean refugeOn) {
        cams = new boolean[10][10];
        pols = new boolean[10][10];
        numCams = 5;
        numPols = 5;
        maxCams = mc;
        maxPols = mp;
        camsData = new ArrayList<Integer>();
        polsData = new ArrayList<Integer>();
        refuge = refugeOn;
    }

    public void run(int c, int p) {
        int squaresLeft = 100;
        int overlap = 0;
        for(int i = 0; i < 10; i++) for(int j = 0; j < 10; j++) {
            // Caminacules
            if((int)(Math.random() * squaresLeft) + 1 <= c) {
                cams[i][j] = true;
                c--;
            }
            // Pollards
            if((int)(Math.random() * squaresLeft) + 1 <= p) {
                if(refuge) {
                    if(!(i <= 3 && j <= 3)) { // Cannot spawn on top left 4x4 squares
                        pols[i][j] = true;
                        p--;
                    }
                }
                else {
                    pols[i][j] = true;
                    p--;
                }
            }
            squaresLeft--;
            if(cams[i][j] && pols[i][j]) overlap++;
        }
        numCams = (numCams - overlap) * 3;
        if(numCams < 0) numCams = 0;
        else if(numCams > maxCams) numCams = maxCams;
        numPols = (numPols - numPols/3) + overlap;
        if(numPols < 0) numPols = 0;
        else if(numPols > maxPols) numPols = maxPols;
    }

    public static String arrToString(ArrayList<Integer> arr) {
        String str = "";
        for(int i : arr) str += i + " ";
        return str;
    }

    public void simulate(int trials) {
        for(int i = 0; i < trials; i++) {
            System.out.println("GENERATION " + (i + 1));
            int ogCams = numCams;
            int ogPols = numPols;
            run(numCams, numPols);
            System.out.println("CAMS: ");
            System.out.println(toStringCams(cams));
            System.out.println();
            System.out.println("POLS: ");
            System.out.println(toStringPols(pols));
            System.out.println("# of CAMS: " + ogCams);
            System.out.println("# of POLS: " + ogPols);
            System.out.println();
            System.out.println("--------------------------------------");
            System.out.println();
            camsData.add(ogCams);
            polsData.add(ogPols);
            reset();
        }

        // TABLE
        System.out.println("GEN   CAMS   POLS");
        for(int i = 0; i < trials; i++) {
            System.out.print(i + 1);
            for(int j = 0; j < 6 - countDigits(i + 1); j++) System.out.print(" ");
            System.out.print(camsData.get(i));
            for(int j = 0; j < 7 - countDigits(camsData.get(i)); j++) System.out.print(" ");
            System.out.print(polsData.get(i));
            for(int j = 0; j < 7 - countDigits(polsData.get(i)); j++) System.out.print(" ");
            System.out.println();
        }
        System.out.println();
        // System.out.println("CAMS: " + arrToString(camsData));
        // System.out.println("POLS: " + arrToString(polsData));
    }

    public void reset() {
        cams = new boolean[10][10];
        pols = new boolean[10][10];
    }

    public static String toString(boolean[][] arr) {
        String str = "";
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                str += arr[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    }

    public static String toStringCams(boolean[][] arr) {
        String str = "";
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                if(arr[i][j]) str += "C ";
                else str += ". ";
            }
            str += "\n";
        }
        return str;
    }

    public static String toStringPols(boolean[][] arr) {
        String str = "";
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                if(arr[i][j]) str += "P ";
                else str += ". ";
            }
            str += "\n";
        }
        return str;
    }

    public static int countDigits(int n) {
        if(n == 0) return 1;
        int count = 0;
        while(n > 0) {
            count++;
            n /= 10;
        }
        return count;
    }

    public static void main(String[] args) {
        Simulation sim0 = new Simulation();
        Simulation sim1 = new Simulation(100, 90, false);
        Simulation sim2 = new Simulation(100, 75, true);

        System.out.println("---------DEFAULT CONDITIONS: ---------");
        sim0.simulate(20);

        System.out.println("---------MAX POLLARDS 90: ---------");
        sim1.simulate(20);

        System.out.println("---------REFUGE: ---------");
        sim2.simulate(20);
        /*
        System.out.println(countDigits(1));
        System.out.println(countDigits(2));
        System.out.println(countDigits(10));
        System.out.println(countDigits(33));
        System.out.println(countDigits(100));
        */
    }
}
