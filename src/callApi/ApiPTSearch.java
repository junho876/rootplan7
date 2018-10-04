package callApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import dao.CalculateDist;
import dto.DataPair;
import dto.DataTotal;
import dto.InfoPT;
import dto.InfoSectionPT;
import dto.TimeMethod;

public class ApiPTSearch {
   CalculateDist cd = new CalculateDist();
   StringBuilder sb;
   String key = "5FtIuAS9YmPfOD56TV5NHqYE6EivPWAAIBCZcy6V72c";
   LinkedList<dto.Address> ad;
   DataTotal dataTotal;
   
   ApiWalkSearch ws;
   int listSize;
   boolean flag = false; 
   boolean isSame=false;
   int adSize;

   public ApiPTSearch(LinkedList<dto.Address> ad, DataTotal dataTotal, int listSize) {
	  this.listSize = listSize;
      adSize = ad.size();
      this.ad = ad;
      this.ws = new ApiWalkSearch();
      this.dataTotal = dataTotal;
     
      dataTotal.initPtDist();
   }   

  
   void ptPrint(int size) {
      
      for(int i=0; i<listSize; i++) {
         for(int j=0; j<listSize; j++) {
            System.out.print(dataTotal.ptDist[i][j].getTime() + " "); 
         }
         System.out.println();
      }
   }
   
   
   public void callTransportApi(int a, int b) {
      for (int i = a; i < b; i++) {
         for (int j = 0; j < listSize; j++) {
            if (dataTotal.ptDist[i][j].getMethod()) {
            	continue; 
            }else if (i == j)
            	dataTotal.ptDist[i][j] = new TimeMethod(Integer.MAX_VALUE, false);
            else {
               callPTApi( ad.get(i).getLat(),  ad.get(i).getLng(), ad.get(j).getLat(), ad.get(j).getLng(),i ,j );
            }
         }
      }
   
   }
 

