package pms.testing;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by ricar on 12/01/2017.
 */

public class Librarian {

    private void onCreate(Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs",0);
        SharedPreferences.Editor edt = prefs.edit();

        edt.putInt("string_counter",0);

        String[] array = context.getResources().getStringArray(R.array.messages);
        String list = getRandIntArrayOfSize(array.length);

        edt.putString("string_list",list);

        edt.commit();
    }

    public String getNextMessage(Context context){
        SharedPreferences prefs = context.getSharedPreferences("prefs",0);
        SharedPreferences.Editor edt = prefs.edit();

        int i = prefs.getInt("string_counter",0);

        String list = prefs.getString("string_list",null);
        if(list!=null){
            StringTokenizer tokens = new StringTokenizer(list,",");
            ArrayList<Integer> rand_list = new ArrayList<>();
            while(tokens.hasMoreTokens()){
                rand_list.add(Integer.parseInt(tokens.nextToken()));
            }

            String[] messages = context.getResources().getStringArray(R.array.messages);
            return messages[rand_list.get(i)];
        }else{
            return null;
        }
    }

    public int getNextImage(){

        return 0;
    }

    private String getRandIntArrayOfSize(int size){
        //construct random number sequence

        //transform sequence into string
        StringBuilder bldr = new StringBuilder();
        for(int i=0;i<size;i++){

            bldr.append(0).append(",");
        }

        return bldr.toString();
    }
}
