package dto;

import java.util.LinkedList;

public class InfoPT {
	private double sx; // 
	private double sy; // 
	private double ex; // 
	private double ey; // 
	private String firstStartStation; 
	private String lastEndStation;
	private int stationCount;
	private int totalDistance; // // 
	private boolean walk; // 
	private int totalTime; // 
	private int fare; //  : payment

	private LinkedList<InfoSectionPT> section;
	private LinkedList<DataPair> lineList; // 
	
	public InfoPT(){
		section = new LinkedList<InfoSectionPT>(); // 
		lineList = new LinkedList<DataPair>();
	}

	
	public int getStationCount() {
		return stationCount;
	}


	public void setStationCount(int stationCount) {
		this.stationCount = stationCount;
	}


	public int getSectionSize() {
		return section.size();
	}
	
	public double getSx() {
		return sx;
	}

	public void setSx(double sx) {
		this.sx = sx;
	}

	public double getSy() {
		return sy;
	}

	public void setSy(double sy) {
		this.sy = sy;
	}

	public double getEx() {
		return ex;
	}

	public void setEx(double ex) {
		this.ex = ex;
	}

	public double getEy() {
		return ey;
	}

	public void setEy(double ey) {
		this.ey = ey;
	}

	public int getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public boolean isWalk() {
		return walk;
	}

	public void setWalk(boolean walk) {
		this.walk = walk;
	}
	

	public String getFirstStartStation() {
		return firstStartStation;
	}

	public void setFirstStartStation(String firstStartStation) {
		this.firstStartStation = firstStartStation;
	}

	public String getLastEndStation() {
		return lastEndStation;
	}

	public void setLastEndStation(String lastEndStation) {
		this.lastEndStation = lastEndStation;
	}

	public int getFare() {
		return fare;
	}

	public void setFare(int fare) {
		this.fare = fare;
	}
	
	public int getLineListSize() {
	      return lineList.size();
	}
	
	public InfoSectionPT getSection(int idx) {
		return section.get(idx);
	}
	public void addSection(InfoSectionPT item) {
		section.add(item);
	}
	public DataPair getLineList(int idx) {
		return lineList.get(idx);
	}
	public void addLineList(DataPair pair) {
		lineList.add(pair);
	}

	public void print() {
		System.out.println("sx, sy= "+this.getSx()+","+this.getSy());
		System.out.println("ex, ey= "+this.getEx()+","+this.getEy());
		System.out.println("첫 정거장, 마지막 정거장 : " + this.getFirstStartStation() + " , " + this.getLastEndStation());
		System.out.println("총거리 , 총시간 : " + this.getTotalDistance() + ", " + this.getTotalTime());
		System.out.println("총비용 : " + this.getFare());
		System.out.println("총환승 횟수 : " + this.getSectionSize());
		
		for(int i=0; i<this.getSectionSize(); i++) {
			InfoSectionPT b = this.getSection(i);
			System.out.println("1:지하철, 2:버스 = " + b.getTrafficType());
			System.out.println("시작, 끝 = " + b.getStartStation() + " , " + b.getEndStation());
			System.out.println("거리, 시간 = " + b.getSectionDistance() + ", " + b.getSectionTime());
			if(b.getTrafficType()==1) {
				System.out.println("지하철 노선 : " + b.getSubwayLine());	
			}else {
				for(int j=0; j<b.getBusNoListSize(); j++) 
					System.out.println("버스 : " + b.getBusNoList(j));
			}
		}
	}
}
