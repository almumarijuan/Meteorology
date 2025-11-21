package meteorology;

public class WetherPartialResult {
	private StatType type;
	private double maxTem; //si type es MAX_TEMPERATURE
	private double humiditySum;
	private double humidityCount;
	private double totalPrecipitation;
	
	public WetherPartialResult(StatType type) {
		this.type=type;
	}
	public void addTemperature(double t) {
        if(t>= maxTem) {
        	maxTem=t;
        }
    }

    public void addHumidity(double h) {
        this.humiditySum += h;
        this.humidityCount++;
    }

    public void addPrecipitation(double p) {
        this.totalPrecipitation += p;
    }

    public void merge(WetherPartialResult other) {
        if(other.maxTem >= this.maxTem) {
        	this.maxTem = other.maxTem;
        }
        humiditySum +=other.humiditySum;
        humidityCount += other.humidityCount;
        totalPrecipitation += other.totalPrecipitation;
    }

    public double getFinalResult() {
    	switch(type) {
    		case MAX_TEMPERATURE:
    			return maxTem;
    		case AVERAGE_HUMIDITY:
    			return humiditySum/humidityCount;
    		case TOTAL_PRECIPITATION:
    			return totalPrecipitation;
    	}
        return 0;
    }
	
}
