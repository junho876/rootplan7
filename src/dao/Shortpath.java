package dao;

import dto.DataTotal;


public class Shortpath {
   int min;
   int visit[];
   int listSize;
   int tmp[]; 
   public DataTotal dataTotal;

   public void init(int size, DataTotal dataTotal){
      min=Integer.MAX_VALUE;
      listSize = size;
      visit = new int[size];
      tmp = new int[size];
      this.dataTotal = dataTotal;
   }
   
   void dfs(int cnt, int now, int sum, int end, int how) { 
      if(sum > min) return;
      if(cnt == listSize-1) {
         if(how==0)
            sum +=  dataTotal.ptDist[now][end].getTime();
         else
            sum += dataTotal.carDist[now][end].getTime();
         
         if(min>sum) {
            for(int i=0; i<listSize - 1; i++) {
               if(how==0)    dataTotal.ptAns[i] = tmp[i];
               else  dataTotal.carAns[i] = tmp[i];
            }
            System.out.println("cnt"+cnt+", end: "+ end);
            if(how == 0)
            	dataTotal.ptAns[cnt] = end;
            else 
            	dataTotal.carAns[cnt] = end;
            min = sum;
         }
         return;
      }
      
      for(int i=0; i<listSize; i++) {
         if(visit[i] == 1 || i == now || i == end) continue;      
         visit[i] = 1;
         tmp[cnt] = i;
         if(how==0) { 
            dfs(cnt+1, i, sum+dataTotal.ptDist[now][i].getTime(), end, 0);   
         }else { 
            dfs(cnt+1, i, sum+dataTotal.carDist[now][i].getTime(), end, 1);
         }
         tmp[cnt]=-1;
         visit[i] = 0;
      }
   }
   
   void dfsEqual(int cnt, int now, int sum, int start, int how) { 
      if(sum > min) return;
      if(cnt == listSize) {
         
         if(how==0)
            sum +=  dataTotal.ptDist[now][start].getTime();
         else
            sum += dataTotal.carDist[now][start].getTime();
         
         if(min>sum) {
            for(int i=0; i<listSize; i++) {
               if(how==0)    dataTotal.ptAns[i] = tmp[i];
               else  dataTotal.carAns[i] = tmp[i];
            }
            min = sum;
         }
         return;
      }
      
      for(int i=0; i<listSize; i++) {
         if(visit[i] == 1|| i == now) continue;
         
         visit[i] = 1;
         tmp[cnt] = i;
         if(how==0) { 
            dfsEqual(cnt+1, i, sum+dataTotal.ptDist[now][i].getTime(), start, 0);   
         }else { 
            dfsEqual(cnt+1, i, sum+dataTotal.carDist[now][i].getTime(), start, 1);
         }
         tmp[cnt]=-1;
         visit[i] = 0;
      }
   }
   void carPrint(int size) {
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
            System.out.print(dataTotal.carDist[i][j].getTime() + " ");
         }
         System.out.println();
      }
   }
   
   void ptPrint(int size) {
      for(int i=0; i<size; i++) {
         for(int j=0; j<size; j++) {
            System.out.print(dataTotal.ptDist[i][j].getTime() + " ");
         }
         System.out.println();
      }
   }
   
   void callDFS(int start, int end, int how, int equal){ 
      System.out.println(""+start);

      carPrint(listSize);

      ptPrint(listSize);
      min=Integer.MAX_VALUE;
      System.out.println("start : " + start);
      System.out.println("end : " + end);
      
      if(start!=end) {

            visit[start] = 1;
            tmp[0] = start;
            if(how==0) { 
               dfs(1, start, 0, end, 0);   
            }else { // 
               dfs(1, start, 0, end, 1);
            }
            tmp[0]=-1;
            visit[start]= 0;
      }else { 
    	  
         visit[start] = 1;
         tmp[0] = start;
         if(how==0) { // 
            dfsEqual(1, start, 0, start, 0);   
         }else { // 
            dfsEqual(1, start, 0, start, 1);
         }
         tmp[0]=-1;
         visit[start]= 0;
      }
      if(how == 0) {
	      
	      for(int i =0;i<listSize;i++) {
	         System.out.print(dataTotal.ptAns[i] +" ");
	      }
      }else {

	      for(int i =0;i<listSize;i++) {
	         System.out.print(dataTotal.carAns[i] +" ");
	      }
      }
      System.out.println();
   }
}