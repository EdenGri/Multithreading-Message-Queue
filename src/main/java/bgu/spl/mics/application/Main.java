package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;

import java.io.IOException;

import static bgu.spl.mics.application.passiveObjects.JsonReader.getInputFromJson;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */

public class Main {


	public static void main(String[] args) {
		if (args.length!=2){//todo check if args[0] orv args[1]
			System.out.println("Invalid argument");
			return;
		}
		String filePath=args[1];//todo check if args[0] orv args[1]
		try{
			Input input = getInputFromJson(filePath);

			LeiaMicroservice leia=new LeiaMicroservice(input.getAttacks());
			C3POMicroservice c3po=new C3POMicroservice();
			HanSoloMicroservice hsnSolo=new HanSoloMicroservice();
			R2D2Microservice r2d2=new R2D2Microservice(input.getR2D2());
			LandoMicroservice lando=new LandoMicroservice(input.getLando());

			Thread t1=new Thread(leia);
			Thread t2=new Thread(c3po);
			Thread t3=new Thread(hsnSolo);
			Thread t4=new Thread(r2d2);
			Thread t5=new Thread(lando);

			t1.start();
			t2.start();
			t3.start();
			t4.start();
			t5.start();

		}catch (IOException E){}

	}
}