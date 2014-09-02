
package io.github.azige.util;

import java.io.*;
import java.util.*;

/**
 *
 * @author Azige
 */
public class INIReader {
    ArrayList<INISection> sections = new ArrayList<INISection>();
    BufferedReader reader;
    int currentSec = 0;

    public INIReader(){

    }

    public INIReader(File ini) throws IOException{
        parse(ini);
    }

    public void switchSection(String section){
        for (int i=0; i<sections.size(); i++){
            if (sections.get(i).name.equals(section)){
                currentSec = i;
                return;
            }
        }
        throw new SectionNotFoundException();
    }

    public String get(String key){
        return sections.get(currentSec).get(key);
    }

    public void parse(File ini) throws IOException{
        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(new FileReader(ini));
        sections.clear();
        int c, linect = 1;
        boolean sf, kf, vf; //section flag, key flag, value flag
        String key = null;
        sf = kf = vf = false;

        while((c = reader.read()) != -1){
            //System.out.printf("%c %b %b %b\n", c, sf, kf, vf);
            if (String.valueOf((char)c).matches("[ \\t]")) continue;
            else if (c == ';' || c == '\r' || c == '\n'){
                if (sf || kf) throw new INIParseException("Line " + linect + ": Unexpected line end.");
                if (vf){
                    sections.get(sections.size() - 1).add(key, sb.toString());
                    sb = new StringBuilder();
                }
                sf = kf = vf = false;
                reader.readLine();
                linect++;
            }
            else if (c == '['){
                if (kf || vf || sf) throw new INIParseException("Line " + linect + ": Unexpected '['.");
                sf = true;
            }
            else if (c == ']'){
                if (!sf) throw new INIParseException("Line " + linect + ": Unexpected ']'.");
                sf = false;
                sections.add(new INISection(sb.toString()));
                sb = new StringBuilder();
            }
            else if (c == '='){
                if (!kf) throw new INIParseException("Line " + linect + ": Unexpected '='.");
                kf = false; vf = true;
                key = sb.toString();
                sb = new StringBuilder();
            }
            else{
                if (!sf && !vf && !kf) kf = true;
                sb.append((char)c);
            }
        }
        if (vf) sections.get(sections.size() - 1).add(key, sb.toString());
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (INISection sec: sections){
            sb.append('[');
            sb.append(sec.name);
            sb.append("]\n");
            for (String key: sec.map.keySet()){
                sb.append(key);
                sb.append('=');
                sb.append(sec.map.get(key));
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
