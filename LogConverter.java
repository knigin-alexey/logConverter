/*
*  2. Написать парсер логов, который выполняет следующие действия над логами в п.1:
*   б) Вставить разделитель ";"  между значениями в строке во всем файле, например, 
*   17.11.2014;11:43:51.347;TRACE:;9.6.25.83:54650;Count is 1157, и записать в новый 
*    файл с расширением csv 
*     б) 3 параметра: <тип_разделителя>,<путь_до_лог_файла(ов)>,<имя_нового_файла>
 */
package logconverter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class LogConverter {

  public static void main(String[] args) throws UnsupportedEncodingException {
    args = changeDelim(args);
    
    if (args.length < 3) {
      System.out.println("Количество аргументов должно быть больше 3.");
      return;
    }
    
    String delim = args[0];
    String outFile = args[args.length - 1];
    
    try {
      PrintWriter out =new PrintWriter(new OutputStreamWriter(
                      new FileOutputStream(outFile), "CP1251"));
      
      for (int i = 1; i < args.length - 1; i++) {
        String inFile = args[i];
        //заходим в файлы
        try(BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(inFile), "CP1251"))) {
          for(String line; (line = br.readLine()) != null; ) {
            line = line.replaceAll("\\s", delim);
            out.println(line);
          }
        } catch (IOException e) {
          System.out.println(e.getMessage());
        }
      }
      out.close();
    } 
    catch (FileNotFoundException ex) {
      System.out.println(ex.getMessage());
    }
    
    System.out.println("Готово.");
  }
  
  //метод отличается от остальных из-за учёта того, что разделителем в csv
  //может быть запятая
  public static String[] changeDelim(String[] args){
    boolean firstComma = false;
    StringBuilder strArgs = new StringBuilder("");
    for (int i = 0; i < args.length; i++) {
      strArgs.append(" " + args[i]);
    }
    String tmp = strArgs.toString().trim();
    strArgs.setLength(0);
    strArgs.append(tmp);
    if(tmp.substring(0, 1).equals(",")){
      strArgs.setCharAt(0, ';');
      firstComma = true;
    }
    String[] newArgs = strArgs.toString().split(",");
    
    for (int i = 0; i < newArgs.length; i++){
      newArgs[i] = newArgs[i].trim();
      if (firstComma && i == 0) {
        if (newArgs[0].length() > 1) {
          newArgs[0] = "," + newArgs[0].substring(1,newArgs[0].length());
        } else {
          newArgs[0] = ",";
        }
      }
    }
    return newArgs;
  }
  
}
