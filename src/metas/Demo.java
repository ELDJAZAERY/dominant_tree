package metas;

import data.reader.Instances;
import metas.BBO.BBO;
import metas.BSO.BSO_Algo;
import metas.GA.GA;

public class Demo {


	// TODO Check Local Search BBO
	// TODO connect and isCennected Function optimisation
	// TODO Re-Do BBO as beginng


	public static void main(String[] args) {

        String path = "bench_marks\\100\\500_1.txt";

        new Instances(path);

		BBO bbo = new BBO(300,10,(float) 0.005);
        GA ga = new GA(50,10000);

        //ga.Exec();
		bbo.BBO_Exec();
        //BSO_Algo.Exec(10,5,1000);

        //heuristic.Exec();
	}

}
