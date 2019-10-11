package arlistak;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

class FromCSV {
    
    char delimiter = ';';

    void read(String fileName) throws IOException {
    	
    	int columns = numberOfColumnsFromFirstRow(fileName);
    	
        File file = new File(fileName);
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name());
        String content = scanner.useDelimiter("\f").next();
        scanner.close();

        int a = 0;
        int b = 0;
        int c;
        char separator;
        
        while (b < content.length()) {
            c = 0;
            separator = delimiter;
            ArrayList<String> rowIn = new ArrayList<>();
            while (c < columns) {
                if (c == columns - 1) {
                    separator = '\n';
                }
                b = content.indexOf(separator, b);
                rowIn.add(content.substring(a, b).trim());
                a = ++b;
                c++;
            }
            Arlistak.HANGZAVAR_NETSOFT_EXPORT.put(rowIn.get(0),rowIn);
        }
    }
    
    private int numberOfColumnsFromFirstRow(String fileName) throws FileNotFoundException {
    	
    	File file = new File(fileName);
        Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.name());
        String content = scanner.useDelimiter("\f").next();
        scanner.close();
        
        int count = 1;
        int ch = 0;
        while (count < content.length()) {
        	if(content.charAt(ch) == (';')) {
        		count++;
        	}
        	ch++;
        	if(content.charAt(ch) == ('\n')) {
        		break;
        	}
        }
        return count;
    } 
}
