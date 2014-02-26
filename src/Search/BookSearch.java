package Search;

import Globals.Book;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookSearch extends HashMap<String,Book> {

	// search versucht zun�chst eine direkte �bereinstimmung zu finden.
	// Danach wird auf einen Regul�renausdruck getestet.
	// Wenn hier auch keine �bereinstimmung gefunden wird, wird Soundex verwendet.
    public HashMap<String,Book> search(String name){
    	HashMap<String,Book> tmp;
    	tmp = searchDirect(name);
    	if(tmp.size()==0){
    		tmp = searchRegex(name);
    		if(tmp.size()==0){
    			tmp = searchSoundex(name);
    			if(tmp.size()==0){
    				tmp = new HashMap<String,Book>();
    			}
    		}
    	}
    	return tmp;
    }

    private HashMap<String,Book> searchDirect(String name){
    	HashMap<String,Book> tmp = new HashMap<String, Book>();
    	Book  tmpBook = this.get(name);
    	if(tmpBook!=null){
    		tmp.put(name,tmpBook);
    	}
    	return tmp;
    }

    public HashMap<String, Book> searchRegex(String searchString){
        HashMap<String,Book> tmp = new HashMap<String, Book>();
        Iterator<Book> it = this.values().iterator();
        Book srv;
        Matcher m;

        while(it.hasNext()){
            srv = it.next();
            m = Pattern.compile(searchString).matcher(srv.getTitle());
            if(m.matches()) tmp.put(srv.getTitle(), srv);
        }
        return tmp;
    }


    private HashMap<String,Book> searchSoundex(String name){
    	String strSoundex = soundex(name);
    	HashMap<String,Book> tmp = new HashMap<String,Book>();
        Iterator<String> it = this.keySet().iterator();
        String tmpString;

        while(it.hasNext()){
            tmpString = it.next();
            if(soundex(tmpString).equals(strSoundex)) tmp.put(tmpString,this.get(tmpString));
        }
        return tmp;

    }

    public static String soundex(String string) {
        char[] x = string.toUpperCase().toCharArray();
        char firstLetter = x[0];

        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {
                case 'B':
                case 'F':
                case 'P':
                case 'V': { x[i] = '1'; break; }

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z': { x[i] = '2'; break; }

                case 'D':
                case 'T': { x[i] = '3'; break; }

                case 'L': { x[i] = '4'; break; }

                case 'M':
                case 'N': { x[i] = '5'; break; }

                case 'R': { x[i] = '6'; break; }

                default:  { x[i] = '0'; break; }
            }
        }

        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        output = output + "0000";
        return output.substring(0, 4);
    }
}
