package meteorology;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WetherAnalyserThreads { //er jefe
	
	private List<String> lines;

    public WetherAnalyserThreads(String filename) throws IOException {
        File archivo = new File(filename);
    	lines = Files.readAllLines(archivo.toPath());
    }
    
    public double analyze(StatType type) throws Exception{
    	int total = lines.size(); //cuantas lineas hay de datos
        int cores = Runtime.getRuntime().availableProcessors();//cuantos procesadores tiene el ordenador
        
        //llamamos al executor service
        ExecutorService exser = Executors.newFixedThreadPool(cores);
        List<Future<WetherPartialResult>> futures = new ArrayList<>(); //creamos los futures que guerdarásn los resultados
        
        int base= total / cores;//cantidad base de datos
        int resto = total % cores;//resto 0<=resto<cores

        int start = 0;
        
        for(int c=0; c<cores;c++) {//repartimos las tareas entre cores
        	int end = start + base;
        	if(c == cores-1) {
        		end += resto;
        	}
        	//para cada core asignamos una tarea
        	WetherCallable tarea = new WetherCallable(this.lines,start,end,type);
        	futures.add(exser.submit(tarea));
        	start=end;
        }
        
        //tenemos que juntar los resultados
        WetherPartialResult resultadoFinal = new WetherPartialResult(type);
        for(Future<WetherPartialResult> f: futures) {
        	resultadoFinal.merge(f.get());
        }
        exser.shutdown();
        return resultadoFinal.getFinalResult();
    }
    
    public static void main(String[] args) throws Exception {

        String file = "data_meteo.txt";
        StatType type = StatType.TOTAL_PRECIPITATION;

        long t0 = System.currentTimeMillis();//vamos a contar el tiempo q tarda

        WetherAnalyserThreads analyzer = new WetherAnalyserThreads(file);
        double result = analyzer.analyze(type);

        long t1 = System.currentTimeMillis();

        System.out.println("Resultado: " + result);
        System.out.println("Tiempo: " + (t1 - t0) + " ms");
    }
}
