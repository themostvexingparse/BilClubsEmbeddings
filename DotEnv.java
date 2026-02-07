import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class DotEnv {
    private String fileName = ".env";
    private File fileObject = null;
    private HashMap<String, String> keyValuePairs = new HashMap<String, String>();

    DotEnv() {
        fileObject = new File(this.fileName);
        loadData();
    }

    DotEnv(String fileName) {
        this.fileName = fileName;
        fileObject = new File(this.fileName);
        loadData();
    }

    public String getVariable(String variableName) {
        return keyValuePairs.get(variableName);
    }

    private boolean processLine(String line) {
        line = line.trim();
        Scanner lineScanner = new Scanner(line);
        lineScanner.useDelimiter("=");
        String key = null;
        if (lineScanner.hasNext()) {
            key = lineScanner.next().trim();
            if (key.trim().equals(line)) {
                System.out.println("Faulty line in file, aborting.");
                lineScanner.close();
                return false;
            }
        } else {
            lineScanner.close();
            return true;
        }
        lineScanner.close();
        String value = line.substring(line.indexOf("=")+1).trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length()-1);
            value = value.replace("\\\"", "\"")
                         .replace("\\\\", "\\")
                         .replace("\\n", "\n")
                         .replace("\\r", "\r")
                         .replace("\\t", "\t");
        }
        keyValuePairs.put(key, value);
        return true;
    }

    public void loadData() {
        if (fileObject == null) {
            return;
        }

        try {
            Scanner contentScanner = new Scanner(fileObject);
            while (contentScanner.hasNextLine()) {
                String line = contentScanner.nextLine();
                processLine(line);
            }
            contentScanner.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}