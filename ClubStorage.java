import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClubStorage {

    private String clubsDirectory = "./clubs";

    public void setDirectory(String directory) {
        clubsDirectory = directory;
    }

    public List<Club> loadClubs() {
        List<Club> clubs = new ArrayList<>(); // we will return this arraylist of clubs
        File folder = new File(clubsDirectory);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Directory " + clubsDirectory + " does not exist. No clubs loaded.");
            return clubs; // return empty list if something goes wrong
        }

        // we will filter the files that ends with ".club.obj"
        File[] allFiles = folder.listFiles();
        List<File> matchingFiles = new ArrayList<>();
        if (allFiles != null) {
            for (File f : allFiles) {
                if (f.isFile() && f.getName().endsWith(".club.obj")) {
                    matchingFiles.add(f); // if file name ends with .club.obj suffix, we store it 
                }
            }
        }

        if (matchingFiles.isEmpty()) {
            System.out.println("No .club.obj files found in " + clubsDirectory);
            return clubs; // return empty arraylist if none found
        }

        System.out.println("Found " + matchingFiles.size() + " club files. Deserializing...");

        // deserialize the objects
        for (File file : matchingFiles) {
            try {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                // read object data and cast it to club
                Club club = (Club)(objectIn.readObject());
                clubs.add(club);
                objectIn.close(); // close the stream so we don't leak memory ?
            } catch (IOException e) {
                System.out.println("Error reading file " + file.getName() + ": " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Class mismatch in file " + file.getName() + ": " + e.getMessage());
            }
        }

        return clubs;
    }

    public void saveClubs(List<Club> clubs) {
        File dir = new File(clubsDirectory);
        if (!dir.exists()) {
            dir.mkdirs(); // create export directory if it doesn't exist
        }

        System.out.println("Saving " + clubs.size() + " clubs to directory: " + clubsDirectory);

        int counter = 0; // start from 0.club.obj
        for (Club club : clubs) {
            String filename = clubsDirectory + "/" + counter + ".club.obj";
            try {
                FileOutputStream fileOut = new FileOutputStream(filename);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(club); // write the object data to file
                objectOut.close(); // and close the stream so we don't leak
            } catch (IOException e) {
                System.out.println("Error saving club " + club.getName() + ": " + e.getMessage());
            }
            counter++;
        }
        System.out.println("Save complete.");
    }
}
