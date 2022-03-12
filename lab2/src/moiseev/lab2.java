import java.util.Scanner;
import java.util.regex.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Comparable;

public class lab2 {

  public static void main(String[] args) throws IOException
  {
      System.out.println("Enter amount of DNS-servers:");

      ArrayList<String> DnsAddres = new ArrayList<String>();

      Scanner scan = new Scanner(System.in);
      int size = scan.nextInt();

      System.out.println("Enter your DNS-servers:");

      for (int i = 0; i < size; i++)
      {
          String temp = scan.next();
          DnsAddres.add(temp);
      }
      scan.close();

      ArrayList<DNSRes> DNSres = new ArrayList<DNSRes>();

      for (int i = 0; i < size; i++)
      {
          ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "ping "+ DnsAddres.get(i));
          builder.redirectErrorStream(true);
          Process p = builder.start();
          BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

          String line;
          ArrayList<String> consoleStrings = new ArrayList<String>();

          while (true)
            {
              int a =0;
              line = r.readLine();
              if (line == null)
              {
                break;
              }

              consoleStrings.add(line);
            }

          Pattern pattern = Pattern.compile(".* = (\\d*)");
          Matcher matcher = pattern.matcher(consoleStrings.get(consoleStrings.size() - 1));

          if (matcher.find())
          {
            boolean canUse = true;
            for (DNSRes a : DNSres)
            {
              if(a.getName().equals(DnsAddres.get(i)))
              {
                canUse = false;
              }
            }
            if(canUse)
            {
              DNSres.add(new DNSRes(DnsAddres.get(i), Integer.parseInt(matcher.group(1))));
            }
          }
      }

      Collections.sort(DNSres, Collections.reverseOrder());

      System.out.println("");
      for (DNSRes a : DNSres)
      {
         System.out.println("Adr: " + a.getName() + ", time: " + a.getTime() + "ms");
      }
  }

}

class DNSRes implements Comparable<DNSRes>
{
  private String Name;
  private int Time;

  public DNSRes(String name, int time)
  {
    Name = name;
    Time = time;
  }

  public String getName()
  {
    return Name;
  }

  public int getTime()
  {
    return Time;
  }

  @Override
  public int compareTo(DNSRes obj)
  {
      return Time - obj.Time;
  }
}
