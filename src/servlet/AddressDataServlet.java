package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AddressDataManager;
import dao.ConnectDB;
import dao.Route;
import dto.Address;
import dto.DBRoute2Data;
import dto.Route2DataCall;
import dto.SetData;

@WebServlet("/AddressDataServlet")
public class AddressDataServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    ConnectDB[] db = new ConnectDB[20];
    AddressDataManager[] ad = new AddressDataManager[20];  
    SetData[] sd = new SetData[20];
    Route[] r = new Route[20];
    Address[] aTmp = new Address[20];    		
    boolean[] apiFlag = new boolean[20];
    
    public AddressDataServlet() {
        super();
        for(int i=0; i<20; i++) {
            ad[i]=new AddressDataManager();
            sd[i] = new SetData();
            db[i] = new ConnectDB();
         }
    }
    
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.getWriter().append("Served at: ").append(request.getContextPath());
      this.doPost(request, response);
   }
   
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //contextType 
      response.setContentType("text/html;charset=UTF-8");
      
      PrintWriter out = response.getWriter();
      System.out.println(" "+ request.getParameter("menuIndex"));
      int optionNum = Integer.parseInt(request.getParameter("menuIndex"));
      //System.out.println("customerID : "+request.getParameter("customerID"));
      int ID = Integer.parseInt(request.getParameter("customerID"));
      
      
      switch(optionNum) {
         case 0: 

        	 String cID = request.getParameter("cID");   
        	 String what2 = request.getParameter("what"); 
        	 String name = request.getParameter("name");
        	 System.out.println("cID() : "+cID);
        	 String resultFlag = db[ID].CheckSameData(ad[ID].addressData, cID, what2, name);
        	 if(resultFlag.equals("1") && what2.equals("1")) { 
        		 String rID2 = db[ID].makeRID(ad[ID].addressData); 

        		 DBRoute2Data tmp = new DBRoute2Data(cID, rID2);
        		 DBRoute2Data tmpResult = r[ID].putRoute2Dto(tmp, sd[ID].GetStartData(), sd[ID].GetLastData());
        	 	 db[ID].SaveRoute2Data(tmpResult);
        	 }
        	 out.print(resultFlag); 
        	 break;
         case 1: 
        	 apiFlag[ID] = true;
            if(ad[ID].addressData.size() == 7) {
               out.print(7);
            }else {
               double lat = Double.parseDouble(request.getParameter("lat"));
               double lng = Double.parseDouble(request.getParameter("lng"));
               String address = request.getParameter("address");
               //String si = request.getParameter("si");
               aTmp[ID] = new Address(lat,lng,address);   
               String result= ad[ID].addData(aTmp[ID]);
               out.print(result); 
            }
            break;
            
         case 2: 
            int deleteIndex =  Integer.parseInt(request.getParameter("deleteIndex"));
            int result1 = ad[ID].deleteData(deleteIndex-1);   
            out.print(result1); 
            break;
            
         case 3: 
            int addressIndex =  Integer.parseInt(request.getParameter("addressIndex"));
            String result2 = ad[ID].callAddress(addressIndex);
            out.print(result2);          
            break;
            
         case 4: 
        	  System.out.print(""); 
        	  String result3 = ad[ID].callLatLng(sd[ID]); 
        	  out.print(result3);  
            break;
            
         case 5: 
            String result4 = ad[ID].callAllAddress();
            int num = Integer.parseInt(request.getParameter("num"));
            System.out.println("num : " + num);
            if(num== 0) {  
            	System.out.println("");
            	this.r[ID] = new Route(ad[ID].addressData.size());
            	apiFlag[ID] = true;   
               // r[ID].Clear();
                System.out.println(" flag ="+ r[ID].carFlag+",  flag="+ r[ID].ptFlag);
            	sd[ID].SetStartData(-1);
            	sd[ID].SetLastData(-1);
            }
            out.print(result4); 
            break;      
            
         case 6: 
        	 cID = request.getParameter("cID");
        	 String resultDB = db[ID].GetAllData(cID); 
        	 out.print(resultDB);
        	 break;
            
         case 7: //reset
            int result6 = ad[ID].resetData();
            out.print(result6);
            break;   
            
         case 8:
            int result7 = ad[ID].listSize();
            out.print(result7);
            break;   
            
         case 9:  //start, last 
        	 System.out.println("");
            int index = Integer.parseInt(request.getParameter("index"));
            int startLast = Integer.parseInt(request.getParameter("startLast"));
            System.out.print(index);
            if(startLast == 0) 
               sd[ID].SetStartData(index);
            else if(startLast == 1) 
               sd[ID].SetLastData(index);
            out.print(1);
            break;
            
         case 10:  
            int result8 = sd[ID].isSame();
            out.print(result8);
            break;
         
         case 11:  
            String result9 = ad[ID].callAllAddress_StartData(sd[ID]);
            out.print(result9);
            break;
            
         case 12: 
        	    
             String result14 = "";
          		  result14 += "<Data>";
          		  result14 += "<lat>" + Double.toString(ad[ID].addressData.get(0).getLat()) + "</lat>";
          		  result14 += "<lng>" + Double.toString(ad[ID].addressData.get(0).getLng()) + "</lng>"; 
          		  result14 += "</Data>";
             out.print(result14);	  
             break;
             
         case 13:  
        	 int what = Integer.parseInt(request.getParameter("what"));
        	 System.out.println("13," +what +":"+ r[ID].ptFlag);
        	 int result13 = 0; 
        	 if(what == 0) { //
	        		 try {
	        			 while(true) {
	        				 Thread.sleep(500);
	        				 if(r[ID].ptFlag == 1 && sd[ID].GetStartData() != -1 && sd[ID].GetLastData() != -1) {
	    	        			 System.out.println("대:"+r[ID].ptFlag+", "+sd[ID].GetStartData() +", "+sd[ID].GetLastData());
	    	        			 result13 = 1;
	    	        			 break;
	    	        		 }
	        			 }
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	 }else if(what == 1){ //        		  			 
        			try {
        				while(true) {         
						Thread.sleep(500);
						if(r[ID].carFlag == 1 && sd[ID].GetStartData() != -1 && sd[ID].GetLastData() != -1) {
	        				System.out.println("자동차While:"+r[ID].carFlag);
		        			 result13 = 1;
		        			 break;
		        		 }
        				}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();    			 
					}
        	 }
        	 System.out.println(result13);
        	 out.print(result13);
        	 break;
            
         case 14: 
            String result10 = ad[ID].callAllLatLng();
            System.out.print(result10);
            out.print(result10);
            break;
            
         case 15:             
           if(apiFlag[ID]) {
              int a = Integer.parseInt(request.getParameter("a"));
              int b = Integer.parseInt(request.getParameter("b"));
              String car = request.getParameter("carBlock");
              apiFlag[ID] = r[ID].callAPIData(a, b, car, ad[ID], sd[ID]);      
           }
            break;
            
         case 16:  
           int how = Integer.parseInt(request.getParameter("how"));
           System.out.println("");            
           r[ID].callShortestPath(sd[ID].GetStartData(),sd[ID].GetLastData(), sd[ID].isSame(), how); //          
           break;
         
         case 17:  
           int how2 = Integer.parseInt(request.getParameter("how")); 
           
           out.print(r[ID].orderResult(how2, ad[ID]));
           break;
         
         case 18:
           int how3 =  Integer.parseInt(request.getParameter("how"));
           out.print(r[ID].resultPoly(how3));       
           break;
           
         case 19: 
        	System.out.println("19번 들어옴");
     	   String cID2 = request.getParameter("cID");  
     	   String rID2 = request.getParameter("rID");
     	   ad[ID].callSaveDBData(rID2, cID2);        	
     	   out.print("1");
     	   break;
        	 
         case 20: 
    	   int how4 = Integer.parseInt(request.getParameter("how"));
    	   System.out.println("");
    	   out.print(r[ID].resultList(how4, ad[ID], sd[ID]));
           break;   
           
         case 21: 
        	 String rID = request.getParameter("rID");
        	 String cID3 = request.getParameter("cID");  
        	 Route2DataCall resultRoute2 = db[ID].GetSavedRoute2Data(cID3, rID);
        	 sd[ID].SetStartData(resultRoute2.getStart());
        	 sd[ID].SetLastData(resultRoute2.getLast());
        	 this.r[ID] = new Route(ad[ID].addressData.size()); 
        	
        	 r[ID].putDTO_AND_reCall(resultRoute2, ad[ID]);
        	 break;
        	 
         case 22: 
        	 String rID4 = request.getParameter("rID");
        	 String cID4 = request.getParameter("cID");
        	 out.print(db[ID].DeleteData(rID4, cID4)); //
        	 break;
	 
         case 23: 
        	
     	 	/*db = new ConnectDB[ID];
     	    ad = new AddressDataManager[ID];  
     	    sd = new SetData[ID];*/
        	 break; 

      }               
   }  
}