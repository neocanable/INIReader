
package io.github.azige.util;

import java.util.*;

/**
 *
 * @author Azige
 */
public class INISection {
    Map<String, String> map = new HashMap<String, String>();
    String name;

    INISection (String name){
        this.name = name;
    }

    void add(String key, String value){
        map.put(key, value);
    }

    String get(String key){
        return map.get(key);
    }
}
