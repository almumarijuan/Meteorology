package meteorology;

import java.util.List;
import java.util.concurrent.Callable;

public class WetherCallable implements Callable<WetherPartialResult>{
	
	private List<String> lineas;
	private int lo;
	private int hi;
	private StatType type;
	
	public WetherCallable (List<String> lineas, int lo, int hi, StatType type) {
		this.lineas = lineas;
        this.lo = lo;
        this.hi = hi;
        this.type = type;
	}
	
	@Override
	public WetherPartialResult call() throws Exception {
		WetherPartialResult partial = new WetherPartialResult(this.type);
		for(int i=lo; i<hi;i++) {
			String linea = lineas.get(i);
			String[] partes = linea.split(" ");
			switch (this.type) {
				case MAX_TEMPERATURE:
					partial.addTemperature(Double.parseDouble(partes[1]));
				case AVERAGE_HUMIDITY:
					partial.addHumidity(Double.parseDouble(partes[2]));
				case TOTAL_PRECIPITATION:
					partial.addPrecipitation(Double.parseDouble(partes[3]));
					
			}
		}
		return partial;
	}
	
}
