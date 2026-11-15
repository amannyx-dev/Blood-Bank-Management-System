package bloodbank.util;

import java.io.*;
import java.util.Map;

public class CsvExporter {
    public void exportStock(Map<?,Integer> stock,File file)throws IOException{try(BufferedWriter writer=new BufferedWriter(new FileWriter(file))){writer.write("Blood Group,Units");writer.newLine();for(Map.Entry<?,Integer> entry:stock.entrySet()){writer.write(entry.getKey()+","+entry.getValue());writer.newLine();}}}
}
