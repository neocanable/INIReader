
package io.github.azige.util;

/**
 *
 * @author Azige
 */
class INIParseException extends RuntimeException{

    public INIParseException(String message) {
        super(message);
    }

    public INIParseException() {
    }

}

class SectionNotFoundException extends RuntimeException{

}