  public void callPTApi(double sx, double sy, double ex , double ey, int i, int j) {
      CalculateDist calDist = new CalculateDist();
	  double distanceMeter = calDist.distance(sx, sy, ex, ey, "meter");
      if (distanceMeter <= 800) {
       
         int tmpTime = 0;
         if (flag == true) {
            try {
               Thread.sleep(550);
               tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  
            } catch (Exception e) {}
         } else {
            tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
         }
         
        
         dataTotal.ptDist[i][j] = new TimeMethod(tmpTime, true);
         dataTotal.ptDist[j][i] = new TimeMethod(tmpTime, true);
         flag = true;
      } else {
         flag = false;
         try {
            String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX=" + Double.toString(sx) + "&SY="
                  + Double.toString(sy) + "&EX=" + Double.toString(ex) + "&EY=" + Double.toString(ey) + "&apiKey="
                  + key + "";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {         
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
             
               int tmpTime = 0;
               if (flag == true) {
                  try {
                     Thread.sleep(550);
                     tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  
                  } catch (Exception e) {}
               } else {
                  tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
               }
               
            
               dataTotal.ptDist[i][j] = new TimeMethod(tmpTime, true);
               dataTotal.ptDist[j][i] = new TimeMethod(tmpTime, true);
               flag = true;

               return ;
            }
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
               sb.append(line + "\n");
            }         

            String data = sb.toString();
            String[] array;
            array = data.split("\"");
            for (int k = 0; k < array.length; k++) {
               if(array[k].equals("code")) {   
                  
                   int tmpTime = 0;
                   if (flag == true) {
                      try {
                         Thread.sleep(550);
                         tmpTime = ws.walkApi(i, j, sx, sy, ex, ey)/60;  
                      } catch (Exception e) {}
                   } else {
                      tmpTime = ws.walkApi(i, j, sx, sy, ex, ey) / 60;
                   }
                   
                  
                   dataTotal.ptDist[i][j] = new TimeMethod(tmpTime, true);
                   dataTotal.ptDist[j][i] = new TimeMethod(tmpTime, true);
                   flag = true;

                   return ;
               }
               if (array[k].equals("totalTime")) {
            	   dataTotal.ptDist[i][j] = new TimeMethod(Integer.parseInt(array[k + 1].substring(1, array[k + 1].length() - 1)), false);
                  break;
               }              
            }
            br.close();
            con.disconnect();
         } catch (Exception e) { }
      }
   }

  public void resultOrderCall(int[] result, int start, int end) {  //결과대로 호출
	  if(start==end) listSize++;  
	  
      for(int i =0; i < listSize -1; i++) {
    	  System.out.println("i:"+i);
    	  dataTotal.ptList.add(callResultPT( ad.get(result[i]).getLat(), ad.get(result[i]).getLng(),
               ad.get(result[i+1]).getLat(), ad.get(result[i+1]).getLng(), result[i],result[i+1]));
      }
  }
  
   
  public InfoPT callResultPT(double sx, double sy, double ex, double ey,int a, int b) {
      InfoPT infopt = new InfoPT(); 
      InfoSectionPT infoSec = new InfoSectionPT();
      CalculateDist calDist = new CalculateDist();
      double distanceMeter = calDist.distance(sx, sy, ex, ey, "meter");
      if (dataTotal.ptDist[a][b].getMethod() || distanceMeter <= 800) {
    	
      
         if (flag == true) {
            try {
               Thread.sleep(1500);
               infopt = ws.resultWalkPTApi(sx, sy, ex, ey); 
            } catch (Exception e) {}
         } else {
            infopt = ws.resultWalkPTApi(sx, sy, ex, ey);
         }        
         
         flag = true;
      } else {
         flag=false;
         try {
            String apiURL = "https://api.odsay.com/v1/api/searchPubTransPath?SX=" + Double.toString(sx) + "&SY="
                  + Double.toString(sy) + "&EX=" + Double.toString(ex) + "&EY=" + Double.toString(ey) + "&apiKey="
                  + key + "";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
               br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
               
              
            }
            sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
               sb.append(line + "\n");
            }

            br.close();
            con.disconnect();

            String data = sb.toString();
            String[] array;
            array = data.split("\\{|\\}|\"|\\,|\\:");

            int trafficType = 0;
            double x = 0, y = 0;
            int cnt=0;
            for (int i = 0; i < array.length; i++) {
               if (array[i].equals("result")) {
                  infopt.setSx(sx);
                  infopt.setSy(sy);
                  infopt.setEx(ex);
                  infopt.setEy(ey);
                 
               } else if (array[i].equals("trafficType")) {
                  trafficType = Integer.parseInt(array[i + 2]);
               } else if (array[i].equals("lane")) {
                  if(cnt!=0) infopt.addSection(infoSec);
                  cnt++;
                  infoSec = new InfoSectionPT();
                  infoSec.setTrafficType(trafficType); 
               } else if (array[i].equals("busNo")) { 
                  if (trafficType != 2)   continue;
                  infoSec.addBusNoList(array[i + 3]);
               } else if (array[i].equals("name")) { 
                  if (trafficType != 1)   continue;
                  infoSec.setSubwayLine(array[i + 3]);
               } else if(array[i].equals("stationCount")){ 
            	   infoSec.setSectionStationCount(Integer.parseInt(array[i + 2]));
               }else if (array[i].equals("x")) {
                  x = Double.parseDouble(array[i + 3]);
               } else if (array[i].equals("y")) {
                  y = Double.parseDouble(array[i + 3]);
                  infopt.addLineList(new DataPair(x, y));
               } else if (array[i].equals("distance")) {
            	   if(trafficType==3) continue;
                  infoSec.setSectionDistance(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("sectionTime")) {
            	   if(trafficType==3) continue;
                  infoSec.setSectionTime(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("startName")) {
                  infoSec.setStartStation(array[i + 3]);
               } else if (array[i].equals("endName")) {
                  infoSec.setEndStation(array[i + 3]);
               } else if (array[i].equals("payment")) {
                  infopt.setFare(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("totalTime")) {
                  infopt.setTotalTime(Integer.parseInt(array[i + 2]));
               }else if(array[i].equals("totalStationCount")) { 
            	   infopt.setStationCount(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("totalDistance")) {
                  infopt.setTotalDistance(Integer.parseInt(array[i + 2]));
               } else if (array[i].equals("firstStartStation")) {
                  infopt.setFirstStartStation(array[i + 3]);
               } else if (array[i].equals("lastEndStation")) {
                  infopt.setLastEndStation(array[i + 3]);
                  break; 
               }
            }
            infopt.addSection(infoSec); 
         } catch (Exception e) {}
      }
      return infopt;
  }
  
}